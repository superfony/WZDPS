package com.epsmart.wzdp.bean;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

/**
 * 
 * @author fony
 * 系统权限解析类
 *
 */
public class PermissionHandler extends BaseParserHandler {
	private StringBuilder builder;
	private PermissionResponse response;
	public WorkOrder workOrder;
	private Field field;
	private String flag;

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
			field.fieldContent = trim(builder.toString());//StringUtils.ToDBC(trim(builder.toString()));
		} else if ("fieldView".equalsIgnoreCase(localName)) {
			field.fieldView = trim(builder.toString());
		} else if ("fieldInfo".equalsIgnoreCase(localName)) {
			workOrder.fieldKeys.add(field.fieldEnName);
			workOrder.fields.put(field.fieldEnName, field);
			if(field.fieldEnName.equalsIgnoreCase("fcode")){
				flag=field.fieldContent;
			}
			field = null;
		} else if ("recordInfo".equalsIgnoreCase(localName)) {
			response.workOrders.put(flag,workOrder);
			workOrder = null;
			flag=null;
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
		response = new PermissionResponse();
		response.workOrders = new HashMap<String, WorkOrder>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if ("fieldInfo".equalsIgnoreCase(localName)) {
			field = new Field();
		} else if ("recordInfo".equalsIgnoreCase(localName)) {
			workOrder = new WorkOrder();
			workOrder.fields = new HashMap<String, Field>();
			workOrder.fieldKeys = new ArrayList<String>();
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
