package com.epsmart.wzdp.http;

public interface ModuleResponseProcessor {
	/**
	 * 解析完成后的数据处理
	 * @param httpModule
	 * @param parseObj
     * 解析完成后的对象
	 */
	void processResponse(BaseHttpModule httpModule, Object parseObj);
}
