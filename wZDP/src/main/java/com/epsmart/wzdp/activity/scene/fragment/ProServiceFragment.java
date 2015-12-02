package com.epsmart.wzdp.activity.scene.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.common.Common;
/**
 * 现场服务Fragment
 * @author hqq
 *
 */
public class ProServiceFragment extends SceneFragmemt{
	private View view;
	private String ATG = ProServiceFragment.class.getName();
	private Button framebtn_apply;//申请服务按钮
	private Button framebtn_question;//服务问题按钮
	private Button framebtn_list;//待评价列表按钮
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
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
			view = inflater.inflate(R.layout.fragment_scene_pro_service, container, false);
		} catch (Exception e) {
			Log.i(ATG, "....Exception......>>" + e);
		}
		int backStackCount = getFragmentManager().getBackStackEntryCount();
		Log.i("ProServiceFragment", "<<<<<backStackCount>>>>>>"+backStackCount);
		Common.replaceRightFragment(activity, new ServiceTopApplyFragment(), false,
				R.id.top_container);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		framebtn_apply = (Button) view.findViewById(R.id.frame_btn_service_apply);
		framebtn_question = (Button) view.findViewById(R.id.frame_btn_service_quesition);
		framebtn_list = (Button) view.findViewById(R.id.frame_btn_service_evaluate);
		framebtn_apply.setEnabled(false);
		framebtn_apply.setOnClickListener(frameBtnClick(framebtn_apply, 0));
		framebtn_question
				.setOnClickListener(frameBtnClick(framebtn_question, 0));
		framebtn_list.setOnClickListener(frameBtnClick(framebtn_list, 0));

	}
	
	private View.OnClickListener frameBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				
				clearStackOther();
				if (btn == framebtn_apply) {
					framebtn_apply.setEnabled(false);
					Log.i(ATG, "....framebtn_info......>>" );
					Common.replaceRightFragment(activity, new ServiceTopApplyFragment(), false,
							R.id.top_container);
				} else {
					framebtn_apply.setEnabled(true);
				}
				if (btn == framebtn_question) {
					framebtn_question.setEnabled(false);
					Common.replaceRightFragment(activity, new ServiceTopQuestionFragment(), false,
							R.id.top_container);
				} else {
					framebtn_question.setEnabled(true);
				}
				if (btn == framebtn_list) {
					framebtn_list.setEnabled(false);
					Common.replaceRightFragment(activity, new ServiceTopListFragment(), false,
							R.id.top_container);
				} else {
					framebtn_list.setEnabled(true);
				}
			}
		};
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
