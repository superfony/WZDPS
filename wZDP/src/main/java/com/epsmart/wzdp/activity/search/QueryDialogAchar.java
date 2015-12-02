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
/**
 * 查询类 用于报表的查询
 * 
 */
public class QueryDialogAchar {
	private static final String TAG = QueryDialog.class.getName();
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
	private AppContext app_context;

	public QueryDialogAchar(Activity activity, final QueryDialogListener listener) {
		this.mActivity = activity;
		this.listener = listener;
		layoutInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = layoutInflater.inflate(R.layout.queryachar, null);
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


		TextView ev_one = (TextView) popView
				.findViewById(R.id.search_title_one);
		TextView ev_two = (TextView) popView
				.findViewById(R.id.search_title_two);
		TextView ev_three = (TextView) popView
				.findViewById(R.id.search_title_three);
		TextView ev_four = (TextView) popView
				.findViewById(R.id.search_title_four);
		TextView ev_five = (TextView) popView
				.findViewById(R.id.search_title_five);

		et_one = (EditText) popView.findViewById(R.id.search_et_one);// 条件一
		et_two = (EditText) popView.findViewById(R.id.search_et_two);// 条件二
		et_three = (EditText) popView.findViewById(R.id.search_et_three);// 条件三
		et_four = (EditText) popView.findViewById(R.id.search_et_four);// 条件二
		et_five = (EditText) popView.findViewById(R.id.search_et_five);// 条件二
		
		ev_one.setText("开始时间:");
		ev_two.setText("结束时间:");
		ev_three.setVisibility(View.GONE);
		ev_four.setVisibility(View.GONE);
		ev_five.setVisibility(View.GONE);
		et_three.setVisibility(View.GONE);
		et_four.setVisibility(View.GONE);
		et_five.setVisibility(View.GONE);
		
		et_one.setFocusable(false);
		
		et_one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);//
			}
		});
		
		et_two.setFocusable(false);
		
		et_two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});
		

		app_context = (AppContext) activity.getApplication();

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
							.getReqXML("startTime",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("endTime",
									et_two.getText().toString())));
				listener.doQuery(req);
				popupWindow.dismiss();
			}

		});
	}

	public void show(View anchor) {// TODO 设置显示
		popupWindow.setHeight(dipToPixels(300));
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
				if((month + 1)<=9){
					et.setText(year + "-" + ("0"+(month + 1)) + "-" + dayOfMonth);
				}else{
				et.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				}
			}
		};
		Calendar calendar = Calendar.getInstance();
		Dialog dialog = new DatePickerDialog(mActivity, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}
}