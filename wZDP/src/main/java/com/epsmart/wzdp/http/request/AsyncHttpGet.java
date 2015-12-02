package com.epsmart.wzdp.http.request;


import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.epsmart.wzdp.http.request.model.RequestParameter;

/**
 * @Title 异步Http Get请求.
 * 
 * @Descriptoin 线程的终止工作交给线程池,当activity停止的时候,设置回调函数为false,就不会执行回调方法.
 * 
 */
public class AsyncHttpGet extends AsyncHttpRequest {
	private static final long serialVersionUID = 8931099024759099406L;

	public AsyncHttpGet(String url, List<RequestParameter> parameter,
			RequestCallback requestCallback) {
		super(url, parameter, requestCallback);
	}

	protected HttpRequestBase buildHttpUriRequest() throws Exception {
		if (null != parameters && parameters.size() > 0) {
			uriStr += "&" + encodeParameters();
		}
		request = new HttpGet(uriStr);
		return request;
	}

	@Override
	public String encodeParameters() {
		if (null == parameters || parameters.size() <= 0)
			return null;
		StringBuffer buf = new StringBuffer();
		RequestParameter rp = null;
		int paramLen = parameters.size();
		for (int j = 0; j < paramLen; j++) {
			rp = (RequestParameter) parameters.get(j);
			if (j != 0) {
				buf.append("&");
			}
			if (rp.isNeedUrlencode()) {
				try {
//					buf.append(ParamUtil.encode(rp.getName())).append("=")
//							.append(ParamUtil.encode(rp.getValue()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				buf.append(rp.getName()).append("=").append(rp.getValue());
			}
		}
		return buf.toString();
	}
}
