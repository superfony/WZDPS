package com.epsmart.wzdp.activity.achar;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.epsmart.wzdp.R;

/*
 * 项目进度情况统计报表中的Popupwindow
 */
public class SelectPicPopupWindow extends PopupWindow {


	private View mMenuView;
	@SuppressWarnings("unused")
	private EditText spinner1,buildtime,projectstart,facttime,Voltage_Class,projecttext,factorytext,typetext;
	private Button select;

	public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.project_dialog, null);
		spinner1 = (EditText)mMenuView.findViewById(R.id.spinner1);
		projecttext = (EditText) mMenuView.findViewById(R.id.projecttext);
		factorytext = (EditText) mMenuView.findViewById(R.id.factorytext);
		typetext = (EditText) mMenuView.findViewById(R.id.typetext);
		buildtime = (EditText) mMenuView.findViewById(R.id.buildtime);
		projectstart = (EditText) mMenuView.findViewById(R.id.projectstart);
		facttime = (EditText) mMenuView.findViewById(R.id.facttime);
		Voltage_Class = (EditText) mMenuView.findViewById(R.id.Voltage_Class);
		select = (Button) mMenuView.findViewById(R.id.select);

		//取消按钮
//		btn_cancel.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				//销毁弹出框
//				dismiss();
//			}
//		});
		
		//设置按钮监听
//		select.setOnClickListener(itemsOnClick);

		select.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
							//销毁弹出框
							dismiss();
						}
					});
		
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimationFade);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pup_pop).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
	}

}
