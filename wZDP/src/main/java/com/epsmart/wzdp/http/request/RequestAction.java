package com.epsmart.wzdp.http.request;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.model.PageBean;

public class RequestAction {
	public RequestType requestType = RequestType.WEBSERVICE;
	/** service服务名 */
	public String serviceName;
	/** 是否启用分页(传递分页参数) */
	
	public boolean isPageBeanEnable = false;
	public PageBean pageBean;
	public Bundle queryBundle;
	public List<String> queryKeys;
	public RequestPram reqPram;

	public RequestPram getReqPram() {
		return reqPram;
	}

	public void setReqPram(RequestPram reqPram) {
		this.reqPram = reqPram;
	}

	public RequestAction() {
		pageBean = new PageBean();
		queryBundle = new Bundle();
		queryKeys = new ArrayList<String>();
	}

	public RequestAction reset() {
		queryKeys.clear();
		queryBundle.clear();
		pageBean.reset();
		return this;
	}

	public void putParam(String key, String value) {
		if (queryBundle.containsKey(key)) {
			queryBundle.putString(key, value);
		} else {
			queryKeys.add(key);
			queryBundle.putString(key, value);
		}
	}
	
	@Override
	public String toString() {
		return "RequestAction [requestType=" + requestType + ", serviceName="
				+ serviceName + ", pageBean=" + pageBean + ", queryBundle="
				+ queryBundle + ", queryKeys=" + queryKeys + "]";
	}
}
