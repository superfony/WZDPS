package com.epsmart.wzdp.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.http.request.model.PageBean;
import com.epsmart.wzdp.http.xml.handler.BaseParserHandler;

/**
 * 
 * @author fony
 *
 */
public class WorkOrderHandler extends BaseParserHandler {
	private StringBuilder builder;
	private WorkOrderResponse response;
	private List<WorkOrder> workOrders;
	private WorkOrder workOrder;
	private Field field;

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
			if ("totleNum".equals(field.fieldEnName)) {
				try {
					response.pageBean.setAllCount(Integer
							.valueOf(field.fieldContent));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			workOrder.fieldKeys.add(field.fieldEnName);
			workOrder.fields.put(field.fieldEnName, field);
			field = null;
		} else if ("recordInfo".equalsIgnoreCase(localName)) {
			Field field2 = workOrder.fields.get("totleNum");
			if (null == field2) {
				workOrders.add(workOrder);
			}
			workOrder = null;
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
		response = new WorkOrderResponse();
		response.pageBean = new PageBean();
		workOrders = new ArrayList<WorkOrder>();
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
		response.pageBean.setPageDatas(workOrders);
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
