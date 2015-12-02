package com.epsmart.wzdp.activity.achar;

import com.epsmart.wzdp.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ValidFragment")
public class FragmentCharShow extends Fragment {
	private ProgressDialog pd;
	private View view;

	public FragmentCharShow() {
	}

	public FragmentCharShow(View view) {
		this.view = view;
	}

	private View rootView;// 缓存Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// pd=new ProgressDialog(this.getActivity()).show(this.getActivity(),

		// "提示", "数据绘制中....");
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.tab_fragment, null);
		}
		LinearLayout showlay = (LinearLayout) rootView
				.findViewById(R.id.show_lay);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		showlay.removeAllViews();
		showlay.addView(view, lp);
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();

	}

}
