package com.epsmart.wzdp.http.request;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.epsmart.wzdp.http.request.model.RequestParameter;

/**
 * @Title 异步Http Post请求
 * @Descriptoin 线程的终止工作交给线程池,当activity停止的时候,设置回调函数为false,就不会执行回调方法
 * @author fony
 */
public class AsyncHttpPost extends AsyncHttpRequest {
	private static final long serialVersionUID = 5139886571863034394L;

	public AsyncHttpPost(RequestAction requestAction,
			RequestCallback requestCallback) {
		super(requestAction, requestCallback);
	}

	public AsyncHttpPost(String url, List<RequestParameter> parameter,
			RequestCallback requestCallback) {
		super(url, parameter, requestCallback);
	}

	protected HttpRequestBase buildHttpUriRequest() throws Exception {
		try {
			request = new HttpPost(uriStr);
			request.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
			request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					readTimeout);
			if (null != parameters && parameters.size() > 0) {
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				int len = parameters.size();
				RequestParameter requestParameter = null;
				for (int i = 0; i < len; i++) {
					requestParameter = (RequestParameter) parameters.get(i);
					list.add(new BasicNameValuePair(requestParameter.getName(),
							requestParameter.getValue()));
				}
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(list,
						HTTP.UTF_8));
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return request;
	}
}
