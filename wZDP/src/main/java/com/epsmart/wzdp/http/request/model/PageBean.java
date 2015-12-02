package com.epsmart.wzdp.http.request.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 * @Title: 数据分页实体类.
 * @version 1.0
 * 
 */
public class PageBean implements Parcelable {
	/**
	 * 总记录数
	 */
	private int allCount;
	/**
	 * 当前页,默认第一页
	 */
	private int currentPage = 1;

	/**
	 * 总页数,默认共一页
	 */
	private int pageCount = 1;

	/**
	 * 每页记录数,默认15条
	 */
	private int pageSize = 15;

	/**
	 * 当前页的查询条件
	 */
	private Bundle queryBundle;
	private List<String> queryKeys;

	private Object pageDatas;

	public PageBean() {
		queryBundle = new Bundle();
		queryKeys = new ArrayList<String>();
		init();
	}

	private void init() {
		queryKeys.add("startPage");
		queryKeys.add("pageNum");
		queryBundle.putInt("startPage", currentPage);
		queryBundle.putInt("pageNum", pageSize);
	}

	public void reset() {
		queryKeys.clear();
		queryBundle.clear();
		currentPage=1;
		pageCount=1;
		pageDatas = null;
		init();
	}

	public static final Parcelable.Creator<PageBean> CREATOR = new Creator<PageBean>() {
		public PageBean createFromParcel(Parcel source) {
			PageBean mPageBean = new PageBean();

			mPageBean.allCount = source.readInt();
			mPageBean.currentPage = source.readInt();
			mPageBean.pageCount = source.readInt();
			mPageBean.pageSize = source.readInt();
			mPageBean.queryBundle = source.readBundle();
			List<String> list = new ArrayList<String>();
			source.readStringList(list);
			mPageBean.queryKeys = list;
			return mPageBean;
		}

		public PageBean[] newArray(int size) {
			return new PageBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(allCount);
		parcel.writeInt(currentPage);
		parcel.writeInt(pageCount);
		parcel.writeInt(pageSize);
		parcel.writeBundle(queryBundle);
		parcel.writeStringList(queryKeys);
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
		pageCount = (int) Math.ceil((float) allCount / pageSize);
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		Log.i("", "..currentPage...>>"+currentPage);
		Log.i("", "..pageCount...>>"+pageCount);
		Log.i("", "..allCount...>>"+allCount);
		
		if (currentPage > pageCount) {//
			this.currentPage = allCount > 0 ? pageCount : 1;
		} else if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
		queryBundle.putInt("startPage", currentPage);
	}

	public int getPageCount() {
		pageCount = (int) Math.ceil((float) allCount / pageSize);
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		if (pageCount < 1) {
			this.pageCount = 1;
		} else {
			this.pageCount = pageCount;
		}
		if (allCount > 0 && this.pageCount < currentPage) {
			currentPage = this.pageCount;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		pageCount = (int) Math.ceil((float) allCount / pageSize);
		if (allCount > 0 && currentPage > pageCount) {
			currentPage = pageCount;
		}
		queryBundle.putInt("pageNum", pageSize);
	}

	public Bundle getQueryBundle() {
		return queryBundle;
	}

	public void setQueryBundle(Bundle queryBundle) {
		this.queryBundle = queryBundle;
	}

	public Object getPageDatas() {
		return pageDatas;
	}

	public void setPageDatas(Object pageDatas) {
		this.pageDatas = pageDatas;
	}

	public List<String> getQueryKeys() {
		return queryKeys;
	}

	@Override
	public String toString() {
		return "PageBean [allCount=" + allCount + ", currentPage="
				+ currentPage + ", pageCount=" + pageCount + ", pageSize="
				+ pageSize + ", queryBundle=" + queryBundle + ", queryKeys="
				+ queryKeys + ", pageDatas=" + pageDatas + "]";
	}

}
