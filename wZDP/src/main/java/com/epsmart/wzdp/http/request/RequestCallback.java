package com.epsmart.wzdp.http.request;

import com.epsmart.wzdp.http.response.model.Response;


/**
 * 网络请求回调接口
 * 
 * @author fony
 * 
 */
public interface RequestCallback {

	/**
	 * 请求成功
	 * @param response
	 */
	void onSuccess(Response response);

	/**
	 * 请求失败
	 * @param ex
	 *默认为HttpException的实例
	 */
	void onFail(Exception ex);

}
