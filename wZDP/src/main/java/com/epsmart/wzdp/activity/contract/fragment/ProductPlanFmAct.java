package com.epsmart.wzdp.activity.contract.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.achar.service.AbstractChart;
import com.epsmart.wzdp.activity.achar.service.CharResponse;
import com.epsmart.wzdp.activity.achar.service.CharXmlHandler;
import com.epsmart.wzdp.activity.achar.service.TableViewShow;
import com.epsmart.wzdp.activity.contract.CommonReqAct;
import com.epsmart.wzdp.activity.contract.fragment.fsch.FileExplorer;
import com.epsmart.wzdp.activity.contract.fragment.fsch.FileExplorer.FileExplorerListener;
import com.epsmart.wzdp.activity.contract.fragment.fsch.SFTPChannel;
import com.epsmart.wzdp.activity.contract.fragment.fsch.SFTPConstants;
import com.epsmart.wzdp.activity.contract.fragment.fsch.ToastTool;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.search.QueryPlanDialog;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.CommonRegex;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.jcraft.jsch.ChannelSftp;

/*
 * 铁塔排产计划信息/下载周报、选择、上传  表格数据显示页面  
 */
public class ProductPlanFmAct extends CommonReqAct {
	protected QueryPlanDialog queryPlanDialog;
	private List<String> uploadFiles;
	private String templetaddress;
	private File file;
	private TextView tv;
	private LinearLayout show_content;
	private LinearLayout bottom_btns;
	private Button download_btn;// 下载按钮
	private Button select_btn;// 选择按钮
	private Button upload_btn;// 上传按钮

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.comm_frgment_act);// 单独布局
		activity = this;
		initUI();
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.plan_cs);
		search_lay.setOnClickListener(searchLisp);// 查询
		search_btn.setOnClickListener(searchLisp);

		bottom_btns.setVisibility(View.VISIBLE);
		download_btn.setOnClickListener(btnsLister);
		select_btn.setOnClickListener(btnsLister);
		upload_btn.setOnClickListener(btnsLister);
		initNet();
		loadData(new RequestPram());
	}

	private void initUI() {
		app_context = (AppContext) activity.getApplication();
		search_lay = (RelativeLayout) getActionBar().getCustomView()
				.findViewById(R.id.search_lay);
		search_btn = (Button) getActionBar().getCustomView().findViewById(
				R.id.search_btn);
		show_content = (LinearLayout) findViewById(R.id.show_content);
//		tv = (TextView) findViewById(R.id.show_title);

		bottom_btns = (LinearLayout) findViewById(R.id.bottom_btns);
		download_btn = (Button) findViewById(R.id.download_btn);
		select_btn = (Button) findViewById(R.id.select_btn);
		upload_btn = (Button) findViewById(R.id.upload_btn);

	}

	// 实现回调
	public void initViews(View view) {

	}

	OnClickListener btnsLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select_btn:
				FileExplorer explorer = FileExplorer.openFileExplorer(activity,
						explorerListener, Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "");
				try {
					explorer.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.upload_btn:
				if (null == uploadFiles || uploadFiles.size() < 1) {
					ToastTool.toast(activity, "未添加附件");
					return;
				}
				new UploadTask(activity).execute("");
				break;
			case R.id.download_btn:
				RequestPram requestPram = new RequestPram();
				loadDataDown(requestPram);
				break;
			}
		}
	};

	/** 下载 */
	public void loadDataDown(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
				.getReqXML("plan", "1"));
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		//requestPram.userName = MainActivity.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvTempDownload;
		requestAction.setReqPram(requestPram);
		// super.loadData(requestPram);
		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	private FileExplorerListener explorerListener = new FileExplorerListener() {

		@Override
		public void onClosed(List<String> selectedFiles) {
			Log.d("FileExplorerListener", "onClosed," + selectedFiles);
			uploadFiles = selectedFiles;
			final LinearLayout uploadFilesLayout = (LinearLayout) activity
					.findViewById(R.id.uploadFilesLayout);
			uploadFilesLayout.removeAllViews();
			if (null == selectedFiles || selectedFiles.size() < 1)
				return;

			int size = selectedFiles.size();
			for (int i = 0; i < size; i++) {
				final ImageView imageView = new ImageView(activity);
				imageView.setTag(selectedFiles.get(i));
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						uploadFiles.remove((String) (v.getTag()));
						uploadFilesLayout.removeView(imageView);
					}
				});
				imageView.setBackgroundResource(R.drawable.file);
				uploadFilesLayout.addView(imageView);
			}
		}
	};

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}

		return filename;

	}

	// 附件上传的异步操作
	class UploadTask extends AsyncTask<String, Void, String> {

		private Handler handler;

		UploadTask(final Context context) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 0:
						ToastTool.toast(context, "附件上传失败！");
						break;
					case 1:
						ToastTool.toast(context, "附件上传成功！");
						break;
					}
				}
			};
		}

		@Override
		protected String doInBackground(String... params) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
					.getDateTimeInstance();
			sdf.applyPattern("yyyyMMddhhmmss");
			for (int i = 0; i < 1; i++) {
				file = new File(uploadFiles.get(0));
				String filename = sdf.format(calendar.getTime()) + "_"
						+ Math.random() + "."
						+ getExtensionName(file.getName());
				String remoteFilePath = "/opt/Tomcat6/webapps/wzdp/wzdp/app/downloadApp/"
						+ filename;
				JschUpload(file.getAbsolutePath(), remoteFilePath, handler,
						filename);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			showModuleProgressDialog("提示", "附件上传中,请稍后...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (null != result) {
				ToastTool.toast(activity, result);
			}
		}
	}

	/**
	 * jsch 方式上传附件
	 * 
	 * @param src
	 * @param dst
	 */
	private void JschUpload(String src, String dst, Handler handler,
			String filename) {
		Map<String, String> sftpDetails = new HashMap<String, String>();
		// 设置主机ip，端口，用户名，密码
		sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "127.0.0.1");
		sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "Admin@123");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "28022");
		SFTPChannel channel = getSFTPChannel();
		ChannelSftp chSftp = null;
		try {
			chSftp = channel.getChannel(sftpDetails, 60000);
			chSftp.put(src, dst, ChannelSftp.OVERWRITE); // 代码段2
			handler.sendEmptyMessage(1);

			RequestAction request = new RequestAction();
			request.serviceName = "ehvTempUpload";
			request.putParam("userName",
					Integer.toString(appContext.user.getUid()));
			request.putParam(
					"reqParam",
					RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("filename", filename)
							.append(RequestXmlHelp
									.getReqXML("templet",
											"/opt/Tomcat6/webapps/wzdp/wzdp/app/downloadApp/"))
							.append(RequestXmlHelp.getReqXML("pspid",
									getIntent().getStringExtra("PSPID")))
							.append(RequestXmlHelp.getReqXML("type", "1"))));
			String resultback = upload(request);

			if (resultback == null) {
				return;
			}
			String result = CommonRegex.getMiddleValue("result", resultback);
			if ("0".equals(result)) {
				uHandler.obtainMessage(0).sendToTarget();
			} else {
				uHandler.obtainMessage(1).sendToTarget();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		} finally {
			if(chSftp!=null){
				chSftp.quit();
			}
			try {
				channel.closeChannel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 处理网络异常等信息 **/
	private BaseHandler uHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				Toast.makeText(activity, "上传成功！", Toast.LENGTH_LONG).show();
				break;
			case 1:

				break;
			default:
				break;
			}
		}

	}

	// 下载附件
	private ProgressDialog mProDialog;

	private void jschDown(final String src, final String dst) {

		if (mProDialog == null) {
			mProDialog = ProgressDialog.show(activity, null, "正在下载...", true,
					true);
		}

		final Handler handlerdown = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mProDialog != null && mProDialog.isShowing()) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				switch (msg.what) {
				case 0:
					ToastTool.toast(activity, "下载失败！");
					break;
				case 1:
					ToastTool.toast(activity, "下载成功！");
					break;
				}
			}
		};

		new Thread() {
			@Override
			public void run() {

				Map<String, String> sftpDetails = new HashMap<String, String>();
				sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "127.0.0.1");
				sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
				sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "Admin@123");
				sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "28022");
				SFTPChannel channel = null;
				ChannelSftp chSftp = null;
				try {
					channel = getSFTPChannel();
					chSftp = channel.getChannel(sftpDetails, 60000);

					chSftp.get(dst, src);
					handlerdown.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
					handlerdown.sendEmptyMessage(0);

				} finally {
					if(chSftp!=null){
						chSftp.quit();
					}
					try {
						channel.closeChannel();
					} catch (Exception e) {
						handlerdown.sendEmptyMessage(0);
						e.printStackTrace();
					}
				}

			}

		}.start();

	}

	public SFTPChannel getSFTPChannel() {
		return new SFTPChannel();
	}

	// 附件上传成功后的 附件信息上传调用接口 webservices
	public String upload(RequestAction requestAction) {
		StringBuilder result = new StringBuilder();
		String ras = null;
		Log.d("upload", "" + requestAction);
		// 1.实例化SoapObject 对象,指定webService的命名空间,以及调用方法名称
		Log.d("serviceName", requestAction.serviceName);
		SoapObject request = new SoapObject(
				RequestParamConfig.serviceNameSpace, requestAction.serviceName);// 这里需要放开
		// 2.设置调用方法参数
		if (requestAction.queryKeys.size() >= 1) {
			List<String> queryKeys = requestAction.queryKeys;
			Bundle queryBundle = requestAction.queryBundle;
			int size = queryKeys.size();
			for (int i = 0; i < size; i++) {
				request.addProperty(queryKeys.get(i),
						queryBundle.get(queryKeys.get(i)));
			}
		}

		// 3.设置SOAP请求信息(参数部分为SOAP协议版本号,与你要调用的webService中版本号一致)
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		// 4.注册Envelope
		(new MarshalBase64()).register(envelope);
		// 5.构建传输对象,并指明WSDL文档URL,Android传输对象
		HttpTransportSE transport = new HttpTransportSE(
				RequestParamConfig.ServerUrl);
		transport.debug = true;
		// 6.调用WebService(其中参数为1:命名空间+方法名称,2:Envelope对象)
		try {
			transport.call(RequestParamConfig.serviceNameSpace
					+ requestAction.serviceName, envelope);
			if (envelope.getResponse() != null) {
				if (envelope.getResponse() instanceof SoapObject) {
					SoapObject response = (SoapObject) envelope.getResponse();
					int count = response.getPropertyCount();
					for (int i = 0; i < count; i++) {
						result.append(response.getProperty(i).toString());
					}
					ras = result.toString();
				} else if (envelope.getResponse() instanceof SoapPrimitive) {
					SoapPrimitive response = (SoapPrimitive) envelope
							.getResponse();
					ras = response.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return ras;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/** 数据请求 */
	public void loadData(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.param = getIntent().getStringExtra("reqParam");
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvPlanDownload;
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, new CharXmlHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	{
		listener = new QueryDialogListener() {
			@Override
			public void doQuery(String req) {
				RequestPram requestPram = new RequestPram();
				requestPram.param = req;
				loadDataq(requestPram);

			}
		};
	}

	// 查询
	protected OnClickListener searchLisp = new OnClickListener() {
		@Override
		public void onClick(View v) {
			activity.setVers(getIntent().getStringExtra("reqPq"));
			activity.setPspid(getIntent().getStringExtra("PSPID"));
			queryPlanDialog = new QueryPlanDialog(activity, listener,
					httpModule);// TODO
			queryPlanDialog.show(v);
		}
	};

	/** 点击查询按钮数据请求 */
	public void loadDataq(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvPlanDownload;
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, new CharXmlHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			closeDialog();
			if (parseObj instanceof BasicResponseNew) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			} else if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			} else if (parseObj instanceof CharResponse) {
				mHandler.obtainMessage(3, parseObj).sendToTarget();
			}
		}
	}

	private FlowHandler mHandler = new FlowHandler();
	private BasicResponse response;
	private BasicResponseNew responseNew;

	private class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {//
				onSuccessNew((BasicResponseNew) msg.obj);
			} else if (msg.what == 1) {// 返回数据为空 或 查询异常信息
				onSuccess((BasicResponse) msg.obj);
			} else if (msg.what == 3) {
				onSuccessTable((CharResponse) msg.obj);
			}
		}
	}

	/**
	 * 绘制图表
	 * 
	 * @param response
	 */
	private ArrayList<BasicEntity> list;

	private void onSuccessTable(CharResponse charResponse) {
		WindowManager wm = activity.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		AbstractChart charShow = null;
		String title = null;
		list = charResponse.entityList;
		for (int i = 0; i < list.size(); i++) {
			BasicEntity entiry = list.get(i);
			String charType = entiry.fields.get("chartype").fieldContent;
			title = entiry.fields.get("title").fieldContent;
			if (charType.equals("table")) {
				charShow = new TableViewShow(activity, title);
			}
			charShow.execute(entiry);
		}
		tv.setText(title);
		show_content.removeAllViews();
		
		show_content.addView(charShow.getview());
	}

	private void onSuccess(BasicResponse response) {
		Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show();
	}

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;

		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		templetaddress = response.basicEntityList.get(0).fields
				.get("templetaddress").fieldContent;
		final String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		if (TextUtils.isEmpty(templetaddress))
			templetaddress = "/opt/Tomcat6/webapps/wzdp/wzdp/app/downloadApp/Testdown.xls";
		jschDown(filePath, templetaddress);
	}
}
