package com.epsmart.wzdp.activity.supply.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class DataRow implements Parcelable {
	public ArrayList<Field> columns;

	public static final Parcelable.Creator<DataRow> CREATOR = new Creator<DataRow>() {
		@SuppressWarnings("unchecked")
		public DataRow createFromParcel(Parcel source) {
			DataRow field = new DataRow();
			field.columns = source.readArrayList(Field.class.getClassLoader());
			return field;
		}

		public DataRow[] newArray(int size) {
			return new DataRow[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeList(columns);
	}
}
