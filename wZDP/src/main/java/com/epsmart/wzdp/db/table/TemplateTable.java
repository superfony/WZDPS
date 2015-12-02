package com.epsmart.wzdp.db.table;


import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 * 各类表格模板类
 * @author fony
 */
public class TemplateTable extends TableImpi{
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField(index = true)
	public String methodName;
	@DatabaseField
	public String userName;
	@DatabaseField
	public String param;
	@DatabaseField
	public String value;
	@DatabaseField
	public Date date;
	@DatabaseField
	public String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getParam() {
		return param;
	}


	public void setParam(String param) {
		this.param = param;
	}



	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	

	
     public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public	TemplateTable() {
		
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
//		sb.append(", ").append("string=").append(string);
//		sb.append(", ").append("millis=").append(millis);
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
//		sb.append(", ").append("date=").append(dateFormatter.format(date));
//		sb.append(", ").append("even=").append(even);
		return sb.toString();
	}
}
