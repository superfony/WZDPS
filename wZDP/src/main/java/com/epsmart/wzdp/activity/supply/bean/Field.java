package com.epsmart.wzdp.activity.supply.bean;

import java.io.Serializable;

/**
 * @author fony
 */
public class Field implements Serializable {
	public String fieldChName;
	public String fieldEnName;
	public String fieldView;
	public String fieldContent="";

	public void reset() {
		fieldChName = "";
		fieldEnName = "";
		fieldView = "";
		fieldContent = "";
	}

	public Field() {
	}

	@Override
	public String toString() {
		return "Field [fieldChName=" + fieldChName + ", fieldEnName="
				+ fieldEnName + ", fieldView=" + fieldView + ", fieldContent="
				+ fieldContent + "]";
	}
}
