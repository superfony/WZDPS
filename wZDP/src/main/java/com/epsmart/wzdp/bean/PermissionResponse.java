package com.epsmart.wzdp.bean;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class PermissionResponse implements Parcelable {
	public String result;
	public String message;
	public  Map<String,WorkOrder> workOrders;
	

	public static final Parcelable.Creator<PermissionResponse> CREATOR = new Creator<PermissionResponse>() {
		@SuppressWarnings("unchecked")
		public PermissionResponse createFromParcel(Parcel source) {
			PermissionResponse entity = new PermissionResponse();

			entity.result = source.readString();
			entity.message = source.readString();
			entity.workOrders = source.readHashMap(WorkOrder.class
					.getClassLoader());
			return entity;
		}

		public PermissionResponse[] newArray(int size) {
			return new PermissionResponse[size];
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
		parcel.writeMap(workOrders);
	}

	@Override
	public String toString() {
		return "WorkOrderResponse [workOrders=" + workOrders + "]";
	}
}
