package com.epsmart.wzdp.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.epsmart.wzdp.bean.ViewCreator;

/**
 * 实现HolderView模式的Adapter
 */
public class HolderAdapter<T> extends AbstractAdapter<T> {

	/**
	 * 创建对象
	 * 
	 * @param inflater
	 * @param creator
	 */
	public HolderAdapter(Context context, ViewCreator<T> creator) {
		super(context, creator);
	}

	public HolderAdapter(Context context, ViewCreator<T> creator,
			List<T> dataCache) {
		super(context, creator, dataCache);
	}

	private static class ViewHolder {
		public View view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Holder View模式实现
		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			convertView = mCreator.createView(mInflater, position,
					getItem(position));
			holder.view = convertView;
			convertView.setTag(holder);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			// 释放当前的View的数据
			mCreator.releaseView(convertView, getItem(position));
			// 将新数据更新到HodlerView中
			mCreator.updateView(holder.view, position, getItem(position));
		}
		return convertView;
	}
}
