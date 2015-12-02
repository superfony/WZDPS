package com.epsmart.wzdp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.epsmart.wzdp.http.request.model.PageBean;
import com.epsmart.wzdp.http.response.model.PagerResponse;

public class WorkOrderResponse extends PagerResponse implements Parcelable {

	public static final Parcelable.Creator<WorkOrderResponse> CREATOR = new Creator<WorkOrderResponse>() {
		public WorkOrderResponse createFromParcel(Parcel source) {
			WorkOrderResponse entity = new WorkOrderResponse();

			entity.result = source.readString();
			entity.message = source.readString();
			entity.pageBean = source.readParcelable(PageBean.class
					.getClassLoader());
			return entity;
		}

		public WorkOrderResponse[] newArray(int size) {
			return new WorkOrderResponse[size];
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
		parcel.writeParcelable(pageBean,
				Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
	}

	@Override
	public String toString() {
		return "WorkOrderResponse [pageBean=" + pageBean + "]";
	}
}
