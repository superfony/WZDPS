package com.epsmart.wzdp.activity.supply.bean;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class BasicEntity implements Parcelable {
	public ArrayList<String> fieldKeys;
	public HashMap<String, Field> fields;
	public ArrayList<DataRow> rows;

	public static final Parcelable.Creator<BasicEntity> CREATOR = new Creator<BasicEntity>() {
		@SuppressWarnings("unchecked")
		public BasicEntity createFromParcel(Parcel source) {
			BasicEntity field = new BasicEntity();
			field.fieldKeys = source.readArrayList(String.class
					.getClassLoader());
			field.fields = source.readHashMap(HashMap.class.getClassLoader());
			field.rows = source.readArrayList(DataRow.class.getClassLoader());
			return field;
		}

		public BasicEntity[] newArray(int size) {
			return new BasicEntity[size];
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
		parcel.writeList(rows);
	}
}
