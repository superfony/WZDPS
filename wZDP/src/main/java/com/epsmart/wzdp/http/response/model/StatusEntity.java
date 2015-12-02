package com.epsmart.wzdp.http.response.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 成功、失败解析后的实体
 * 
 * @author fony
 * 
 */
public class StatusEntity implements Parcelable {
	public String result;
	public String message;

	public static final Parcelable.Creator<StatusEntity> CREATOR = new Creator<StatusEntity>() {
		public StatusEntity createFromParcel(Parcel source) {
			StatusEntity entity = new StatusEntity();

			entity.result = source.readString();
			entity.message = source.readString();
			return entity;
		}

		public StatusEntity[] newArray(int size) {
			return new StatusEntity[size];
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
	}

	public StatusEntity() {
	}

	public StatusEntity(String result, String message) {
		super();
		this.result = result;
		this.message = message;
	}

	@Override
	public String toString() {
		return "StatusEntity [result=" + result + ", message=" + message + "]";
	}
}