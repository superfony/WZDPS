package com.epsmart.wzdp.updata;

import java.io.Serializable;
/**
 * 接口URL实体类
 */
public class URLs implements Serializable {
	public final static String HOST ="127.0.0.1";
	public final static String HTTP = "http://";
	public final static String PORT = ":28080/wzdp/wzdp/app/downloadApp";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HTTP + HOST+PORT + URL_SPLITTER;
	// 获取APK版本信息
	public final static String UPDATE_VERSION = URL_API_HOST+"update.xml";
}
