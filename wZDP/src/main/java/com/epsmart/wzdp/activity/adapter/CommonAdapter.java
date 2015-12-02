package com.epsmart.wzdp.activity.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.epsmart.wzdp.bean.ViewCreator;

/**
 * 
 * @author fony
 *
 * @param <T>
 */
public class CommonAdapter<T> extends AbstractAdapter<T> {
	
	

	public CommonAdapter(Context context, ViewCreator<T> creator) {
		super(context, creator);
	}

	public CommonAdapter(Context context, ViewCreator<T> creator,
			List<T> dataCache) {
		super(context, creator, dataCache);
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		T data = mDataCache.get(pos);
		if (null == convertView) {
			convertView = mCreator.createView(mInflater, pos, data);
		} else {
			mCreator.updateView(convertView, pos, data);
		}
		return convertView;
	}

}
