package com.epsmart.wzdp.activity.supply.bean;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fony
 * 新表单处理 原来现实的表单进行模块化现实
 */
public class BasicResponseNew implements Parcelable {
	public String result;
	public String message;
	public  ArrayList<BasicEntity> basicEntityList;

	public static final Parcelable.Creator<BasicResponseNew> CREATOR = new Creator<BasicResponseNew>() {
		@SuppressWarnings("unchecked")
		public BasicResponseNew createFromParcel(Parcel source) {
			BasicResponseNew field = new BasicResponseNew();
			field.result = source.readString();
			field.message = source.readString();
			field.basicEntityList = source.readArrayList(BasicEntity.class
					.getClassLoader());
			return field;
		}

		public BasicResponseNew[] newArray(int size) {
			return new BasicResponseNew[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(result);
		parcel.writeString(message);
		parcel.writeList(basicEntityList);
	}

	@Override
	public String toString() {
		return "BasicResponse [result=" + result + ", message=" + message
				+ ", basicEntityList=" + basicEntityList + "]";
	}
}
