package com.epsmart.wzdp.activity.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.epsmart.wzdp.bean.ViewCreator;

/**
 * 
 * @author fony
 *
 * @param <T>
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {
	protected Context mContext;
	/** 用于从XML文件中创建Layout */
	protected LayoutInflater mInflater;

	/** View创建器 */
	protected ViewCreator<T> mCreator;
	/** 数据缓存 */
	protected List<T> mDataCache=null;
	public void setmDataCache(List<T> mDataCache) {
		this.mDataCache = mDataCache;
	}

	/**
	 * 数据源在ListView中的所有项的选中状态
	 */
	private SparseBooleanArray checkedStat;
	/**
	 * 上一个选中状态的单选按钮的索引,-1表示没有选中状态的单选按钮
	 */
	private int prevChecdedRadio = -1;

	/**
	 * 创建Adapter,需要给定View创建接口
	 * 
	 * @param inflater
	 * @param creator
	 */
	public AbstractAdapter(Context context, ViewCreator<T> creator) {
		this.mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCreator = creator;
		checkedStat = new SparseBooleanArray();
		checkedAll(false);
	}

	public AbstractAdapter(Context context, ViewCreator<T> creator,
			List<T> dataCache) {
		this(context, creator);
		this.mDataCache = dataCache;
	}

	/**
	 * 更新数据集
	 * 
	 * @param data
	 */
	public void update(List<T> data) {
		removeAll();
		mDataCache = data;
		checkedAll(false);
	}

	/**
	 * 添加数据集,向数据缓存中添加多个元素
	 * 
	 * @param set
	 */
	public void add(List<T> set) {
		//add(set, getDataCache().size());
		if(set==null)
			return;
		getDataCache().addAll(set);
	}

	/**
	 * 添加数据集,向数据缓存中添加多个元素
	 * 
	 * @param set
	 * @param position
	 *            插入的起始位置
	 */
	public void add(List<T> set, int position) {
		if (null == set || set.size() < 1) {
			return;
		}
		int len = set.size();
		for (int i = 0; i < len; i++) {
			add(position + i, set.get(i), false);
		}
		Log.i("", "........add 2...........>>");
		
		
		notifyDataSetChanged();
	}

	/**
	 * 添加数据元素,向数据缓存中添加单个元素
	 * 
	 * @param item
	 */
	public void add(T item, boolean notifyDataSetChanged) {
		add(getDataCache().size(), item, notifyDataSetChanged);
	}

	/**
	 * 
	 * 添加数据元素,向数据缓存中添加单个元素
	 * 
	 * @param position
	 *            插入的位置
	 * @param item
	 *            插入的对象
	 * @param notifyDataSetChanged
	 *            是否通知数据变化
	 */
	public void add(int position, T item, boolean notifyDataSetChanged) {
		// 当前集合最后一项索引
		int lastIndex = getDataCache().size() - 1;
		if (position < -1 || position > lastIndex)
			return;
//		if (position < lastIndex) {
//			// 不是最后一项,则调整选中状态记录表,顺序后移
//			for (int i = lastIndex; i >= position; i--) {
//				checkedStat.put(i + 1, checkedStat.get(i));
//			}
//		} else if (position > lastIndex) {
//			checkedStat.put(position, false);
//		} else {
//			checkedStat.put(position, checkedStat.get(position));
//		}
		Log.i("", "........add....3........>>");
			getDataCache().add(position, item);
		if (notifyDataSetChanged)
			notifyDataSetChanged();
	}

	/**
	 * 交换两个元素的位置
	 * 
	 * @param src
	 * @param target
	 */
	public void exchange(int src, int target) {
		if (src > target) {
			int temp = target;
			target = src;
			target = temp;
		}
		T endObject = getItem(target);
		T startObject = getItem(src);
		getDataCache().set(src, endObject);
		getDataCache().set(target, startObject);
	}

	/**
	 * 
	 * 同步更改所有项选中状态.
	 * 
	 * @param checked
	 */
	public void checkedAll(Boolean checked) {
		for (int i = getDataCache().size() - 1; i >= 0; i--) {
			setChecked(i, checked, false);
		}
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 * 设置指定项的选中状态.
	 * 
	 * @param position
	 * @param checked
	 * @param notifyDataSetChanged
	 *            是否通知数据变化
	 */
	public void setChecked(int position, Boolean checked,
			boolean notifyDataSetChanged) {
		checkedStat.put(position, checked);
		if (notifyDataSetChanged)
			notifyDataSetChanged();
	}

	/**
	 * 
	 * 反选指定索引项.
	 * 
	 * @param position
	 * @param notifyDataSetChanged
	 *            是否通知数据变化
	 */
	public void toggle(int position, boolean notifyDataSetChanged) {
		setChecked(position, !checkedStat.get(position), notifyDataSetChanged);
	}

	/**
	 * 
	 * 全部反选.
	 * 
	 */
	public void toggleAll() {
		for (int i = getDataCache().size() - 1; i >= 0; i--) {
			toggle(i, false);
		}
		this.notifyDataSetChanged();
	}

	public void removeAll() {
		getDataCache().clear();
		checkedStat.clear();
		this.notifyDataSetChanged();
	}

	/**
	 * 
	 * 删除指定的所有项.
	 * 
	 * @param deletedPositions
	 *            待删除的索引列表
	 */
	public void remove(List<Integer> deletedPositions) {
		if (null == deletedPositions || deletedPositions.size() < 1) {
			return;
		}
		// 按升序排序
		Collections.sort(deletedPositions);
		int len = deletedPositions.size();
		// 按照索引由大到小的顺序删除
		for (int i = len - 1; i >= 0; i--) {
			remove(deletedPositions.get(i), false);
		}
		notifyDataSetChanged();
	}

	/**
	 * 
	 * 删除指定索引项.
	 * 
	 * @param position
	 *            指定索引
	 * @param notifyDataSetChanged
	 *            是否通知数据变化
	 */
	public void remove(int position, boolean notifyDataSetChanged) {
		// 当前集合最后一项索引
		int lastIndex = getDataCache().size() - 1;
		if (position < 0 || position > lastIndex)
			return;
		if (position < lastIndex) {
			// 不是最后一项,则调整选中状态记录表,顺序后移
			for (int i = position; i < lastIndex; i++) {
				checkedStat.put(i, checkedStat.get(i + 1));
			}
		} else {
			// 最后一项,直接删除
			checkedStat.delete(position);
		}
		mDataCache.remove(position);
		if (notifyDataSetChanged)
			notifyDataSetChanged();
	}

	/**
	 * 
	 * 删除指定索引项.
	 * 
	 * @param position
	 *            指定索引
	 */
	public void remove(int position) {
		remove(position, true);
	}

	@Override
	public int getCount() {
		return getDataCache().size();
	}

	@Override
	public T getItem(int position) {
		return getDataCache().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public SparseBooleanArray getCheckedStat() {
		return checkedStat;
	}

	public int getPrevRadioIndex() {
		return prevChecdedRadio;
	}

	public List<T> getDataCache() {
		if (null == mDataCache)
			mDataCache = new ArrayList<T>();
		return mDataCache;
	}

	public void setDataCache(List<T> dataCache) {
		this.mDataCache = dataCache;
	}
}
