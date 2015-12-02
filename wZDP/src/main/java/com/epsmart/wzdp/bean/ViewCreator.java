package com.epsmart.wzdp.bean;

import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @author fony
 *
 * @param <E>
 */
public interface ViewCreator<E> {
	/**
	 * 创建View,HolderAdapter需要创建View时，会调用此方法创建View
	 * 
	 * @param inflater
	 * @param position
	 * @param data
	 * @return
	 */
	View createView(LayoutInflater inflater, int position, E data);

	/**
	 * 更新View
	 * 
	 * @param view
	 * @param position
	 * @param data
	 */
	void updateView(View view, int position, E data);

	/**
	 * 这个View将被从可显示区中移除
	 * 
	 * @param view
	 * @param position
	 * @param data
	 */
	void releaseView(View view, E data);
};