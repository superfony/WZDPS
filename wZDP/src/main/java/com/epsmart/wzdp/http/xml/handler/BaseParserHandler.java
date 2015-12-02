package com.epsmart.wzdp.http.xml.handler;

import org.xml.sax.helpers.DefaultHandler;

public abstract class BaseParserHandler extends DefaultHandler {

	public abstract Object getParseContent();

}
