package com.epsmart.wzdp.http.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.http.response.model.StatusEntity;

/**
 * @Title: sax 具体解析方式默认实现.
 * @Description: 这里默认实现的是异常值的解析(参考xml文档结构).<br/>
 *               <?xml version="1.0" encoding="utf-8"?> <root> <!—结果标识-->
 *               <result>0</result> <!—错误提示--> <message>这里是错误提示</message>
 *               </root>
 * 
 * @version 1.0
 *DefaultSaxHandler-》 BaseParserHandler-》DefaultHandler
 */
public class DefaultSaxHandler extends BaseParserHandler {
	private StatusEntity statusEntity;
	private StringBuilder builder;

	/**
	 * 遇到文本节点中的字符数据时便会调用此方法
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (null != statusEntity) {
			if (localName.equalsIgnoreCase("result")) {
				statusEntity.result = trim(builder.toString());
			} else if (localName.equalsIgnoreCase("message")) {
				statusEntity.message = trim(builder.toString());
			}
			builder.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		statusEntity = new StatusEntity();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
	}

	private String trim(String strToTrim) {
		if (null == strToTrim) {
			return null;
		}
		return strToTrim.trim();
	}

	@Override
	public Object getParseContent() {
		return statusEntity;
	}
	
	
	

}
