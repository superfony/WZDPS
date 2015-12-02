package com.epsmart.wzdp.http.xml.parser;

/**
 * xml解析基类
 * @Description: 1.子类需提供parse()实现,以提供具体的解析方式.
 *               2.setContent(),getContent()具体的设置和获取解析完数据值.
 * 
 * 
 */
public interface BaseXmlParser {

	/**
	 * 解析方法.
	 */
	void parse();

	/**
	 * 
	 * 返回parse()解析完成的object,客户端可按需求转型成需要的类型.
	 * 
	 * @return
	 */
	Object getContent();

	/**
	 * 
	 * 设置解析完成的数据.
	 * 
	 * @param content
	 */
	void setContent(Object content);

	/**
	 * 
	 * 待解析的xml数据.
	 * 
	 * @param xml
	 */
	void setXml(String xml);

	String getXml();

}
