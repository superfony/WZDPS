package com.fony.menu.widget.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.fony.menu.sding.R;
import com.fony.menu.widget.slidingmenu.app.SlidingFragmentActivity;
import com.fony.menu.widget.slidingmenu.basic.SlidingMenu;
import com.fony.menu.widget.slidingmenu.basic.SlidingMenu.OnClosedListener;
import com.fony.menu.widget.slidingmenu.basic.SlidingMenu.OnOpenedListener;
import com.fony.menu.widget.slidingmenu.fragment.ActivityUtils;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

public class BaseSdingActivity extends SlidingFragmentActivity implements
		OnClickListener {

	protected Fragment mContent;
	protected FrameLayout sdingCloseBar;
	protected SlidingMenu sm;

	public void setSmGone() {
//		sm.setBehindWidth(0);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sdingCloseBar.setVisibility(View.GONE);
	}

	public void setSmShow() {
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		sm.setBehindWidth(185);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSdingBar(savedInstanceState);
		// 初始化菜单布局
		setBehindContentView(R.layout.slide_frame_menu);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		SlidingMenuFragment menuFragment = addSlidingMenuFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.commit();
		initSlidingMenu();
	}

	protected void initSdingBar(Bundle savedInstanceState) {
		// 设置主视图界面
		// ActivityUtils.requestNotTitleBar(this);
		setContentView(R.layout.sding_content_fragment);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, mContent).commit();
		sdingCloseBar = (FrameLayout) findViewById(R.id.sding_close_side_lay);
	}

	/**
	 * 不是具体的实现
	 * 
	 * @return
	 */
	public SlidingMenuFragment addSlidingMenuFragment() {
		SlidingMenuFragment menuFragment = new SlidingMenuFragment();
		return menuFragment;
	}

	/**
	 * 切换Fragment，也是切换视图的内容
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, fragment).commit();
		getSlidingMenu().showContent();
	}

	/**
	 * 关闭时显示sdingBar
	 * 
	 * @param sdingBar
	 */
	public void visibleSdingBar() {

		TranslateAnimation mShowAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		sdingCloseBar.setAnimation(mShowAction);
		sdingCloseBar.setVisibility(View.VISIBLE);
	}

	public void invisibleSdingBar() {
		sdingCloseBar.setVisibility(View.INVISIBLE);
	}

	public void goneSdingBar() {
		TranslateAnimation mHiddenAction = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		mHiddenAction.setDuration(500);
		sdingCloseBar.setAnimation(mHiddenAction);
		sdingCloseBar.setVisibility(View.GONE);

	}

	public void initSlidingMenu() {
		setSlidingActionBarEnabled(false);
		sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setBehindWidth(185);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setOnClosedListener(new OnClosedListener() {
			@Override
			public void onClosed() {
				visibleSdingBar();
			}
		});
		sm.setOnOpenedListener(new OnOpenedListener() {
			@Override
			public void onOpened() {
				goneSdingBar();
			}
		});

	}

	/**
	 * 销毁之前
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);

	}

	@Override
	public void onClick(View v) {
		toggle();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isMenuShowing()) {
				toggle();
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			toggle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}