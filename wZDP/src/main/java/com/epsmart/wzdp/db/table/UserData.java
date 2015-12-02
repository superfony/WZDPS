package com.epsmart.wzdp.db.table;

import com.j256.ormlite.field.DatabaseField;

public class UserData {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(index = true)
	private String userName;
	@DatabaseField
	private String taskCity;
	@DatabaseField
	private String Department;
	public UserData() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTaskCity() {
		return taskCity;
	}
	public void setTaskCity(String taskCity) {
		this.taskCity = taskCity;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}
	

}
