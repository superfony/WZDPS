package com.epsmart.wzdp.activity.supply.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author fony
 *
 */
public class BasicResponse implements Parcelable {
	public String result;
	public String message;
	public BasicEntity entity;

	public static final Parcelable.Creator<BasicResponse> CREATOR = new Creator<BasicResponse>() {
		public BasicResponse createFromParcel(Parcel source) {
			BasicResponse field = new BasicResponse();
			field.result = source.readString();
			field.message = source.readString();
			field.entity = source.readParcelable(BasicEntity.class
					.getClassLoader());
			return field;
		}

		public BasicResponse[] newArray(int size) {
			return new BasicResponse[size];
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
		parcel.writeParcelable(entity, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
	}

	@Override
	public String toString() {
		return "BasicResponse [result=" + result + ", message=" + message
				+ ", entity=" + entity + "]";
	}
}
