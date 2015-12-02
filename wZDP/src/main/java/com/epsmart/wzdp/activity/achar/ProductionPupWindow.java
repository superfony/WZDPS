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
import com.epsmart.wzdp.R;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;

/*
 * 物资供应综合分析报表中PopupWindow
 */
public class ProductionPupWindow extends PopupWindow {

	private View mMenuView;
	@SuppressWarnings("unused")
	private EditText month_ner,company_ner,Voltage_Class,typetext;
	private Button select;

	public ProductionPupWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.supplies_dialog, null);
		month_ner = (EditText)mMenuView.findViewById(R.id.month_ner);
		company_ner = (EditText) mMenuView.findViewById(R.id.company_ner);
		typetext = (EditText) mMenuView.findViewById(R.id.typetext);
		Voltage_Class = (EditText) mMenuView.findViewById(R.id.Voltage_Class);
		select = (Button) mMenuView.findViewById(R.id.select);

		month_ner.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				selectDateAndTime(context,v);
			}
			
		});
		
		//设置按钮监听
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

	/**
	 * 年月日 时分秒
	 * 
	 * @param v
	 */
	public void selectDateAndTime(Activity activity,View v) {
		EditText et = (EditText) v;
		DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(
				activity, null);
		dialog.dateTimePicKDialog(et);
	}

}
