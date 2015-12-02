package com.epsmart.wzdp.activity.supply.service;

import android.R.integer;
import android.view.View;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.common.Constants;
import com.epsmart.wzdp.service.CommonService;

public class SupplyService extends CommonService {

	private View clickedView;

	public SupplyService(CommonActivity activity, View view) {
		super(activity, view);
	}

	@SuppressWarnings("deprecation")
	public void setLeftTextViewBackgroundColor(View clickedView, int tag) {
		this.clickedView = clickedView;

		formerly(Constants.supplyLeftTag);

		Constants.supplyLeftTextView.setFocusable(true);
		Constants.supplyLeftTextView.setClickable(true);
		clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.left_click_bg));

		if (MainActivity.provenance.equals("supply")) {
			switch (tag) {
			case 0:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn1xz));
				break;
			case 1:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn2xz));
				break;
			case 2:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn3xz));
				break;

			}
		} else if (MainActivity.provenance.equals("scene")) {
			switch (tag) {
			case 0:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.nbtn1xz));
				break;
			case 1:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.nbtn2xz));
				break;
			case 2:
				clickedView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.nbtn3xz));
				break;

			}
		}


		clickedView.setFocusable(false);
		clickedView.setClickable(false);
		Constants.supplyLeftTextView = clickedView;
		Constants.supplyLeftTag = tag;
	}


	/**
	 * 判断上次点击的 items 实现切换回原来的背景
	 * @param formerlyTag
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	private void formerly(int formerlyTag) {
		if (MainActivity.provenance.equals("supply")) {
			switch (formerlyTag) {
			case 0:
				Constants.supplyLeftTextView.setBackgroundDrawable(activity.getResources().getDrawable(
						R.drawable.sidebar_icon_tq));
				break;
			case 1:
				Constants.supplyLeftTextView
						.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn2));
				break;
			case 2:
				Constants.supplyLeftTextView
						.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.btn3));
				break;

			}
		} else if (MainActivity.provenance.equals("scene")) {
			switch (formerlyTag) {
			case 0:
				Constants.supplyLeftTextView.setBackgroundDrawable(activity.getResources().getDrawable(
						R.drawable.nbtn1));
				break;
			case 1:
				Constants.supplyLeftTextView
						.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.nbtn2));
				break;
			case 2:
				Constants.supplyLeftTextView
						.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.nbtn3));
				break;

			}
		}
	}

}
