package com.epsmart.wzdp.http.xml.parser.sax;

import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;
import com.epsmart.wzdp.http.xml.parser.DefaultXmlParser;

/**
 * @Title: 默认的xml解析器实现.
 * 
 * @version 1.0
 * 
 */
public abstract class SaxXmlParser extends DefaultXmlParser {
	// sax xml解析器
	private BaseParserHandler baseHandler;

	public BaseParserHandler getHandler() {
		return baseHandler;
	}
	
	public void setHandler(BaseParserHandler handler) {
		this.baseHandler = handler;
	}

	@Override
	public Object getContent() {
		return getHandler().getParseContent();
	}

}
