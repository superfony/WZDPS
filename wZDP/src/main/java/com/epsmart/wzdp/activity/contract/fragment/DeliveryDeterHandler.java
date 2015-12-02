package com.epsmart.wzdp.activity.contract.fragment;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReply;
import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

public class DeliveryDeterHandler extends BaseParserHandler {
	private StringBuilder builder;
	private DeliveryDeter dd;
	private DeliveryDeterPro resp;

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
		}  else if ("ganth".equalsIgnoreCase(localName)) {
			resp.ganth = trim(builder.toString());
		} else if ("sydw".equalsIgnoreCase(localName)) {
			resp.sydw = trim(builder.toString());
		}  else if ("vendor".equalsIgnoreCase(localName)) {
			resp.vendor = trim(builder.toString());
		} else if ("tax".equalsIgnoreCase(localName)) {
			resp.tax = trim(builder.toString());
		} else if ("zhongl".equalsIgnoreCase(localName)) {
			resp.zhongl = trim(builder.toString());
		} else if("jhzczl".equalsIgnoreCase(localName)){
			resp.jhzczl = trim(builder.toString());
		} else if ("jhhh".equalsIgnoreCase(localName)) {
			dd.jhhh = trim(builder.toString());
		} else if ("jhfhsj".equalsIgnoreCase(localName)) {
			dd.jhfhsj= trim(builder.toString());
		} else if ("jhdhsj".equalsIgnoreCase(localName)) {
			dd.jhdhsj = trim(builder.toString());
		} else if ("jhzcsl".equalsIgnoreCase(localName)) {
			dd.jhzcsl = trim(builder.toString());
		} else if ("state".equalsIgnoreCase(localName)) {
			dd.state = trim(builder.toString());
		} else if ("deter".equalsIgnoreCase(localName)) {
			resp.list.add(dd);
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		resp = new DeliveryDeterPro();
		resp.list = new ArrayList<DeliveryDeter>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if ("deter".equalsIgnoreCase(localName)) {
			dd = new DeliveryDeter();
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
