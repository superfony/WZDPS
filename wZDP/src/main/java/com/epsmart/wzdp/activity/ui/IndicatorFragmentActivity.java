/*
 * @author http://blog.csdn.net/singwhatiwanna
 */
package com.epsmart.wzdp.activity.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.achar.service.BaseShowCharAct;
/**
 * 表格样式
 * @author fony
 *
 */
public class IndicatorFragmentActivity extends BaseShowCharAct implements
		OnPageChangeListener {
	private static final String TAG = "DxFragmentActivity";

	public static final String EXTRA_TAB = "tab";
	public static final String EXTRA_QUIT = "extra.quit";

	protected int mCurrentTab = 0;
	protected int mLastTab = -1;

	

	// 选项卡控件
	protected TitleIndicator mIndicator;

	public TitleIndicator getIndicator() {
		return mIndicator;
	}

	public class MyAdapter extends FragmentStatePagerAdapter{
		ArrayList<TabInfo> tabs = null;
		Context context = null;
		FragmentManager fm;

		public MyAdapter(Context context, FragmentManager fm,
				ArrayList<TabInfo> tabs) {
			super(fm);
			this.tabs = tabs;
			this.context = context;
			this.fm = fm;
		}

		public void setFragments(ArrayList<TabInfo> tabs) {
			if (this.tabs != null) {
				FragmentTransaction ft = fm.beginTransaction();
				for (TabInfo f : this.tabs) {
					ft.remove(f.fragment);
				}
				ft.commit();
				ft = null;
				fm.executePendingTransactions();
			}
			//this.tabs = tabs;
			//notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int pos) {
			Fragment fragment = null;
			if (tabs != null && pos < tabs.size()) {
				TabInfo tab = tabs.get(pos);
				if (tab == null)
					return null;
				fragment = tab.fragment;
			}
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			Log.d("Indi", ".........POSITION_NONE.........");
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			if (tabs != null && tabs.size() > 0)
				return tabs.size();
			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
		//	TabInfo tab = tabs.get(position);

			Fragment fragment = (Fragment) super.instantiateItem(container,
					position);

			return fragment;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.titled_fragment_tab_activity);
		bottom_btns = (LinearLayout) findViewById(R.id.bottom_btns);
		download_btn = (Button) findViewById(R.id.download_btn);
		select_btn = (Button) findViewById(R.id.select_btn);
		upload_btn = (Button) findViewById(R.id.upload_btn);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void clearAll() {

	}

	public void initViews(ArrayList<TabInfo> mTabs) {
		mCurrentTab = supplyTabs(mTabs);
		Intent intent = getIntent();
		if (intent != null) {
			mCurrentTab = intent.getIntExtra(EXTRA_TAB, mCurrentTab);
		}
		myAdapter = new MyAdapter(this, getSupportFragmentManager(), mTabs);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(myAdapter);
		mPager.setOnPageChangeListener(this);
		mPager.setOffscreenPageLimit(mTabs.size());
		mIndicator = (TitleIndicator) findViewById(R.id.pagerindicator);
		mIndicator.init(mCurrentTab, mTabs, mPager);

		mPager.setCurrentItem(mCurrentTab);
		mLastTab = mCurrentTab;
		// 设置viewpager内部页面之间的间距
		mPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin_width));
		// 设置viewpager内部页面间距的drawable
		mPager.setPageMarginDrawable(R.color.page_viewer_margin_color);
	}

	/**
	 * 添加一个选项卡
	 * 
	 * @param tab
	 */
	public void addTabInfo(TabInfo tab) {
		mTabs.add(tab);
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 从列表添加选项卡
	 * 
	 * @param tabs
	 */
	public void addTabInfos(ArrayList<TabInfo> tabs) {
		mTabs.addAll(tabs);
		myAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin())
				* position + positionOffsetPixels);
	}

	@Override
	public void onPageSelected(int position) {
		mIndicator.onSwitched(position);
		mCurrentTab = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			mLastTab = mCurrentTab;
		}
	}

	protected TabInfo getFragmentById(int tabId) {
		if (mTabs == null)
			return null;
		for (int index = 0, count = mTabs.size(); index < count; index++) {
			TabInfo tab = mTabs.get(index);
			if (tab.getId() == tabId) {
				return tab;
			}
		}
		return null;
	}

	/**
	 * 跳转到任意选项卡
	 * 
	 * @param tabId
	 *            选项卡下标
	 */
	public void navigate(int tabId) {
		for (int index = 0, count = mTabs.size(); index < count; index++) {
			if (mTabs.get(index).getId() == tabId) {
				mPager.setCurrentItem(index);
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	/**
	 * /** 在这里提供要显示的选项卡数据
	 */
	protected int supplyTabs(List<TabInfo> tabs) {

		return 0;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 单个选项卡类，每个选项卡包含名字，图标以及提示（可选，默认不显示）
	 */
	public static class TabInfo implements Parcelable {

		private int id;
		private int icon;
		private String name = null;
		public boolean hasTips = false;
		public Fragment fragment = null;

		public Fragment getFragment() {
			return fragment;
		}

		public void setFragment(Fragment fragment) {
			this.fragment = fragment;
		}

		public boolean notifyChange = false;

		public TabInfo(int id, String name, Fragment fragment) {
			this(id, name, 0, fragment);
		}

		public TabInfo(int id, String name, boolean hasTips, Fragment fragment) {
			this(id, name, 0, fragment);
			this.hasTips = hasTips;
		}

		public TabInfo(int id, String name, int iconid, Fragment fragment) {
			super();

			this.name = name;
			this.id = id;
			icon = iconid;
			this.fragment = fragment;

		}

		public TabInfo(Parcel p) {
			this.id = p.readInt();
			this.name = p.readString();
			this.icon = p.readInt();
			this.notifyChange = p.readInt() == 1;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setIcon(int iconid) {
			icon = iconid;
		}

		public int getIcon() {
			return icon;
		}

		public static final Parcelable.Creator<TabInfo> CREATOR = new Parcelable.Creator<TabInfo>() {
			public TabInfo createFromParcel(Parcel p) {
				return new TabInfo(p);
			}

			public TabInfo[] newArray(int size) {
				return new TabInfo[size];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel p, int flags) {
			p.writeInt(id);
			p.writeString(name);
			p.writeInt(icon);
			p.writeInt(notifyChange ? 1 : 0);
		}

	}
}
