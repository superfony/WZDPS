package com.epsmart.wzdp.activity.achar;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;

/*
 * 合同在线情况统计报表
 */
public class SubstancePupWindow extends PopupWindow {


	private View mMenuView;
	@SuppressWarnings("unused")
	private EditText spinner1,material_ner,Voltage_Class,project_ner,supplier_ner,batch_ner,data_ner,display_ner;
	private Button select;
	//申明PopupWindow对象引用
	private PopupWindow popWindow;

	public SubstancePupWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.signed_dialog, null);
		spinner1 = (EditText)mMenuView.findViewById(R.id.spinner1);
		material_ner = (EditText) mMenuView.findViewById(R.id.material_ner);
		Voltage_Class = (EditText) mMenuView.findViewById(R.id.Voltage_Class);
		project_ner = (EditText) mMenuView.findViewById(R.id.project_ner);
		supplier_ner = (EditText) mMenuView.findViewById(R.id.supplier_ner);
		batch_ner = (EditText) mMenuView.findViewById(R.id.batch_ner);
		data_ner = (EditText) mMenuView.findViewById(R.id.data_ner);
		display_ner = (EditText) mMenuView.findViewById(R.id.display_ner);
		select = (Button) mMenuView.findViewById(R.id.select);
		
		data_ner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDateAndTime(context,v);
			}
			
		});
		
		
		
		//设置查询
		
		select.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(context, "查询ing",
					     Toast.LENGTH_SHORT).show();
				
							//销毁弹出框
							dismiss();
						}
					});
		
//		select.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(context, "查询ing",
//					     Toast.LENGTH_SHORT).show();
//				if (popWindow != null && popWindow.isShowing()) {
//					popWindow.dismiss();
//					popWindow = null;
//				}
//				
//			}
//		});

		
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
	
	
	/**
	 * 年月日 时分秒
	 * 
	 * @param v
	 */
	public void selectDateAndTime(Activity mActivity,View v) {
		EditText et = (EditText) v;
		DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(
				mActivity, null);
		dialog.dateTimePicKDialog(et);
	}
	
	
}
