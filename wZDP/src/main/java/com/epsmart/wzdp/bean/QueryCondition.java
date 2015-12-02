package com.epsmart.wzdp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询条件实体类
 * @author fony
 *
 */
public class QueryCondition implements Parcelable {
	public String formNo;
	public String title;
	public String startTime;
	public String endTime;
	public String countryName;
	public String taskState;
	public String personId;

	public static final Parcelable.Creator<QueryCondition> CREATOR = new Creator<QueryCondition>() {
		public QueryCondition createFromParcel(Parcel source) {
			QueryCondition field = new QueryCondition();
			field.formNo = source.readString();
			field.title = source.readString();
			field.startTime = source.readString();
			field.endTime = source.readString();
			field.countryName = source.readString();
			field.taskState = source.readString();
			field.personId = source.readString();
			return field;
		}

		public QueryCondition[] newArray(int size) {
			return new QueryCondition[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(formNo);
		parcel.writeString(title);
		parcel.writeString(startTime);
		parcel.writeString(endTime);
		parcel.writeString(countryName);
		parcel.writeString(taskState);
		parcel.writeString(personId);
	}
}
