package com.epsmart.wzdp.activity.dailyoffice.fragment.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

public class DailyNoticeNumHandler extends BaseParserHandler {
	private StringBuilder builder;
	private DailyNoticeNum resp;

	/**
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
		if ("requestcode".equalsIgnoreCase(localName)) {
			resp.requestcode = trim(builder.toString());
		} else if ("message".equalsIgnoreCase(localName)) {
			resp.message = trim(builder.toString());
		}  else if ("noreadnum".equalsIgnoreCase(localName)) {
			resp.noreadnum = trim(builder.toString());
		} else if ("content".equalsIgnoreCase(localName)) {
			resp.content = trim(builder.toString());
		}else if ("alarm".equalsIgnoreCase(localName)) {
			resp.alarm = trim(builder.toString());
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		resp = new DailyNoticeNum();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	private String trim(String strToTrim) {
		if (null == strToTrim) {
			return null;
		}
		return strToTrim.trim();
	}

	@Override
	public Object getParseContent() {
		return resp;
	}

}
