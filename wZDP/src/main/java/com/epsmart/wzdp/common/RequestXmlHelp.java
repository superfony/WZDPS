package com.epsmart.wzdp.common;

public class RequestXmlHelp {

	public RequestXmlHelp() {

	}

	public static StringBuffer getReqXML(String key,String value) {
		StringBuffer reqXML = new StringBuffer();
		
		reqXML.append("<param>").append("<key>").append(key).append("</key>")
				.append("<value>").append(value).append("</value>").append("</param>");
		return reqXML;
	}

	public static String getCommonXML(StringBuffer requestXML) {
		return "<opdetail>"
				+ requestXML.toString() + "</opdetail>";
	}
}
