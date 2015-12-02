package com.epsmart.wzdp.http.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.epsmart.wzdp.http.request.model.PageBean;

public class PagerResponse extends StatusEntity implements Parcelable {
	public PageBean pageBean;

	public static final Parcelable.Creator<PagerResponse> CREATOR = new Creator<PagerResponse>() {
		public PagerResponse createFromParcel(Parcel source) {
			PagerResponse entity = new PagerResponse();

			entity.result = source.readString();
			entity.message = source.readString();
			entity.pageBean = source.readParcelable(PageBean.class
					.getClassLoader());
			return entity;
		}

		public PagerResponse[] newArray(int size) {
			return new PagerResponse[size];
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
		return "WorkOrderResponse [pageBean=]";
	}
}
