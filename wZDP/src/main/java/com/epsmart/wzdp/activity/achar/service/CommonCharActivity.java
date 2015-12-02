package com.epsmart.wzdp.activity.achar.service;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.fragment.FillTableHelp;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.MyActivityManager;
import com.epsmart.wzdp.db.DatabaseHelper;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;


public class CommonCharActivity extends FragmentActivity {

	public DatabaseHelper databaseHelper = null;
	protected MyActivityManager activityManager;
	protected FillTableHelp fillHelp;
	protected HashMap<String, ImageView> imageMap = new HashMap<String, ImageView>();
	protected String tag;
	public static int state=0;
	private String TAG = CommonCharActivity.class.getName();
	private String vers;
	private String pspid;
	protected AppContext appContext;

	/**
	 * @return the pspid
	 */
	public String getPspid() {
		return pspid;
	}

	/**
	 * @param pspid the pspid to set
	 */
	public void setPspid(String pspid) {
		this.pspid = pspid;
	}

	/**
	 * @return the versions
	 */
	public String getVers() {
		return vers;
	}

	/**
	 * @param string the versions to set
	 */
	public void setVers(String vers) {
		this.vers = vers;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setImageMap(HashMap<String, ImageView> imageMap) {
		this.imageMap = imageMap;
	}

	public FillTableHelp getFillHelp() {
		return fillHelp;
	}

	public void setFillHelp(FillTableHelp fillHelp) {
		this.fillHelp = fillHelp;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext=(AppContext)getApplication();
		activityManager = MyActivityManager.getMyActivityManagerInstance();
		activityManager.pushActivity(this);
		// resolveSocketProblem();
		initActionBar();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		
	}

	/**
	 * 解决访问网络的问题（socket访问网络，webService访问网络）
	 */
	protected void resolveSocketProblem() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
	}

	/**
	 * 
	 * @param v
	 */
	public void initDateAndTime(View v) {
		EditText et = (EditText) v;
		DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(this,
				null);
		dialog.dateTimePicKDialog(et);

	}

	/**
	 * 替换右侧Fragment
	 * 
	 * @param fragment
	 */
	protected void replaceRightFragment(Fragment fragment, int layoutFragment) {
		replaceRightFragment(fragment, false, layoutFragment);
	}

	protected void replaceRightFragment(Fragment fragment,
			boolean popBackStack, int layoutFragment) {
		Common.replaceRightFragment(this, fragment, popBackStack,
				layoutFragment);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/** 获取数据库操作对象 */
	public DatabaseHelper getDbHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	/*
	 * 释放资源
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * 消除通知栏的系统通知
	 */
	protected void cancleNotifycation() {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// 得到系统通知服务
		manager.cancel(1001);
	}

	// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View view = actionBar.getCustomView();
		RelativeLayout exitBt = (RelativeLayout) view.findViewById(R.id.add_lay);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int count = getFragmentManager().getBackStackEntryCount();
			if (count == 1) {
				finish();
				return true;
			}
			if (CommonCharActivity.state == 1 || CommonCharActivity.state == 5) {
				if (count == 3) {
					getFragmentManager().popBackStack();
					return true;
				}
			}
		}
		return true;
	}
}
