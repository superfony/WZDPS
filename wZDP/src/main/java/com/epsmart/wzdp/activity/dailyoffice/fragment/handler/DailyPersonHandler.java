package com.epsmart.wzdp.activity.dailyoffice.fragment.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

public class DailyPersonHandler extends BaseParserHandler {
	private StringBuilder builder;
	private DailyPerson dperson;
	private DailyPersonPro resp;
	private Person pe;

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
		}  else if ("depid".equalsIgnoreCase(localName)) {
			dperson.depid = trim(builder.toString());
		} else if ("depname".equalsIgnoreCase(localName)) {
			dperson.depname = trim(builder.toString());
		}
//		else if ("persion".equalsIgnoreCase(localName)) {
//		} 
		else if ("persionid".equalsIgnoreCase(localName)) {
			pe.persionid = trim(builder.toString());
		} 
		else if ("persionname".equalsIgnoreCase(localName)) {
			pe.persionname = trim(builder.toString());
		} else if ("persionimg".equalsIgnoreCase(localName)) {
			pe.persionimg = trim(builder.toString());
		}
		else if ("persion".equalsIgnoreCase(localName)) {
			dperson.person.add(pe);
		} 
		else if ("departs".equalsIgnoreCase(localName)) {
			resp.list.add(dperson);
		} 
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		resp = new DailyPersonPro();
		resp.list = new ArrayList<DailyPerson>();
		
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if ("persion".equalsIgnoreCase(localName)) {
			pe = new Person();
		}
		if ("departs".equalsIgnoreCase(localName)) {
			dperson = new DailyPerson();
			dperson.person = new ArrayList<Person>();
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
