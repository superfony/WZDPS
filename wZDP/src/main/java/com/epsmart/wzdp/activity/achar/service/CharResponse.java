package com.epsmart.wzdp.activity.achar.service;

import java.util.ArrayList;

import com.epsmart.wzdp.activity.supply.bean.BasicEntity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author fony
 *
 */
public class CharResponse implements Parcelable {
	public String result;
	public String message;
	public ArrayList<BasicEntity> entityList;

	public static final Parcelable.Creator<CharResponse> CREATOR = new Creator<CharResponse>() {
		@SuppressWarnings("unchecked")
		public CharResponse createFromParcel(Parcel source) {
			CharResponse field = new CharResponse();
			field.result = source.readString();
			field.message = source.readString();
			field.entityList = source.readArrayList(BasicEntity.class
					.getClassLoader());
			return field;
		}

		public CharResponse[] newArray(int size) {
			return new CharResponse[size];
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
		parcel.writeList(entityList);
		}

	@Override
	public String toString() {
		return "BasicResponse [result=" + result + ", message=" + message
				+ ", entity=" + entityList + "]";
	}
}
