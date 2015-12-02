package com.epsmart.wzdp.http.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.db.dao.DaoManager;
import com.epsmart.wzdp.db.table.PageDateTable;
import com.epsmart.wzdp.db.table.SubmitDateTable;
import com.epsmart.wzdp.db.table.TableImpi;
import com.epsmart.wzdp.db.table.TemplateTable;
import com.epsmart.wzdp.http.request.model.RequestParameter;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.util.JavaUtil;
import com.j256.ormlite.dao.Dao;

/**
 * @Title 异步Http请求
 * @Descriptoin 线程的终止工作交给线程池,当activity停止的时候,设置回调函数为false ,就不会执行回调方法
 * @author fony
 * @param <E>
 */
public class AsyncHttpRequest<E> extends BaseRequest {
	private static final long serialVersionUID = -3509046726762405265L;

	private DefaultHttpClient httpClient = null;
	protected List<RequestParameter> parameters = null;
	private RequestAction requestAction;
	private Map<String, String> requestHeaders = new HashMap<String, String>();
	private int timeout = 60000;// 20s
	protected Context context;
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @param url
	 * @param parameters
	 * @param requestCallback
	 */
	public AsyncHttpRequest(String url, List<RequestParameter> parameters,
			RequestCallback requestCallback) {
		this.uriStr = url;
		this.parameters = parameters;
		this.requestCallback = requestCallback;

		if (null == httpClient)// http请求
			httpClient = new DefaultHttpClient();
	}

	public AsyncHttpRequest(RequestAction requestAction,
			RequestCallback requestCallback) {
		this.requestAction = requestAction;
		this.requestCallback = requestCallback;
		if (null == httpClient)
			httpClient = new DefaultHttpClient();
	}

	public void run() {
		try {
			if (requestType == RequestType.HTTP) {
				responseProcess(buildAndExecuteHttpRequest());
			} else if (requestType == RequestType.WEBSERVICE) {
				responseProcess(buildAndExecuteWebServiceRequest());
			} else if (requestType == RequestType.THRIFT) {
				responseProcess(getResultFroThrift());
			} else {
				throw new Exception("couldn't set the request type");
			}
		} catch (java.lang.IllegalArgumentException e) {
			HttpException exception = new HttpException(
					HttpException.IO_EXCEPTION, "服务器连接错误", e, request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (org.apache.http.conn.ConnectTimeoutException e) {
			HttpException exception = new HttpException(
					HttpException.SOCKET_TIMEOUT_EXCEPTION, "服务器连接超时", e,
					request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (java.net.SocketTimeoutException e) {
			HttpException exception = new HttpException(
					HttpException.SOCKET_TIMEOUT_EXCEPTION, "服务器数据读取超时", e,
					request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (UnsupportedEncodingException e) {
			HttpException exception = new HttpException(
					HttpException.UNSUPPORTED_ENCODEING_EXCEPTION, "编码错误", e,
					request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (org.apache.http.conn.HttpHostConnectException e) {
			HttpException exception = new HttpException(
					HttpException.CONNECT_EXCEPTION, "服务器连接错误", e, request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (ClientProtocolException e) {
			HttpException exception = new HttpException(
					HttpException.CLIENT_PROTOL_EXCEPTION, "客户端协议异常", e,
					request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
			e.printStackTrace();
		} catch (IOException e) {
			HttpException exception = new HttpException(
					HttpException.IO_EXCEPTION, "服务器数据读取异常", e, request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);
		} catch (Exception e) {
			Log.w("Exception", "Exception 服务器提交异常信息=" + e);
			HttpException exception = new HttpException(
					HttpException.EXCEPTION, "服务响应失败！", e, request);
			AsyncHttpRequest.this.requestCallback.onFail(exception);

		}
	}

	/**
	 * 添加缓存数据的处理 先判断网络状态 1、固定模板，较长时间不需要更新。 2、保存待提交，提交后清除数据。 3、需要区分哪些是模板
	 * 哪些是需要提交保存的？ 4、列表的需要实时刷新的数据 每天刷新一下缓存数据：先判断是
	 * 
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	@SuppressWarnings("unchecked")
	protected String executeWebServiceRequest(RequestPram params)
			throws IOException, XmlPullParserException {
		DaoManager<E> dm = (DaoManager<E>) DaoManager.getInstance();
		dm.setContext(context);
		Dao<E, Integer> dao = null;
		TableImpi tableImpi = null;
		boolean isInsertorUpdate = false;// 是否需要更新 小于一天的不更新缓存数据
		boolean isReal = false;// 是否存在记录
		boolean isOnline = ((AppContext) (((Activity) context).getApplication())).isOnline;// 当前网络状况
		List<E> list = null;
		String state =null;// 0 获取用户权限  1 分页相关 2 表单相关 3 提交表单相关
		StringBuilder result = new StringBuilder();
		SoapObject request = new SoapObject(
				RequestParamConfig.serviceNameSpace, params.methodName);
		//Log.i("asy", ".........params...>>"+params.toString());
		if (params.methodName.equals("userLogin")) {// 权限
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("pwd", "");
			dao = dm.getDao(TemplateTable.class);
			state = "0";
			// 离线的时候才需要去查询 并进行显示处理
		} else if (params.methodName.equals(RequestParamConfig.procedureReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";

		} else if (params.methodName.equals(RequestParamConfig.keyPotReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName.equals(RequestParamConfig.projectlistReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName
				.equals(RequestParamConfig.procedureDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName.equals(RequestParamConfig.procedureUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.issuelistDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName
				.equals(RequestParamConfig.qualityIssueDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.qualityIssueUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.keyPotQueryDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.keyPotQueryUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName.equals(RequestParamConfig.projectQueryReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.projectNodeUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.materialslistReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName
				.equals(RequestParamConfig.materialsReceiveReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.projectReceiveUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.materialsListReq2)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName
				.equals(RequestParamConfig.servicelistDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName
				.equals(RequestParamConfig.newserviceslistupload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.newServicelistDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName.equals(RequestParamConfig.serviceDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.serviceRequireUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName.equals(RequestParamConfig.contructAnlysis)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName.equals(RequestParamConfig.contractsign)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName.equals(RequestParamConfig.materialsupply)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.projectmilestone)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName.equals(RequestParamConfig.dairylistReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("usertype", params.user_type);
			request.addProperty("pageNo", Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		} else if (params.methodName.equals(RequestParamConfig.dairyReceiveReq)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(TemplateTable.class);
			state = "2";
		} else if (params.methodName
				.equals(RequestParamConfig.dairyReceiveUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param", params.param);
			dao = dm.getDao(SubmitDateTable.class);
			state = "3";
		} else if (params.methodName
				.equals(RequestParamConfig.linelistDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize",Integer.toString(params.pageSize));
			dao = dm.getDao(PageDateTable.class);
			state = "1";
		}else if (params.methodName.equals(RequestParamConfig.ehvProjectList)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize",Integer.toString(params.pageSize));
			request.addProperty("type", params.password);
		}else if (params.methodName.equals(RequestParamConfig.ehvPlanDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.ehvReportDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.ehvplanrep)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.ehvTempDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}
//		else if (params.methodName.equals(RequestParamConfig.ehvPlantempUpload)) {
//			request.addProperty("username", Integer.toString(params.userName));
//			request.addProperty("param",params.param);
//		}else if (params.methodName.equals(RequestParamConfig.ehvReporttempUpload)) {
//			request.addProperty("username", Integer.toString(params.userName));
//			request.addProperty("param",params.param);
//		}
		else if (params.methodName.equals(RequestParamConfig.ehvPlan)) {
			request.addProperty("pspid",params.param);
		}else if (params.methodName.equals(RequestParamConfig.ehvReportReq)) {
			request.addProperty("pspid",params.param);
		}else if (params.methodName.equals(RequestParamConfig.getSubList)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("pagenum",Integer.toString(params.pageNo));
			request.addProperty("pageSize",Integer.toString(params.pageSize));
		}else if (params.methodName.equals(RequestParamConfig.getReplayList)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.submitReplay)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
			request.addProperty("subid",params.password);
		}else if (params.methodName.equals(RequestParamConfig.buildSub)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.getPersionList)) {
			request.addProperty("username", Integer.toString(params.userName));
//			request.addProperty("param",params.param);
		}else if(params.methodName.equals(RequestParamConfig.getHeadImage)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("headimage",params.param);
		}else if(params.methodName.equals(RequestParamConfig.getHeadImg)){
			request.addProperty("username", Integer.toString(params.userName));
		}else if(params.methodName.equals(RequestParamConfig.delSubject)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("subid", params.password);
		}else if(params.methodName.equals(RequestParamConfig.getNoRedNum)){
			request.addProperty("username", Integer.toString(params.userName));
		}else if(params.methodName.equals(RequestParamConfig.getNoticeList)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
		}else if(params.methodName.equals(RequestParamConfig.getNoticeDetail)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("noticeid", params.password);
		}else if (params.methodName.equals(RequestParamConfig.LocationUpdate)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.ehvMaterialprocedure)) {
			request.addProperty("param",params.param);
		}else if(params.methodName.equals(RequestParamConfig.uhvMateriallist)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
		}else if (params.methodName.equals(RequestParamConfig.DelplanDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.DelplanUpload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if(params.methodName.equals(RequestParamConfig.delplanlist)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
		}else if(params.methodName.equals(RequestParamConfig.Delplanlinelist)){
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
			request.addProperty("pageNo",Integer.toString(params.pageNo));
			request.addProperty("pageSize", Integer.toString(params.pageSize));
		}else if (params.methodName.equals(RequestParamConfig.DeldepartureDownload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.Deldepartureupload)) {
			request.addProperty("username", Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.deldeterupload)) {
			request.addProperty("jhhh",params.password);
			request.addProperty("param",params.param);

		}else if (params.methodName.equals(RequestParamConfig.delconfirmDownload)) {
			request.addProperty("username",Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.delconfirmupload)) {
			request.addProperty("username",Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}else if (params.methodName.equals(RequestParamConfig.queryDenial)) {
			request.addProperty("username",Integer.toString(params.userName));
			request.addProperty("param",params.param);
		}

		else{
			Log.w("AsyncHttpReqest","接口名错误或参数不正确！！");
		}
		// 返回离线数据的操作 1、模板信息 2、提交信息  queryDenial
		if (!isOnline&&!TextUtils.isEmpty(state)) {
			try {
				if ("2".equals(state)) {
					list = (List<E>) dm.getMoreWhereList(
							new String[] { "methodName", "userName", "param" },
							new String[] { params.methodName,
									Integer.toString(params.userName),
									params.param });

				} else if ("1".equals(state)) {
					list = (List<E>) dm.getMoreWhereList(
							new String[] { "methodName", "userName", "param",
									"pageNo", "pageSize"},
							new String[] { params.methodName,
									Integer.toString(params.userName),
									params.param,
									Integer.toString(params.pageNo),
									Integer.toString(params.pageSize) });

					if (list.size() > 0) {
						isReal = true;
						tableImpi = ((PageDateTable) list.get(0));
						return ((PageDateTable) tableImpi).getValue();
					} else {
						return "";
					}
				} else if ("0".equals(state)) {
					// 查询的权限的
					list = (List<E>) dm.getMoreWhereList(
							new String[] { "methodName", "userName" },
							new String[] { params.methodName,
									Integer.toString(params.userName) });
				}else if("3".equals(state)){// 提交表单相关 这里只有写操作
					SubmitDateTable submitData = null;
					submitData = new SubmitDateTable();
					submitData.setMethodName(params.methodName);
					submitData.setParam(params.param);
					submitData.setUserName(Integer.toString(params.userName));
					submitData.setDate(new Date());
					submitData.setState("0");// 缓存未上传
					try {
						dao.create((E) submitData);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return "<opdetail><result>0</result><message>离线数据已保存！</message></opdetail>";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 存在记录
			if (list.size() > 0) {
				isReal = true;
				tableImpi = ((TemplateTable) list.get(0));
				return ((TemplateTable) tableImpi).getValue();
			} else {
				return "";
			}
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		Log.w("AsyncHttp", ".....请求参数....request"+request.toString());
		envelope.bodyOut = request;

		(new MarshalBase64()).register(envelope);
		HttpTransportSE transport = new HttpTransportSE(
				RequestParamConfig.ServerUrl, timeout);
		transport.debug = true;
		
		transport.call(RequestParamConfig.serviceNameSpace + params.methodName,
				envelope);
		String respStr = "";
		if (envelope.getResponse() != null) {
			if (envelope.getResponse() instanceof SoapObject) {
				SoapObject response = (SoapObject) envelope.getResponse();
				int count = response.getPropertyCount();
				for (int i = 0; i < count; i++) {
					result.append(response.getProperty(i).toString());
				}
				respStr = result.toString();
			} else if (envelope.getResponse() instanceof SoapPrimitive) {
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				respStr = response.toString();
				System.out.println("........返回的结果..>>"+respStr);
			}
		}
		// 进行数据保存
		if (respStr != null&&isOnline) {
			if ("1".equals(state)) {
				PageDateTable pagetable = null;
				if (isReal) {
					pagetable = (PageDateTable) tableImpi;
					Date startDate = pagetable.getDate();// 判断当前日
					try {
						isInsertorUpdate = JavaUtil.compare24(new Date(),
								startDate);
						if (isInsertorUpdate) {
							// 更新记录
							pagetable.setDate(new Date());
							pagetable.setValue(respStr);
							dao.update((E) pagetable);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// 插入新记录
					pagetable = new PageDateTable();
					pagetable.setMethodName(params.methodName);
					pagetable.setParam(params.param);
					pagetable.setUserName(Integer.toString(params.userName));
					pagetable.setValue(respStr);
					pagetable.setPageNo(Integer.toString(params.pageNo));
					pagetable.setPageSize(Integer.toString(params.pageSize));
					pagetable.setState("0");
					try {
						dao.create((E) pagetable);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

			} else if("2".equals(state)){
				TemplateTable templatetable = null;
				if (isReal) {
					templatetable = (TemplateTable) tableImpi;
					Date startDate = templatetable.getDate();// 判断当前日
					try {
						isInsertorUpdate = JavaUtil.compare24(new Date(),
								startDate);
						if (isInsertorUpdate) {
							// 更新记录
							templatetable.setDate(new Date());
							templatetable.setValue(respStr);
							dao.update((E) templatetable);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					// 插入新记录
					templatetable = new TemplateTable();
					templatetable.setMethodName(params.methodName);
					templatetable.setParam(params.param);
					templatetable
							.setUserName(Integer.toString(params.userName));

					templatetable.setValue(respStr);
					templatetable.setState("0");// 离线缓存 未上传
					try {
						dao.create((E) templatetable);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else if("0".equals(state)){
				TemplateTable templatetable = null;
				if (isReal) {
					templatetable = (TemplateTable) tableImpi;
					Date startDate = templatetable.getDate();// 判断当前日
					try {
						isInsertorUpdate = JavaUtil.compare24(new Date(),
								startDate);
						if (isInsertorUpdate) {
							// 更新记录
							templatetable.setDate(new Date());
							templatetable.setValue(respStr);
							dao.update((E) templatetable);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					// 插入新记录
					templatetable = new TemplateTable();
					templatetable.setMethodName(params.methodName);
					templatetable.setParam(params.param);
					templatetable
							.setUserName(Integer.toString(params.userName));

					templatetable.setValue(respStr);
					templatetable.setState("0");// 离线缓存 未上传
					try {
						dao.create((E) templatetable);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//Log.d("Asy", "back result=" + respStr);
		return respStr;
	}

	// 本地文件 测试用例
	// protected String buildAndExecuteWebServiceRequest() {
	// Bundle queryBundle = requestAction.queryBundle;
	// String fileName = (String) queryBundle.get("txt");
	// Log.i("AsyncHttpRequest", "....text fileName..>>" + fileName);
	// return getFileToStr(fileName);//
	// }

	@Override
	protected String buildAndExecuteWebServiceRequest() throws IOException,
			XmlPullParserException {
		StringBuilder result = new StringBuilder();
		SoapObject request = new SoapObject(serviceNameSpace,
				requestAction.serviceName);
		Log.w("AsyncHttpRequest", "serviceUrl=" + serviceUrl);
		Log.w("AsyncHttpRequest", "serviceName=" + requestAction.serviceName);
		Log.w("AsyncHttpRequest", "" + requestAction);
		// 2.设置调用方法参数
		if (requestAction.queryKeys.size() >= 1) {
			List<String> queryKeys = requestAction.queryKeys;
			Bundle queryBundle = requestAction.queryBundle;
			int size = queryKeys.size();
			for (int i = 0; i < size; i++) {
				request.addProperty(queryKeys.get(i),
						queryBundle.get(queryKeys.get(i)));
				Log.i("property", ".........>>property=" + queryKeys.get(i)
						+ ".....>>value=" + queryBundle.get(queryKeys.get(i)));
			}
		}

		if (null != requestAction.pageBean && requestAction.isPageBeanEnable) {
			List<String> queryKeys = requestAction.pageBean.getQueryKeys();
			Bundle queryBundle = requestAction.pageBean.getQueryBundle();
			int size = queryKeys.size();
			for (int i = 0; i < size; i++) {
				request.addProperty(queryKeys.get(i),
						queryBundle.getInt(queryKeys.get(i)));
				Log.i("property", ".......分页参数..>>property=" + queryKeys.get(i)
						+ ".....>>value=" + queryBundle.get(queryKeys.get(i)));
			}
		}
		// 3.设置SOAP请求信息(参数部分为SOAP协议版本号,与你要调用的webService中版本号一致)
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
	//	Log.i("", ".........请求数据....envelope.bodyOut...>>" + envelope.bodyOut);
		// 4.注册Envelope
		(new MarshalBase64()).register(envelope);
		// 5.构建传输对象,并指明WSDL文档URL,Android传输对象
		HttpTransportSE transport = new HttpTransportSE(serviceUrl, timeout);
		transport.debug = true;
		envelope.implicitTypes=true;
		// 6.调用WebService(其中参数为1:命名空间+方法名称,2:Envelope对象) envelope.bodyIn
		transport.call(serviceNameSpace + requestAction.serviceName, envelope);
		//Log.i("", ".........请求返回....envelope.bodyIn...>>" + envelope.bodyIn);
		if (envelope.getResponse() != null) {
			if (envelope.getResponse() instanceof SoapObject) {
				SoapObject response = (SoapObject) envelope.getResponse();
				int count = response.getPropertyCount();
				for (int i = 0; i < count; i++) {
					result.append(response.getProperty(i).toString());
				}
				String backresult = result.toString();
				// writeLogInfo(requestAction.serviceName,backresult);
				return backresult;
			} else if (envelope.getResponse() instanceof SoapPrimitive) {
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

				String respStr = response.toString();
				// writeLogInfo(requestAction.serviceName,respStr);
				return respStr;
			}
		}
		return "";
	}

	protected String getResultFroThrift() {

		RequestPram reqPram = requestAction.getReqPram();
		String result = null;

		if (null != requestAction.pageBean && requestAction.isPageBeanEnable) {
			Bundle queryBundle = requestAction.pageBean.getQueryBundle();
			reqPram.pageNo = Integer.parseInt(queryBundle.get("startPage")
					.toString());
			reqPram.pageSize = Integer.parseInt(queryBundle.get("pageNum")
					.toString());
		}
		try {
			result = executeWebServiceRequest(reqPram);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String getFileToStr(String fileName) {
		StringBuffer temp = new StringBuffer();
		try {
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(fileName);
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String tempLine = rd.readLine();
			while (tempLine != null) {
				temp.append(tempLine);
				tempLine = rd.readLine();
			}
		} catch (Exception e) {
		}
		String result = temp.toString();
		return result;
	}

	private void writeLogInfo(String logName, String respStr) {
		try {
			FileWriter fw = new FileWriter("/sdcard/DCIM/Camera/" + logName
					+ "_log.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(respStr);
			bw.close();
			fw.close();
		} catch (Exception e) {
		}

	}

	protected Map<String, String> buildWebServiceRequest() {
		Map<String, String> params = new HashMap<String, String>();
		if (null == parameters)
			return params;
		if (null != parameters && parameters.size() > 0) {
			int len = parameters.size();
			RequestParameter reqParam = null;
			for (int i = 0; i < len; i++) {
				reqParam = parameters.get(i);
				params.put(reqParam.getName(), reqParam.getValue());
			}
		}
		return params;
	}

	protected HttpResponse buildAndExecuteHttpRequest()
			throws ClientProtocolException, IOException, Exception {
		try {
			return httpClient.execute(buildHttpUriRequest());
		} catch (ClientProtocolException e) {
			throw new ClientProtocolException(e);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	protected HttpUriRequest buildHttpUriRequest() throws Exception {
		request = new HttpPost(uriStr);
		setHeaders();
		return request;
	}

	private void setHeaders() {
		for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
			request.addHeader(header.getKey(), header.getValue());
			log(header.getKey() + ": " + header.getValue());
		}
	}

	public String encodeParameters() {
		return null;
	}

	private static void log(String message) {
		System.out.println("[" + new java.util.Date() + "]" + message);
	}

	protected void responseProcess(String httpResponse) {
		Response response = new Response();
		response.setResponseAsString(httpResponse);
		requestCallback.onSuccess(response);
	}

	protected void responseProcess(HttpResponse httpResponse) throws Exception {
		try {
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				requestCallback.onSuccess(new Response(httpResponse));
			} else {
				HttpException exception = new HttpException(
						HttpException.IO_EXCEPTION, "响应码异常,响应码：" + statusCode,
						request);
				requestCallback.onFail(exception);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void setRequestHeader(String name, String value) {
		requestHeaders.put(name, value);
	}

	public String getRequestHeader(String name) {
		return requestHeaders.get(name);
	}

	public void setRequestAction(RequestAction requestAction) {
		this.requestAction = requestAction;
	}

}
