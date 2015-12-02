package com.epsmart.wzdp.activity.supply.fragment.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.bean.DataRow;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

/**
 * @author fony 表单解析类 表单模块后的
 */
public class BasicResponseHandlerNew extends BaseParserHandler {
	private StringBuilder builder;
	private BasicResponseNew response;
	private Field field;
	private DataRow dataRow;
	private BasicEntity entity;

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

		if ("fieldChName".equalsIgnoreCase(localName)) {
			field.fieldChName = trim(builder.toString());
		} else if ("fieldEnName".equalsIgnoreCase(localName)) {
			field.fieldEnName = trim(builder.toString());
		} else if ("fieldContent".equalsIgnoreCase(localName)) {
			field.fieldContent = trim(builder.toString());
		} else if ("fieldView".equalsIgnoreCase(localName)) {
			field.fieldView = trim(builder.toString());
		} else if ("fieldInfo".equalsIgnoreCase(localName)) {
			dataRow.columns.add(field);
			entity.fieldKeys.add(field.fieldEnName);
			entity.fields.put(field.fieldEnName, field);
			field = null;
			entity.rows.add(dataRow);
			dataRow = null;
		} else if ("recordInfo".equalsIgnoreCase(localName)) {
			response.basicEntityList.add(entity);
			entity=null;
			
		} else if ("result".equalsIgnoreCase(localName)) {
			response.result = trim(builder.toString());
		} else if ("message".equalsIgnoreCase(localName)) {
			response.message = trim(builder.toString());
		}
		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
		response = new BasicResponseNew();
		response.basicEntityList = new ArrayList<BasicEntity>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if ("fieldInfo".equalsIgnoreCase(localName)) {
			dataRow = new DataRow();
			dataRow.columns = new ArrayList<Field>();
			field = new Field();
		} else if ("recordInfo".equalsIgnoreCase(localName)) {// 原来只有一层
			   entity=new BasicEntity();
			   entity.rows = new ArrayList<DataRow>();
			    entity.fields = new HashMap<String, Field>();
			    entity.fieldKeys = new ArrayList<String>();
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
		return response;
	}
}
