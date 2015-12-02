package com.epsmart.wzdp.activity.dailyoffice.fragment.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

public class DailyReplyHandler extends BaseParserHandler {
	private StringBuilder builder;
	private DailyReply bp;
	private DailyReplyPro resp;

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
		if ("result".equalsIgnoreCase(localName)) {
			resp.result = trim(builder.toString());
		} else if ("message".equalsIgnoreCase(localName)) {
			resp.message = trim(builder.toString());
		}  else if ("repid".equalsIgnoreCase(localName)) {
			bp.repid = trim(builder.toString());
		} else if ("reppersionid".equalsIgnoreCase(localName)) {
			bp.reppersionid = trim(builder.toString());
		}  else if ("reppersionname".equalsIgnoreCase(localName)) {
			bp.reppersionname = trim(builder.toString());
		} else if ("reppersionimg".equalsIgnoreCase(localName)) {
			bp.reppersionimg = trim(builder.toString());
		} else if ("repcontent".equalsIgnoreCase(localName)) {
			bp.repcontent = trim(builder.toString());
		}else if("reptime".equalsIgnoreCase(localName)){
			bp.reptime = trim(builder.toString());
		} else if ("replay".equalsIgnoreCase(localName)) {
			resp.list.add(bp);
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		resp = new DailyReplyPro();
		resp.list = new ArrayList<DailyReply>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if ("replay".equalsIgnoreCase(localName)) {
			bp = new DailyReply();
		}
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
