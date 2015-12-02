package com.epsmart.wzdp.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.epsmart.wzdp.activity.supply.bean.Field;


public class WorkOrder implements Parcelable {


	public ArrayList<String> fieldKeys;
	public HashMap<String, Field> fields;

	public static final Parcelable.Creator<WorkOrder> CREATOR = new Creator<WorkOrder>() {
		@SuppressWarnings("unchecked")
		public WorkOrder createFromParcel(Parcel source) {
			WorkOrder field = new WorkOrder();
			field.fieldKeys = source.readArrayList(String.class
					.getClassLoader());
			field.fields = source.readHashMap(HashMap.class.getClassLoader());
			return field;
		}

		public WorkOrder[] newArray(int size) {
			return new WorkOrder[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeList(fieldKeys);
		parcel.writeMap(fields);
	}

	@Override
	public String toString() {
		return "WorkOrder [fieldKeys=" + fieldKeys + ", fields=" + fields + "]";
	}
}
