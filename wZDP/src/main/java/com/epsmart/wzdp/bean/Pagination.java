package com.epsmart.wzdp.bean;

import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

public class Pagination extends PaginationWidget<WorkOrder> {

	@Override
	public BaseXmlParser getXmlParser(Response response) {
		httpModule.setParseHandler(new WorkOrderHandler());
		return super.getXmlParser(response);
	}

}
