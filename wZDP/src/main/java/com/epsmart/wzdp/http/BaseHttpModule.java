package com.epsmart.wzdp.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.Context;
import android.util.Log;

import com.epsmart.wzdp.common.CommonRegex;
import com.epsmart.wzdp.http.request.AsyncHttpPost;
import com.epsmart.wzdp.http.request.BaseRequest;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.request.RequestCallback;
import com.epsmart.wzdp.http.request.model.RequestParameter;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.xml.XmlParseModule;
import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;
import com.epsmart.wzdp.http.xml.parser.sax.DefaultSaxXmlParser;

/**
 * 基于继承Activity 类的webservice调用及xml解析方式
 * @author fony
 */
public class BaseHttpModule {
	private Context mContext;

	/** 当前所持有的所有请求 */
	private List<BaseRequest> requestList = null;
	private RequestCallback requestCallback;
	private BaseParserHandler parseHandler;
	private ModuleResponseProcessor responseProcess;
	private String serviceNameSpace;
	private String serviceUrl;
	private String TAG=BaseHttpModule.class.getName();
	public BaseHttpModule() {

	}

	public BaseHttpModule(Context mContext) {
		this.mContext = mContext;
	}

	public BaseHttpModule(Context mContext, String serviceNameSpace,
			String serviceUrl) {
		this.mContext = mContext;
		this.serviceNameSpace = serviceNameSpace;
		this.serviceUrl = serviceUrl;
	}

	public void init() {
		requestList = new ArrayList<BaseRequest>();
	}

	public void onPause() {
		cancelRequest();
	}

	public void onDestroy() {
		cancelRequest();
	}

	/**
	 * 在activity销毁的时候同时设置停止请求,停止线程请求回调.
	 */
	private void cancelRequest() {
		if (null == requestList || requestList.size() <= 0)
			return;
		int len = requestList.size();
		HttpUriRequest httpUriRequest = null;
		for (int i = 0; i < len; i++) {
			httpUriRequest = requestList.get(i).getRequest();
			if (null == httpUriRequest)
				continue;
			try {
				httpUriRequest.abort();
				HttpUriRequest huriReq=requestList.get(i).getRequest();
				//requestList.remove(huriReq);// findbug 注释部分
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param requestAction
	 *            请求参数设置
	 * @param baseHandler
	 *            解析器
	 * @param responseProcess
	 *            回调对象
	 * @param requestType
	 *            请求类型
	 */
	public void executeRequest(RequestAction requestAction,
			BaseParserHandler baseHandler,
			ModuleResponseProcessor responseProcess, RequestType requestType) {
		this.parseHandler = baseHandler;
		this.responseProcess = responseProcess;
		requestCallback = new DefaultCallback();
		AsyncHttpPost httpost = new AsyncHttpPost(requestAction,
				requestCallback);
		httpost.setContext(mContext);
		httpost.setRequestType(requestType);
		httpost.setServiceNameSpace(serviceNameSpace);
		httpost.setServiceUrl(serviceUrl);
		new Thread(httpost).start();
	}

	public void executeRequest(List<RequestParameter> parameterList,
			BaseParserHandler baseHandler,
			ModuleResponseProcessor responseProcess, RequestType requestType) {
		this.parseHandler = baseHandler;
		this.responseProcess = responseProcess;
		requestCallback = new DefaultCallback();
		AsyncHttpPost httpost = new AsyncHttpPost(null, parameterList,
				requestCallback);
		httpost.setRequestType(requestType);
		httpost.setServiceNameSpace(serviceNameSpace);
		httpost.setServiceUrl(serviceUrl);
		getRequestList().add(httpost);
		// DefaultThreadPool.getInstance().execute(httpost);
	}

	public interface RequestListener {
		void onSuccess(Response response);

		void onFail(Exception e);
	}

	// 监听回调对象接口
	private RequestListener requestListener;

	private class DefaultCallback implements RequestCallback {
		@Override
		public void onSuccess(Response response) {
			if (null != requestListener) {
				requestListener.onSuccess(response);
				return;
			}
			if (null == xmlParserProvider) {
				processResponse(BaseHttpModule.this, response,
						getBaseXmlParser(response, parseHandler),
						responseProcess);
			} else {
				processResponse(BaseHttpModule.this, response,
						xmlParserProvider.createXmlParser(response),
						responseProcess);
			}
		}

		@Override
		public void onFail(Exception e) {
			e.printStackTrace();
			Log.w("BaseHttpModule", "requestListener="+requestListener);
			if (null != requestListener)
				requestListener.onFail(e);
		}

	}

	/**
	 * 生成自定义的xmlparser
	 */
	public interface XmlParserProvider {
		BaseXmlParser createXmlParser(Response response);
	}

	private XmlParserProvider xmlParserProvider;

	/**
	 * 开始解析调用
	 * 
	 * @param httpModule
	 *            本身类的一个实列
	 * @param response
	 *            成功请求的xml数据
	 * @param baseXmlParser
	 *            解析器 是自己定义的解析器也可以是默认实现的解析器 不用的解析器返回的对象是不一样的
	 * @param callBack
	 *            回调接口对象
	 */
	public void processResponse(BaseHttpModule httpModule, Response response,
			BaseXmlParser baseXmlParser, ModuleResponseProcessor callBack) {

		XmlParseModule xmlParseModule = new XmlParseModule(response);
		xmlParseModule.setBaseXmlParser(baseXmlParser);
		// 开始解析

		xmlParseModule.parseXml();
		if (null != callBack) {
			callBack.processResponse(httpModule, xmlParseModule
					.getBaseXmlParser().getContent());
		}
	}

	// XmlParseModule-》BaseXmlParser
	// DefaultSaxXmlParser-》SaxXmlParser-》DefaultXmlParser-》BaseXmlParser
	// DefaultSaxHandler-》 BaseParserHandler-》DefaultHandler
	/* 返回解析器管理类工具 */
	public BaseXmlParser getBaseXmlParser(Response response,
			BaseParserHandler baseHandler) {
		String ras = null;
		try {
			ras = response.asString();
		} catch (HttpException e) {
			e.printStackTrace();
		}
		DefaultSaxXmlParser saxXmlParser = new DefaultSaxXmlParser();

		/*
		 * 先提取xml里面的message信息 如果为1 说明返回错误信息 或无结果返回 如果为0 说明有值返回 接下来进行解析
		 */
		String result = CommonRegex.getMiddleValue("result", ras);
		Log.i("BaseHttpModule", "baseHandler="+baseHandler);
		if (null != result) {
			if (null == baseHandler || result.equals("1")) {
				saxXmlParser.setHandler(new DefaultSaxHandler());// 默认的解析器
			} else {
				saxXmlParser.setHandler(baseHandler);// 自己重写的解析器用来处理每次具体请求
			}
		} else {
			saxXmlParser.setHandler(new DefaultSaxHandler());
		}
		return saxXmlParser;
	}

	public List<BaseRequest> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<BaseRequest> requestList) {
		this.requestList = requestList;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public void setRequestListener(RequestListener requestListener) {
		this.requestListener = requestListener;
	}

	public XmlParserProvider getXmlParserProvider() {
		return xmlParserProvider;
	}

	public void setXmlParserProvider(XmlParserProvider xmlParserProvider) {
		this.xmlParserProvider = xmlParserProvider;
	}

	public RequestCallback getRequestCallback() {
		return requestCallback;
	}

	public void setRequestCallback(RequestCallback requestCallback) {
		this.requestCallback = requestCallback;
	}

	public ModuleResponseProcessor getResponseProcess() {
		return responseProcess;
	}

	public BaseParserHandler getParseHandler() {
		return parseHandler;
	}

	public void setServiceNameSpace(String serviceNameSpace) {
		this.serviceNameSpace = serviceNameSpace;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void setParseHandler(BaseParserHandler parseHandler) {
		this.parseHandler = parseHandler;
	}
}
