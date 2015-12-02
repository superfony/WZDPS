package com.epsmart.wzdp.activity.achar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;

/**
 * 合同结算情况统计中的PopupWindow
 *
 */

public class SettlePupWindow extends PopupWindow {

	private View mMenuView;
	private EditText  spinner1,material_ner, Voltage_Class, supplier_ner,batch_ner, data_ner;
	private Button select;

	@SuppressLint("NewApi")
	public SettlePupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.settlement_dialog, null);
		spinner1 = (EditText) mMenuView.findViewById(R.id.spinner1);
		material_ner = (EditText) mMenuView.findViewById(R.id.material_ner);
		Voltage_Class = (EditText) mMenuView.findViewById(R.id.Voltage_Class);
		supplier_ner = (EditText) mMenuView.findViewById(R.id.supplier_ner);
		batch_ner = (EditText) mMenuView.findViewById(R.id.batch_ner);
		data_ner = (EditText) mMenuView.findViewById(R.id.data_ner);
		select = (Button) mMenuView.findViewById(R.id.select);
		
		data_ner.setOnClickListener(new OnClickListener (){
			@Override
			public void onClick(View v) {
				selectDateAndTime(context,v);
			}
		});
		
		
		// 设置按钮监听
		
		select.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
							//销毁弹出框
							dismiss();
						}
					});
		
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationFade);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pup_pop).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	//年月日时分秒
	protected void selectDateAndTime(Activity activity,View v) {

		EditText et = (EditText) v;
		DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(
				activity, null);
		dialog.dateTimePicKDialog(et);
	}
}
