package com.epsmart.wzdp.http.request;

import java.io.Serializable;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * HttpRequest基类,目标:
 * <ul>
 * <li>1:安全有序</li>
 * <li>2:高效</li>
 * <li>3:易用,易控制</li>
 * <li>4:activity停止后停止该activity所用的线程</li>
 * <li>5:监测内存,当内存溢出的时候自动垃圾回收,清理资源,当程序退出之后终止线程池.</li>
 * </ul>
 * @author fony
 * 
 */
public class BaseRequest implements Runnable, Serializable {
	private static final long serialVersionUID = -3995079047509316570L;
	protected int connectTimeout = 20000;
	protected int readTimeout = 20000;
	protected HttpRequestBase request = null;
	protected String uriStr = null;
	protected RequestCallback requestCallback = null;
	protected RequestType requestType = RequestType.WEBSERVICE;
	protected String serviceNameSpace = null;
	protected String serviceUrl = null;

	public enum RequestType {
		HTTP, WEBSERVICE,THRIFT
	}

	public void run() {
	}

	protected HttpResponse buildAndExecuteHttpRequest() throws Exception {
		return null;
	}

	protected HttpUriRequest buildHttpUriRequest() throws Exception {
		return null;
	}

	protected String buildAndExecuteWebServiceRequest() throws Exception {
		return null;
	}

	protected Map<String, String> buildWebServiceRequest() {
		return null;
	}

	protected void responseProcess(HttpResponse httpResponse) throws Exception {
	}

	protected void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	protected void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public void setServiceNameSpace(String serviceNameSpace) {
		this.serviceNameSpace = serviceNameSpace;
	}

}
