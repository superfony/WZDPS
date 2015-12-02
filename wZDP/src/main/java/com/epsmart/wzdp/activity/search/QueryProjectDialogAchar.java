package com.epsmart.wzdp.activity.search;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.common.RequestXmlHelp;

public class QueryProjectDialogAchar {
	private static final String TAG = QueryProjectDialogAchar.class.getName();
	private Activity mActivity;
	private LayoutInflater layoutInflater;
	private PopupWindow popupWindow = null;
	private View popView;
	private QueryDialogListener listener;
	public String req = null;
	private EditText et_one = null;
	private EditText et_two = null;
	private EditText et_three = null;
	private EditText et_four = null;
	private EditText et_five = null;
	private EditText et_six = null;
	private EditText et_seven = null;
	private EditText et_eight = null;
	private AppContext app_context;
	private final static int DIALOG=1;

	public QueryProjectDialogAchar(final Activity activity, final QueryDialogListener listener) {
		this.mActivity = activity;
		this.listener = listener;
		layoutInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = LayoutInflater.from(mActivity).inflate(R.layout.queryprojectachar, null);
		popView.setFocusableInTouchMode(true);
		popView.setFocusable(true);
		popView.setAlpha(10);


		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.UpInAndOutAnimation);
		popupWindow.update();

		TextView ev_six = (TextView) popView.findViewById(R.id.search_title_six);
		
		et_one = (EditText) popView.findViewById(R.id.search_et_one);// 条件一
		et_two = (EditText) popView.findViewById(R.id.search_et_two);// 条件二
		et_three = (EditText) popView.findViewById(R.id.search_et_three);// 条件三
		et_four = (EditText) popView.findViewById(R.id.search_et_four);// 条件二
		et_five = (EditText) popView.findViewById(R.id.search_et_five);// 条件二
		et_six = (EditText) popView.getRootView().findViewById(R.id.search_et_six);//
		et_seven = (EditText) popView.findViewById(R.id.search_et_seven);// 条件二
		et_eight = (EditText) popView.getRootView().findViewById(R.id.search_et_eight);//
		
		
		et_five.setFocusable(false);
		et_five.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});
		et_six.setFocusable(false);
		et_six.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});
		
		et_seven.setFocusable(false);
		
		et_seven.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);//
			}
		});
		
		et_eight.setFocusable(false);
		
		et_eight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});

		View cancel = popView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		View query = popView.findViewById(R.id.query);
		query.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					req = RequestXmlHelp.getCommonXML(RequestXmlHelp
							.getReqXML("xmbm",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("xmmc",
									et_two.getText().toString()))
							.append(RequestXmlHelp.getReqXML("dydjms",
									et_three.getText().toString()))
						    .append(RequestXmlHelp.getReqXML("xmdw",
						    		et_four.getText().toString()))
							.append(RequestXmlHelp.getReqXML("jhkgsjStart",
									et_five.getText().toString()))
							.append(RequestXmlHelp.getReqXML("jhkgsjEnd",
							        et_six.getText().toString()))
							.append(RequestXmlHelp.getReqXML("jhtcsjStart",
											et_seven.getText().toString()))
							.append(RequestXmlHelp.getReqXML("jhtcsjEnd",
									        et_eight.getText().toString())));
					listener.doQuery(req);
					popupWindow.dismiss();
			}

		});
	}

	public void show(View anchor) {// TODO 设置显示
		popupWindow.setHeight(dipToPixels(450));
		Rect frame = new Rect();
		mActivity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);

		popupWindow.showAtLocation(anchor, Gravity.CENTER_HORIZONTAL
				| Gravity.TOP, 0, frame.top);
	}
 
	public int dipToPixels(int dip) {
		Resources r = mActivity.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}
	
	public void selectDate(View v) {
		final EditText et = (EditText) v;
		et.setInputType(InputType.TYPE_NULL);
		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {
				et.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			}
		};
		Calendar calendar = Calendar.getInstance();
		Dialog dialog = new DatePickerDialog(mActivity, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}
}
