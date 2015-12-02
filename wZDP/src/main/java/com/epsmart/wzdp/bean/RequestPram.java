package com.epsmart.wzdp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询条件实体类
 * 
 * @author fony
 * 
 */
public class RequestPram implements Parcelable {
	public String methodName;
	public String param;
	public int bizId;
	public int pluginId;
	public int userName;
	//当前页
	public int pageNo;
	// 当前页大小
	public int pageSize;
	public String password;
	public String user_type;
	public static final Parcelable.Creator<RequestPram> CREATOR = new Creator<RequestPram>() {
		public RequestPram createFromParcel(Parcel source) {
			RequestPram field = new RequestPram();
			field.methodName = source.readString();
			field.param = source.readString();
			field.bizId=source.readInt();
			field.pluginId=source.readInt();
			field.userName=source.readInt();
			field.pageNo=source.readInt();
			field.pageSize=source.readInt();
			field.password=source.readString();
			field.user_type=source.readString();
			return field;
		}

		public RequestPram[] newArray(int size) {
			return new RequestPram[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(methodName);
		parcel.writeString(param);
		parcel.writeInt(bizId);
		parcel.writeInt(pluginId);
		parcel.writeInt(userName);
		parcel.writeInt(pageNo);
		parcel.writeInt(pageSize);
		parcel.writeString(password);
		parcel.writeString(user_type);

	}

	@Override
	public String toString() {
		return "[methodName=" + methodName + "  ,param=" + param+"bizID="+bizId+", pluginId="+pluginId+", userName="+userName+",pageNo="+pageNo+",pageSize="+pageSize+",user_type="+user_type+"]";
	}

}
