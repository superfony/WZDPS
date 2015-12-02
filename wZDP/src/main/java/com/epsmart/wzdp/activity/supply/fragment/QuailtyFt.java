package com.epsmart.wzdp.activity.supply.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.common.Common;

/**
 * 质量监督管理Fragment
 *
 */
public class QuailtyFt extends SupplyFragmemt {
	private View view;
	private Button framebtn_info;
	private Button framebtn_question;
	private Button framebtn_list;
	private String ATG = QuailtyFt.class.getName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);

		}
		try {
			view = inflater.inflate(R.layout.menus_quality, container, false);
		} catch (Exception e) {
		}
		 int backStackCount = getFragmentManager().getBackStackEntryCount();
		 Log.i("QuailtyFt", ".......>>backStackCount"+backStackCount);
		Common.replaceRightFragment(activity, new QtyTopList(), false,
				R.id.top_container);
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
//		framebtn_info = (Button) view.findViewById(R.id.frame_btn_info);
//		framebtn_question = (Button) view.findViewById(R.id.frame_btn_question);
		framebtn_list = (Button) view.findViewById(R.id.frame_btn_list);
		framebtn_list.setEnabled(false);
//		framebtn_info.setOnClickListener(frameBtnClick(framebtn_info, 0));
//		framebtn_question.setOnClickListener(frameBtnClick(framebtn_question, 0));
		framebtn_list.setOnClickListener(frameBtnClick(framebtn_list, 0));

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private View.OnClickListener frameBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("", ".......>>onclick");
				clearStackOther();
//				if (btn == framebtn_info) {
//					framebtn_info.setEnabled(false);
//					Common.replaceRightFragment(activity, new QtyTopInfo(), false,
//							R.id.top_container);
//				} else {
//					framebtn_info.setEnabled(true);
//				}
//				if (btn == framebtn_question) {
//					framebtn_question.setEnabled(false);
//					Common.replaceRightFragment(activity, new QtyTopQuestion(), false,
//							R.id.top_container);
//				} else {
//					framebtn_question.setEnabled(true);
//				}
				if (btn == framebtn_list) {
					framebtn_list.setEnabled(false);
					Common.replaceRightFragment(activity, new QtyTopList(), false,
							R.id.top_container);
				} else {
					framebtn_list.setEnabled(true);
				}
			}
		};
	}

}
