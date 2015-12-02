package com.epsmart.wzdp.activity.search;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.common.RequestXmlHelp;

public class QuerySuppliesDialogAchar {
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
	private EditText et_six = null;
	private AppContext app_context;
	private final static int DIALOG=1;

	public QuerySuppliesDialogAchar(final Activity activity, final QueryDialogListener listener) {
		this.mActivity = activity;
		this.listener = listener;
		layoutInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = LayoutInflater.from(mActivity).inflate(R.layout.querysuppliesachar, null);
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

		et_one = (EditText) popView.findViewById(R.id.search_et_one);// 条件一
		et_two = (EditText) popView.findViewById(R.id.search_et_two);// 条件二
		et_three = (EditText) popView.findViewById(R.id.search_et_three);// 条件三
		et_four = (EditText) popView.findViewById(R.id.search_et_four);// 条件二
		et_five = (EditText) popView.findViewById(R.id.search_et_five);// 条件二
		et_six = (EditText) popView.getRootView().findViewById(R.id.search_et_six);//
		
		et_five.setFocusable(false);
		et_five.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("请选择")
						.setIcon(R.drawable.ic_launcher)
						.setCancelable(true)
						.setItems(R.array.arrys,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										 String arrys=mActivity.getResources().getStringArray(R.array.arrys)[which];
										et_five.setText(arrys);
										dialog.cancel();
									}
								});
				AlertDialog dlg = builder.create();
				dlg.show();
			}
		});
		et_six.setFocusable(false);
		et_six.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("请选择")
						.setIcon(R.drawable.ic_launcher)
						.setCancelable(true)
						.setItems(R.array.arry,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										 String arry=mActivity.getResources().getStringArray(R.array.arry)[which];
										et_six.setText(arry);
										dialog.cancel();
									}
								});
				AlertDialog dlg = builder.create();
				dlg.show();
			}
		});
		
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
		et_three.setFocusable(false);
		
		et_three.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(v);
			}
		});
		et_four.setFocusable(false);
		
		et_four.setOnClickListener(new OnClickListener() {
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
							.getReqXML("udate_s",
									et_one.getText().toString())
							.append(RequestXmlHelp.getReqXML("udate_d",
									et_two.getText().toString()))
							.append(RequestXmlHelp.getReqXML("eindtl_s",
									et_three.getText().toString()))
							.append(RequestXmlHelp.getReqXML("eindtl_d",
									et_four.getText().toString()))
							.append(RequestXmlHelp.getReqXML("zdydj",
							        et_five.getText().toString()))
							.append(RequestXmlHelp.getReqXML("select_type",
									et_six.getText().toString())));
					
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
