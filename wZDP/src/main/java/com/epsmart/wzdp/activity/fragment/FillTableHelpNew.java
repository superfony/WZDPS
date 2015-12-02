package com.epsmart.wzdp.activity.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;
import com.epsmart.wzdp.widget.time.JudgeDate;
import com.epsmart.wzdp.widget.time.ScreenInfo;
import com.epsmart.wzdp.widget.time.WheelMain;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 
 * @author fony 0表示隐藏字段 1显示文本 （表格外显示） 2表格显示 输入 3单选框 4图片 5选择时间弹出框 6显示文本（表格内显示）
 *         7其它
 *         
 */
public class FillTableHelpNew {
	public Activity activity;
	public ViewGroup group;
	private List<EditText> editList = new ArrayList<EditText>();
	private List<String> keyList = new ArrayList<String>();
	private List<String> keyImage = new ArrayList<String>();
	private List<String> valueImage = new ArrayList<String>();
	private HashMap<String, ImageView> imageMap = new HashMap<String, ImageView>();
	private int tag = 1024;
	private LinearLayout btn_lay;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	// private int linlayW = 350;// 表格显示区域宽度
	// private int linlayHi = 140;// 图片显示的区域高度
	// private int linlayWv = 500;// 表单值区域显示区域宽度
	// private int linlayH = 80;// 表格高度
	// private int linlayWt = 360;// 标题显示区域宽度
	//

	private int linlayW = 350;// 表格显示区域宽度
	private int linlayHi = 140;// 图片显示的区域高度
	private int linlayWv = 290;// 表单值区域显示区域宽度
	private int linlayH = 80;// 表格高度
	private int linlayWt = 300;// 标题显示区域宽度
	private int colorline[] = new int[] { R.drawable.blue_line, 0,
			R.drawable.green_line, 1, R.drawable.yellow_line };
	private int linlayWpic = 240;//
	private int countpic = 0;
	private boolean isGroup;
	private int ed1,ed2;
	private int etext1,etext2,etext3,etext4,etext5;

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	// private ImageView image;
	public LinearLayout getBtn_lay() {
		return btn_lay;
	}

	public void setBtn_lay(LinearLayout btn_lay) {
		this.btn_lay = btn_lay;
	}

	/** 初始化布局 */
	public FillTableHelpNew(Activity activity, ViewGroup group) {
		this.activity = activity;
		this.group = group;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.white)
				.showImageForEmptyUri(R.color.white)
				.showImageOnFail(R.color.white).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	// 这里来填充具体的值 测试 两行三列的表格
	@SuppressLint("NewApi")
	public void fillTable(BasicEntity entity, ArrayList<String> list,
			ViewGroup group) {
		int size = list.size();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout table = new LinearLayout(activity);
		table.setOrientation(LinearLayout.VERTICAL);
		group.addView(table, params);
		LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				linlayWt, linlayH);

		LinearLayout.LayoutParams param_value = new LinearLayout.LayoutParams(
				linlayWv, linlayH);

		LinearLayout.LayoutParams param_pan = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout tablerow;
		TextView textView;
		EditText editText;
		LinearLayout linlay;
		LinearLayout.LayoutParams linlp;
		HorizontalScrollView hslay;
		HorizontalScrollView.LayoutParams hslp;
		ImageView img_line;
		ImageView img_pan;
		LinearLayout lay_pic = null;

		Field field = null;
		for (int i = 0; i < size; i++) {
			field = entity.fields.get(list.get(i));
			img_line = new ImageView(activity);
			img_line.setBackgroundResource(R.drawable.border_spacing);
			table.addView(img_line, tableParams);

			tablerow = new LinearLayout(activity);
			tablerow.setOrientation(LinearLayout.HORIZONTAL);

			// 设置最后一行最后两列进行合并单元格

			linlay = new LinearLayout(activity);
			linlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			textView = new TextView(activity);
			textView.setText(field.fieldChName);
			textView.setTextSize(21);
			textView.setTextColor(activity.getResources().getColor(
					R.color.itemtextcolors));
			linlp.gravity = Gravity.CENTER;
			linlp.leftMargin = 30;
			// 判断标题的显示宽度
			if (field.fieldView.equals("4")) {
				LinearLayout.LayoutParams pic_lin_lay = new LinearLayout.LayoutParams(
						200, linlayH);
				pic_lin_lay.gravity = Gravity.CENTER;
				pic_lin_lay.leftMargin = 30;
				linlay.addView(textView);
				tablerow.addView(linlay, pic_lin_lay);
			} else if (field.fieldView.equals("0")) {
				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				keyList.add(field.fieldEnName);
				editList.add(editText);
			} else {
				linlay.addView(textView, linlp);
				tablerow.addView(linlay, rowParams);
			}
			// 2表格显示 输入
			if (field.fieldView.equals("1")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				if (field.fieldChName.contains("行项目号")) {
					int info = Integer.valueOf(field.fieldContent);
					String newinfo = String.valueOf(info);
					editText.setText(newinfo);
				} else if (field.fieldChName.contains("物料编码")) {
					int info = Integer.valueOf(field.fieldContent);
					String newinfo = "" + info;
					editText.setText(newinfo);
				} else if (field.fieldChName.contains("供应商编码")) {
					int info = Integer.valueOf(field.fieldContent);
					String newinfo = "" + info;
					editText.setText(newinfo);
				} else if (field.fieldChName.contains("电压等级描述")) {
					textView.setText("电压等级描述");
				} else if (field.fieldChName.contains("线路长度")) {
					String info = field.fieldContent;
					editText.setText(info + "公里");
				}
				Log.w("FillTableHelpNew","field.fieldContent="+field.fieldContent+"field.fieldEnName="+field.fieldEnName);
				Log.w("FillTableHelpNew",""+("18".equals(field.fieldContent) && "state1".equals(field.fieldEnName)));

				if ("11".equals(field.fieldContent) && "state".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已提交");
				}
				if ("11".equals(field.fieldContent) && "state1".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state2".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state3".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state4".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已提交");
				}

				if ("11".equals(field.fieldContent) && "state5".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已提交");
				}
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);

			} else if (!TextUtils.isEmpty(field.fieldContent)
					&& field.fieldView.equals("2")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				
				if ("计划行号".equals(field.fieldChName)) {
					TextPaint tp = textView.getPaint();
					tp.setFakeBoldText(true);
				}
				
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);

			} else if (field.fieldView.equals("2")
					&& TextUtils.isEmpty(field.fieldContent)) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				editText = new EditText(activity);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);


				if ("计划行号".equals(field.fieldChName)) {
					TextPaint tp = textView.getPaint();
					tp.setFakeBoldText(true);
				}
				editText.setTextSize(21);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);
				// 3单选框
			} else if (field.fieldView.equals("3")) {

				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				final EditText edit = new EditText(activity);
				edit.setBackgroundColor(Color.WHITE);
				edit.setTextSize(21);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				linlp.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(edit, linlp);
				hslay.addView(linlay, hslp);
				final Field fld = field;
				String values = field.fieldContent;
				keyList.add(field.fieldEnName);
				if (field.fieldChName.contains("是否为重点物资")) {
					textView.setText("是否为重点物资");
				}
				editList.add(edit);
				if (values.contains("#")) {
					edit.setText(values.split("#")[0]);
					values = values.split("#")[1];
				}
				final String[] valuesArray = values.split(",");
				edit.setFocusable(false);
				edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(activity);
						builder.setTitle(fld.fieldChName)
								.setIcon(R.drawable.ic_launcher)
								.setCancelable(true)
								.setSingleChoiceItems(valuesArray, 0,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												edit.setText(valuesArray[which]);
												dialog.cancel();
											}
										});
						AlertDialog dlg = builder.create();
						dlg.show();
					}
				});
				tablerow.addView(hslay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);

				// 4图片
			} else if (field.fieldView.equals("4")) {
				if (countpic == 0) {
					lay_pic = new LinearLayout(activity);
					lay_pic.setOrientation(LinearLayout.HORIZONTAL);
				}
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						linlayWpic, linlayHi);

				ImageView image = new ImageView(activity);
				image.setBackgroundResource(R.drawable.add_photo);
				image.setTag(field.fieldEnName);
				Log.w("FillTableNew", "..........下载图片路径....>>"// TODO 添加进度条后的调整为new
						+ field.fieldContent);
				if (field.fieldContent.trim() != null) {

					// imageLoader.displayImage("http://127.0.0.1:8553"
					// + field.fieldContent, image, animateFirstListener);
				}

				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((com.epsmart.wzdp.activity.CommonActivity) activity)
								.setTag(v.getTag().toString());
						showMyDialog(v);
					}
				});
				imageMap.put(field.fieldEnName, image);
				tag++;
				lp.leftMargin = 1;
				lay_pic.addView(image, lp);
				if (countpic == 3) {
					tablerow.addView(lay_pic);
					countpic = 0;
				} else {
					table.removeView(tablerow);
					countpic++;
					continue;
				}
				// 5选择时间弹出框
			} else if (field.fieldView.equals("5")) {
				LinearLayout lay = new LinearLayout(activity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						linlayW, linlayHi);
				final EditText edit = new EditText(activity);

				edit.setGravity(Gravity.CENTER_VERTICAL);
				edit.setTextSize(21);
				edit.setHeight(linlayHi);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));

				keyList.add(field.fieldEnName);
				editList.add(edit);
				lp.gravity = Gravity.CENTER;
				lay.addView(edit, lp);

				if (field.fieldContent != null) {
					edit.setText(field.fieldContent);
				}
				edit.setFocusable(false);
				edit.setBackgroundColor(Color.WHITE);
				edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectDate(v);
					}
				});
				tablerow.addView(lay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);
			} else if (field.fieldView.equals("6")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);

			}

			table.addView(tablerow, tableParams);
		}
		img_line = new ImageView(activity);
		img_line.setBackgroundResource(R.drawable.border_spacing);
		table.setBackgroundColor(Color.WHITE);
		table.addView(img_line, tableParams);
		// btn_lay.setVisibility(View.VISIBLE);
	}

	/**
	 * 收集表单数据
	 * 
	 * @return
	 */
	public String getparams() {
		StringBuffer strb = new StringBuffer();
		strb.append("<opdetail>");
		for (int i = 0; i < keyList.size(); i++) {
			strb.append("<param>").append("<key>").append(keyList.get(i))
					.append("</key>").append("<value>")
					.append(((EditText) editList.get(i)).getText().toString())
					.append("</value>").append("</param>");

		}

		for (int i = 0; i < keyImage.size(); i++) {
			strb.append("<param>").append("<key>").append(keyImage.get(i))
					.append("</key>").append("<value>")
					.append(valueImage.get(i)).append("</value>")
					.append("</param>");
		}
		strb.append("</opdetail>");
		return strb.toString();

	}

	public String getparams(String reqXML) {
		StringBuffer strb = new StringBuffer();
		strb.append("<opdetail>");
		for (int i = 0; i < keyList.size(); i++) {
			strb.append("<param>").append("<key>").append(keyList.get(i))
					.append("</key>").append("<value>")
					.append(((EditText) editList.get(i)).getText().toString())
					.append("</value>").append("</param>");

		}

		for (int i = 0; i < keyImage.size(); i++) {
			strb.append("<param>").append("<key>").append(keyImage.get(i))
					.append("</key>").append("<value>")
					.append(valueImage.get(i)).append("</value>")
					.append("</param>");
		}
		strb.append(reqXML);
		strb.append("</opdetail>");
		return strb.toString();

	}

	private void selectDateNew(View v) {
		final EditText et = (EditText) v;
		et.setInputType(InputType.TYPE_NULL);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss");
		LayoutInflater inflater = LayoutInflater.from(activity);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(activity);
		final WheelMain wheelMain = new WheelMain(timepickerview, false);
		wheelMain.screenheight = screenInfo.getHeight();
		// 获取当前时间
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = dateFormat.format(curDate);

		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd hh:ss")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		new AlertDialog.Builder(activity).setTitle("选择时间")
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						et.setText(wheelMain.getTime());
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

	/**
	 * 年月日
	 * 
	 * @param v
	 */
	public void selectDate(View v) {
		final EditText et = (EditText) v;
		et.setInputType(InputType.TYPE_NULL);

		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {
				if ((month + 1) <= 9) {
					if (dayOfMonth <= 9) {
						et.setText(year + "-" + ("0" + (month + 1)) + "-"
								+ ("0" + dayOfMonth));
					} else {
						et.setText(year + "-" + ("0" + (month + 1)) + "-"
								+ dayOfMonth);
					}
				} else {
					if (dayOfMonth <= 9) {
						et.setText(year + "-" + (month + 1) + "-" + "0"
								+ dayOfMonth);
					} else {
						et.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
					}
				}
			}
		};
		Calendar calendar = Calendar.getInstance();
		Dialog dialog = new DatePickerDialog(activity, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		// new DatePickerD
		dialog.show();
	}

	/**
	 * 年月日 时分秒
	 * 
	 * @param v
	 */
	public void selectDateAndTime(View v) {
		EditText et = (EditText) v;
		DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(
				activity, null);
		dialog.dateTimePicKDialog(et);
	}
	
	/*
	 * 提示框
	 */
	   private void Tdialog() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage("当计划装车数量（总）改变时请必须填写备注原因");
				
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create().show();
				
			}
	   
	   private void Sdialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage("当所有装车数量之和大于计划装车数量时请修改计划装车数量！！！！！");
			
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
			
		}

	/**
	 * 图片转换成字符串类型
	 * 
	 * @param bitmap
	 * @return
	 */
	public String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(bytes);
	}

	/**
	 * 字符串转图片格式
	 * 
	 * @return
	 */
	public Bitmap stringToBitmap(String imagStr) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;
		try {
			bytes = decoder.decodeBuffer(imagStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bytes==null){
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

	public void onCammerResult(int requestCode, int resultCode, Intent data,
			String tag) {
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Toast.makeText(activity, "SD卡不存在", Toast.LENGTH_LONG)
							.show();
					return;
				}
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				saveJPG(bitmap);
				bitmap = postBit(bitmap);
				String bitmapStr = bitmapToString(bitmap);
				ImageView image = imageMap.get(tag);
				keyImage.add(tag);
				valueImage.add(bitmapStr);
				image.setImageBitmap(bitmap);
			}
			break;

		case 2:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String realPath = getRealPathFromURI(uri);
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 4;
					Bitmap bitmap = BitmapFactory.decodeFile(realPath, opts);
					saveJPG(bitmap);
					bitmap = postBit(bitmap);
					String bitmapStr = bitmapToString(bitmap);
					ImageView image = imageMap.get(tag);
					keyImage.add(tag);
					valueImage.add(bitmapStr);
					image.setImageBitmap(bitmap);
				} catch (Exception e) {
					Toast.makeText(activity, "图片过大或内容格式错误", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	/** 图片保存本地 */
	private void saveJPG(Bitmap bitmap) {
		FileOutputStream fileStr = null;
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/images/");
		file.mkdirs();// 创建文件夹
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/images/"
				+ "wuzdp"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System
						.currentTimeMillis())) + ".jpg";
		try {
			fileStr = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileStr);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileStr!=null){
					fileStr.flush();
					fileStr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/** 图片进行压缩处理 */
	private Bitmap postBit(Bitmap bitmapOrg) {

		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = linlayWpic;
		int newHeight = linlayHi;

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 旋转图片 动作
		// matrix.postRotate(45);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
				height, matrix, true);
		return parseRound(resizedBitmap, 20);
	}

	public Bitmap parseRound(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/**
	 * 新表单绘制界面 分模块显示 先确定显示的分区个数 标题单独处理 每个分区单独绘制表格 收集表单值
	 * 
	 * */

	RelativeLayout titleLay;
	LinearLayout contentLay;
	BasicEntity be;
	ViewHolder viewHolder;

	public void fillTableNew(ArrayList<BasicEntity> entityList) {
		if (entityList == null)
			return;
		int size = entityList.size();
		if (size == 0)
			return;
		if (!(size % 2 == 0))
			Toast.makeText(activity, "数据格式错误", Toast.LENGTH_LONG).show();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout table = new LinearLayout(activity);
		table.setOrientation(LinearLayout.VERTICAL);
		group.addView(table, params);

		LinearLayout.LayoutParams paramtc = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		LinearLayout.LayoutParams paramt = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < size; i++) {
			viewHolder = new ViewHolder();
			titleLay = new RelativeLayout(activity);
			titleLay.setBackgroundResource(colorline[i % 6]);
			titleLay.setTag(viewHolder);

			contentLay = new LinearLayout(activity);
			contentLay.setOrientation(LinearLayout.VISIBLE);
			contentLay.setBackgroundColor(Color.WHITE);
			contentLay.setTag(i + 1);

			ImageView openOrclose = new ImageView(activity);
			openOrclose.setBackgroundResource(R.drawable.up_);

			viewHolder.titlelay = titleLay;
			viewHolder.contentlay = contentLay;
			viewHolder.openOrclose = openOrclose;
			titleLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					viewHolder = (ViewHolder) v.getTag();
					if (viewHolder.contentlay.isShown()) {
						viewHolder.contentlay.setVisibility(View.GONE);
						viewHolder.openOrclose
								.setBackgroundResource(R.drawable.down_);

					} else {

						viewHolder.contentlay.setVisibility(View.VISIBLE);
						viewHolder.openOrclose
								.setBackgroundResource(R.drawable.up_);
					}
				}
			});

			/* 处理标题的显示 */
			be = entityList.get(i);
			Log.w("FillTableHelp","i="+i+",be="+be);
			Field field=be.fields.get("title");
			String title = (field!=null)?field.fieldContent:"";
			if ("基础信息".equals(title)) {
				viewHolder.contentlay.setVisibility(View.GONE);
				openOrclose.setBackgroundResource(R.drawable.down_);
			}

			TextView tv = new TextView(activity);
			tv.setText(title);
			tv.setTextSize(22);
			tv.setTextColor(Color.WHITE);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// lp.gravity = Gravity.CENTER_VERTICAL;
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.leftMargin = 20;
			titleLay.addView(tv, lp);
			RelativeLayout.LayoutParams lc = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lc.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lc.addRule(RelativeLayout.CENTER_VERTICAL);
			lc.rightMargin = 30;

			titleLay.addView(openOrclose, lc);
			i++;
			/* 处理内容的显示 */
			be = entityList.get(i);
			if (isGroup)
				fillTableGroupNew(be, be.fieldKeys, contentLay);
			else
				fillTableDouble(be, be.fieldKeys, contentLay);// TODO
			table.addView(titleLay, paramt);
			table.addView(contentLay, paramtc);
		}
	}

	class ViewHolder {
		RelativeLayout titlelay;
		LinearLayout contentlay;
		ImageView openOrclose;
	}

	/************************************************************************/
	// 监造师 根据监造组 分配建造师人员走下面的

	private String[] jgroup = null;
	private HashMap<String, ArrayList<Persion>> jgroupmap = null;
	private EditText phoneEd;
	private EditText groupEd;
	private static int positoin;

	class Persion {
		String name;
		String tel;
	}

	public void fillTableGroupNew(BasicEntity entity, ArrayList<String> list,
			ViewGroup group) {
		int size = list.size();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout table = new LinearLayout(activity);
		table.setOrientation(LinearLayout.VERTICAL);
		group.addView(table, params);
		LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				linlayWt, linlayH);

		LinearLayout.LayoutParams param_value = new LinearLayout.LayoutParams(
				linlayWv, linlayH);

		LinearLayout.LayoutParams param_pan = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout tablerow;
		TextView textView;
		EditText editText;
		LinearLayout linlay;
		LinearLayout.LayoutParams linlp;
		HorizontalScrollView hslay;
		HorizontalScrollView.LayoutParams hslp;
		ImageView img_line;
		ImageView img_pan;

		Field field = null;
		for (int i = 0; i < size; i++) {
			field = entity.fields.get(list.get(i));
			img_line = new ImageView(activity);
			img_line.setBackgroundResource(R.drawable.border_spacing);
			table.addView(img_line, tableParams);

			tablerow = new LinearLayout(activity);
			tablerow.setOrientation(LinearLayout.HORIZONTAL);

			// 设置最后一行最后两列进行合并单元格

			linlay = new LinearLayout(activity);
			linlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			textView = new TextView(activity);
			textView.setText(field.fieldChName);
			textView.setTextSize(21);
			textView.setTextColor(activity.getResources().getColor(
					R.color.itemtextcolors));
			linlp.gravity = Gravity.CENTER;
			linlp.leftMargin = 30;
			if (field.fieldView.equals("0")) {
				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				keyList.add(field.fieldEnName);
				editList.add(editText);
			} else {
				linlay.addView(textView, linlp);
				tablerow.addView(linlay, rowParams);
			}
			// 2表格显示 输入
			if (field.fieldView.equals("1")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);

			} else if (!TextUtils.isEmpty(field.fieldContent)
					&& field.fieldView.equals("2")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);

			} else if (field.fieldView.equals("2")
					&& TextUtils.isEmpty(field.fieldContent)) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				editText = new EditText(activity);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);

				editText.setTextSize(21);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);
				// 3单选框
			} else if (field.fieldView.equals("3")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				final EditText edit = new EditText(activity);
				edit.setBackgroundColor(Color.WHITE);
				edit.setTextSize(21);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				edit.setFocusable(false);
				linlp.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(edit, linlp);
				hslay.addView(linlay, hslp);
				final Field fld = field;
				String values = field.fieldContent;
				keyList.add(field.fieldEnName);
				editList.add(edit);
				if (field.fieldEnName.equals("supervisorteam")) {
					groupEd = edit;
				}
				if (field.fieldEnName.equals("supervisorname")) {
					jgroupmap = getGroupInfo(values.split("#")[1]);
					Log.i("", "..............." + jgroupmap);
					edit.setText(values.split("#")[0]);
					edit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new Builder(activity);
							builder.setTitle(fld.fieldChName)
									.setIcon(R.drawable.ic_launcher)
									.setCancelable(true)
									.setSingleChoiceItems(
											getPersionValues(getArrayValues(jgroupmap)),
											0,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													edit.setText(getPersionValues(getArrayValues(jgroupmap))[which]);
													phoneEd.setText(getPoneValues(
															getArrayValues(jgroupmap),
															which));
													dialog.cancel();
												}
											});
							AlertDialog dlg = builder.create();
							dlg.show();
						}
					});

				} else {

					if (values.contains("#")) {
						edit.setText(values.split("#")[0]);
						values = values.split("#")[1];
					}

					final String[] valuesArray = values.split(",");

					edit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new Builder(activity);
							builder.setTitle(fld.fieldChName)
									.setIcon(R.drawable.ic_launcher)
									.setCancelable(true)
									.setSingleChoiceItems(
											valuesArray,
											0,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													edit.setText(valuesArray[which]);
													dialog.cancel();
												}
											});
							AlertDialog dlg = builder.create();
							dlg.show();
						}
					});
				}
				tablerow.addView(hslay, param_value);

			} else if (field.fieldView.equals("5")) {
				LinearLayout lay = new LinearLayout(activity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						linlayW, linlayHi);
				final EditText edit = new EditText(activity);

				edit.setGravity(Gravity.CENTER_VERTICAL);
				edit.setTextSize(21);
				edit.setHeight(linlayHi);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));

				keyList.add(field.fieldEnName);
				editList.add(edit);
				lp.gravity = Gravity.CENTER;
				lay.addView(edit, lp);

				if (field.fieldContent != null) {
					edit.setText(field.fieldContent);
				}
				edit.setFocusable(false);
				edit.setBackgroundColor(Color.WHITE);
				edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectDate(v);
					}
				});
				tablerow.addView(lay, param_value);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_HORIZONTAL;
				tablerow.addView(img_pan, param_pan);
			} else if (field.fieldView.equals("6")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				if (field.fieldEnName.equals("supervisorphone")) {
					phoneEd = editText;
				}
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);

			}

			table.addView(tablerow, tableParams);
		}
		img_line = new ImageView(activity);
		img_line.setBackgroundResource(R.drawable.border_spacing);
		table.setBackgroundColor(Color.WHITE);
		table.addView(img_line, tableParams);
	}

	private HashMap<String, ArrayList<Persion>> getGroupInfo(String str) {
		HashMap<String, ArrayList<Persion>> map = new HashMap<String, ArrayList<Persion>>();
		ArrayList<Persion> persionList;
		JSONArray groupJson;
		try {
			groupJson = new JSONArray(str);
			int length = groupJson.length();
			for (int i = 0; i < length; i++) {
				JSONObject jb = groupJson.getJSONObject(i);
				JSONArray groupinfo = jb.getJSONArray("info");
				int n = groupinfo.length();
				persionList = new ArrayList<Persion>();
				for (int j = 0; j < n; j++) {
					JSONObject jbb = groupinfo.getJSONObject(j);
					Persion persion = new Persion();
					persion.name = jbb.get("SupervisorName").toString();
					persion.tel = jbb.get("SupervisorPhone").toString();
					persionList.add(persion);

				}
				map.put(jb.get("SupervisorTeam").toString(), persionList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	public Persion[] getArrayValues(HashMap<String, ArrayList<Persion>> map) {
		Persion[] arrayValue = null;
		String groupName = groupEd.getText().toString();
		if (groupName != null) {
			ArrayList<Persion> persionList = (ArrayList<Persion>) map
					.get(groupName);
			Log.i("", "....persionList" + persionList + "....>"
					+ (persionList == null));
			if (persionList == null) {
				Toast.makeText(activity, "所选组下没有人员！", Toast.LENGTH_SHORT)
						.show();
				return arrayValue;
			}
			arrayValue = (Persion[]) (persionList)
					.toArray(new Persion[persionList.size()]);
		} else {
			Toast.makeText(activity, "请先选择监造组！", Toast.LENGTH_SHORT).show();
		}
		return arrayValue;
	}

	public String[] getPersionValues(Persion[] persion) {
		if (persion == null) {
			Toast.makeText(activity, "所选组下没有人员！", Toast.LENGTH_SHORT).show();
			return null;
		}
		int length = persion.length;
		String[] arrayValue = new String[length];
		for (int i = 0; i < length; i++) {
			arrayValue[i] = persion[i].name;
		}
		return arrayValue;
	}

	public String getPoneValues(Persion[] persion, int i) {
		String phoneValue = null;
		phoneValue = persion[i].tel;
		return phoneValue;
	}

	/********************************** end ********************************/

	// 相册弹出框
	Dialog dialog;

	private void showMyDialog(View v) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.show_picselect_dialog, null);

		dialog = new AlertDialog.Builder(activity).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);

		ImageButton photo_cm = (ImageButton) layout.findViewById(R.id.photo_cm);
		ImageButton photo_cdmi = (ImageButton) layout
				.findViewById(R.id.photo_cdmi);
		ImageButton photo_cancel = (ImageButton) layout
				.findViewById(R.id.photo_cancel);
		photo_cm.setOnClickListener(image_ocl);
		photo_cdmi.setOnClickListener(image_ocl);
		photo_cancel.setOnClickListener(image_ocl);

	}

	OnClickListener image_ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.photo_cm) {
				dialog.cancel();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				activity.startActivityForResult(intent, 1);
			} else if (id == R.id.photo_cdmi) {
				dialog.cancel();
				Intent intents = new Intent();
				intents.setType("image/*");
				intents.setAction(Intent.ACTION_GET_CONTENT);
				activity.startActivityForResult(intents, 2);
			} else if (id == R.id.photo_cancel) {
				dialog.cancel();
			}
		}
	};

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	// 每行显示两列数据
	@SuppressLint("NewApi")
	public void fillTableDouble(BasicEntity entity, ArrayList<String> list,
			ViewGroup group) {
		int size = list.size();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout table = new LinearLayout(activity);
		table.setOrientation(LinearLayout.VERTICAL);
		group.addView(table, params);
		LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				linlayWt, linlayH);

		LinearLayout.LayoutParams param_value = new LinearLayout.LayoutParams(
				linlayWv, linlayH);

		LinearLayout.LayoutParams param_pan = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout tablerow;
		TextView textView;
		EditText editText;
		LinearLayout linlay;
		LinearLayout.LayoutParams linlp;
		HorizontalScrollView hslay;
		HorizontalScrollView.LayoutParams hslp;
		ImageView img_line;
		ImageView img_pan;
		LinearLayout lay_pic = null;
		Field field = null;
		LinearLayout lay_double = null;
		int y = 1;
		boolean jo = (size % 2 == 0);
		boolean ishavaeImg = false;
		if (size > 4)
			ishavaeImg = entity.fields.get(list.get(size - 4)).fieldView
					.equals("4");
		for (int i = 0; i < size; i++) {
			field = entity.fields.get(list.get(i));
			img_line = new ImageView(activity);
			img_line.setBackgroundResource(R.drawable.border_spacing);
			table.addView(img_line, tableParams);// TODO
			if (y == 1) {
				lay_double = new LinearLayout(activity);
				y++;
			} else {
				y--;
			}
			tablerow = new LinearLayout(activity);
			tablerow.setOrientation(LinearLayout.HORIZONTAL);
			// 设置最后一行最后两列进行合并单元格
			linlay = new LinearLayout(activity);
			linlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			textView = new TextView(activity);
			textView.setText(field.fieldChName);
			textView.setTextSize(21);
			textView.setTextColor(activity.getResources().getColor(
					R.color.itemtextcolors));
			linlp.gravity = Gravity.CENTER;
			linlp.leftMargin = 30;
			// 判断标题的显示宽度
			if (field.fieldView.equals("4")) {
				LinearLayout.LayoutParams pic_lin_lay = new LinearLayout.LayoutParams(
						200, linlayH);
				pic_lin_lay.gravity = Gravity.CENTER;
				pic_lin_lay.leftMargin = 30;
				linlay.addView(textView);
				tablerow.addView(linlay, pic_lin_lay);
			} else if (field.fieldView.equals("0")) {
				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				keyList.add(field.fieldEnName);
				editList.add(editText);
			} else {
				linlay.addView(textView, linlp);
				tablerow.addView(linlay, rowParams);
			}
			// 2表格显示 输入
			if (field.fieldView.equals("1")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				if (field.fieldChName.contains("行项目号")&&null!=field.fieldContent) {
					int info = Integer.valueOf(field.fieldContent);
					String newinfo = String.valueOf(info);
					editText.setText(newinfo);
				} else if (field.fieldChName.contains("物料编码")&&null!=field.fieldContent) {
					int info = Integer.valueOf(field.fieldContent);
					String newinfo = "" + info;
					editText.setText(newinfo);
				}else if (field.fieldChName.contains("电压等级描述")) {
					textView.setText("电压等级描述");
				} else if (field.fieldChName.contains("线路长度")&&null!=field.fieldContent) {
					String info = field.fieldContent;
					editText.setText(info + "公里");
				}

				if ("计划行号".equals(field.fieldChName)) {
					TextPaint tp = textView.getPaint();
					tp.setFakeBoldText(true);
				}



				if ("11".equals(field.fieldContent) && "state1".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state1".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state2".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state2".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state3".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state3".equals(field.fieldEnName)){
					editText.setText("已提交");
				}


				if ("11".equals(field.fieldContent) && "state4".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state4".equals(field.fieldEnName)){
					editText.setText("已提交");
				}

				if ("11".equals(field.fieldContent) && "state5".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state5".equals(field.fieldEnName)){
					editText.setText("已提交");
				}

				if ("11".equals(field.fieldContent) && "state".equals(field.fieldEnName)) {
					editText.setText("未提交");
				}else if("12".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已确认");
				}else if("18".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已拒绝");
				}else if("17".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已提交");
				}else if ("13".equals(field.fieldContent) && "state".equals(field.fieldEnName)) {
					editText.setText("部分起运");
				}else if("14".equals(field.fieldContent) && "state".equals(field.fieldEnName)){
					editText.setText("已起运");
				}
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);

			}

			else if (field.fieldView.equals("2")) {// 空的需要的输入
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				final EditText edText02 = new EditText(activity);
				
				edText02.setText(field.fieldContent);
				

				if ("计划行号".equals(field.fieldChName)) {
					TextPaint tp = textView.getPaint();
					tp.setFakeBoldText(true);
				}
				edText02.setGravity(Gravity.CENTER_VERTICAL);
				edText02.setTextSize(21);
				edText02.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				
				final String[] isInput = { "true" };
				
				if("jhzcsl".equals(field.fieldEnName)){
					ed1 = Integer.valueOf(field.fieldContent);
				edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
				edText02.addTextChangedListener(new TextWatcher() { 
					@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
					 // TODO Auto-generated method stub 
					 String mString = edText02.getText().toString();
					 
						 mHandler.obtainMessage(0, mString).sendToTarget();
					 } 
					 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					 // TODO Auto-generated method stub 
						 
						 } 
					 @Override public void afterTextChanged(Editable s) { 
					 // TODO Auto-generated method stub

					 } 
					 
					 });
				}
				
				if("sjzcsl1".equals(field.fieldEnName)){
					edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
					edText02.addTextChangedListener(new TextWatcher() { 
						@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
						 // TODO Auto-generated method stub 
						 String mString = edText02.getText().toString();
						 mHandler.obtainMessage(1, mString).sendToTarget();
						 } 
						 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						 // TODO Auto-generated method stub 
							 
							 } 
						 @Override public void afterTextChanged(Editable s) { 
						 // TODO Auto-generated method stub

						 } 
						 
						 });
					
				}
				if("sjzcsl2".equals(field.fieldEnName)){
					edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
					edText02.addTextChangedListener(new TextWatcher() { 
						@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
						 // TODO Auto-generated method stub 
						 String mString = edText02.getText().toString();
						 mHandler.obtainMessage(2, mString).sendToTarget();
						 } 
						 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						 // TODO Auto-generated method stub 
							 
							 } 
						 @Override public void afterTextChanged(Editable s) { 
						 // TODO Auto-generated method stub

						 } 
						 
						 });
					
				}
				if("sjzcsl3".equals(field.fieldEnName)){
					edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
					edText02.addTextChangedListener(new TextWatcher() { 
						@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
						 // TODO Auto-generated method stub 
						 String mString = edText02.getText().toString();
						 mHandler.obtainMessage(3, mString).sendToTarget();
						 } 
						 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						 // TODO Auto-generated method stub 
							 
							 } 
						 @Override public void afterTextChanged(Editable s) { 
						 // TODO Auto-generated method stub

						 } 
						 
						 });
					
				}
				if("sjzcsl4".equals(field.fieldEnName)){
					edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
					edText02.addTextChangedListener(new TextWatcher() { 
						@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
						 // TODO Auto-generated method stub 
						 String mString = edText02.getText().toString();
						 mHandler.obtainMessage(4, mString).sendToTarget();
						 } 
						 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						 // TODO Auto-generated method stub 
							 
							 } 
						 @Override public void afterTextChanged(Editable s) { 
						 // TODO Auto-generated method stub

						 } 
						 
						 });
					
				}
				if("sjzcsl5".equals(field.fieldEnName)){
					edText02.setInputType(InputType.TYPE_CLASS_NUMBER);
					edText02.addTextChangedListener(new TextWatcher() { 
						@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
						 // TODO Auto-generated method stub 
						 String mString = edText02.getText().toString();
						 mHandler.obtainMessage(5, mString).sendToTarget();
						 } 
						 @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						 // TODO Auto-generated method stub 
							 
							 } 
						 @Override public void afterTextChanged(Editable s) { 
						 // TODO Auto-generated method stub

						 } 
						 
						 });
					
				}
				
				edText02.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isInput[0] = "true";
					}
				});
				edText02.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				edText02.addTextChangedListener(new TextWatcher() {
					private CharSequence temp;

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						temp = s;
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						if ("true".equals(isInput[0]) && temp.length() > 18) {
							final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
									activity);
							View view = View.inflate(activity,
									R.layout.edit_text_input, null);
							final EditText ed_value = (EditText) view
									.findViewById(R.id.ed_value);
							ed_value.setText(temp);
							ed_value.setSelection(temp.length());
							Button sure_btn = (Button) view
									.findViewById(R.id.sure_btn);
							Button cancel_btn = (Button) view
									.findViewById(R.id.cancel_btn);

							final AlertDialog alertDialog = dialogBuilder
									.setView(view).create();
							alertDialog.setOnShowListener(new OnShowListener() {
								public void onShow(DialogInterface dialog) {
									InputMethodManager imm = (InputMethodManager) activity
											.getSystemService(activity.INPUT_METHOD_SERVICE);
									imm.showSoftInput(ed_value,
											InputMethodManager.SHOW_IMPLICIT);
								}
							});
							alertDialog.show();
							alertDialog.setCanceledOnTouchOutside(false);
							sure_btn.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									isInput[0] = "false";
									String value_ = ed_value.getText()
											.toString();
									edText02.setText(value_);
									edText02.setSelection(value_.length());
									alertDialog.cancel();
								}
							});
							cancel_btn
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											alertDialog.cancel();
										}
									});
						}
					}
				});

				edText02.setTextSize(21);
				keyList.add(field.fieldEnName);
				editList.add(edText02);
				linlp.gravity = Gravity.CENTER_VERTICAL;
				img_pan = new ImageView(activity);
				param_pan.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(img_pan, param_pan);
				linlay.addView(edText02, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
				// 3单选框
			} else if (field.fieldView.equals("3")) {
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
				final EditText edit = new EditText(activity);
				edit.setTextSize(21);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				linlp.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(edit, linlp);
				final Field fld = field;
				String values = field.fieldContent;
				keyList.add(field.fieldEnName);
				if (field.fieldChName.contains("是否为重点物资")) {
					textView.setText("是否为重点物资");
				}
				editList.add(edit);
				if (values.contains("#")) {
					edit.setText(values.split("#")[0]);
					values = values.split("#")[1];
				}
				final String[] valuesArray = values.split(",");
				edit.setFocusable(false);
				edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(activity);
						builder.setTitle(fld.fieldChName)
								.setIcon(R.drawable.ic_launcher)
								.setCancelable(true)
								.setSingleChoiceItems(valuesArray, 0,
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												edit.setText(valuesArray[which]);
												dialog.cancel();
											}
										});
						AlertDialog dlg = builder.create();
						dlg.show();
					}
				});
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(img_pan, param_pan);
				tablerow.addView(linlay, param_value);
				// 4图片
			} else if (field.fieldView.equals("4")) {
				if (countpic == 0) {
					lay_pic = new LinearLayout(activity);
					lay_pic.setOrientation(LinearLayout.HORIZONTAL);
				}
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						linlayWpic, linlayHi);

				ImageView image = new ImageView(activity);
				image.setBackgroundResource(R.drawable.add_photo);
				image.setTag(field.fieldEnName);
				if (field.fieldContent.trim() != null) {
					Log.d("FillTableNew", "..........下载图片路径....>>"
							+ field.fieldContent);
					// 返回路径 进行加载
					// imageLoader.displayImage("http://127.0.0.1:8553"
					// + field.fieldContent, image, animateFirstListener);
					// 进行图片解析的操作
					// Bitmap bm=stringToBitmap(field.fieldContent);
					// image.setImageBitmap(bm);

				}
				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((com.epsmart.wzdp.activity.CommonActivity) activity)
								.setTag(v.getTag().toString());
						showMyDialog(v);
					}
				});
				imageMap.put(field.fieldEnName, image);
				tag++;
				lp.leftMargin = 1;
				lay_pic.addView(image, lp);
				if (countpic == 3) {
					tablerow.addView(lay_pic);
					countpic = 0;
				} else {
					countpic++;
					continue;
				}
				// 5选择时间弹出框
			} else if (field.fieldView.equals("5")) {
				final EditText edit = new EditText(activity);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				edit.setGravity(Gravity.CENTER_VERTICAL);
				edit.setTextSize(21);
				edit.setHeight(linlayHi);
				edit.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));

				keyList.add(field.fieldEnName);
				editList.add(edit);
				linlay.addView(edit, linlp);

				if (field.fieldContent != null) {
					edit.setText(field.fieldContent);
				}
				edit.setFocusable(false);
				// edit.setBackgroundColor(Color.WHITE);
				edit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectDateNew(v);//
					}
				});
				// linlay.addView(lay);
				img_pan = new ImageView(activity);
				img_pan.setBackgroundResource(R.drawable.pan);
				param_pan.gravity = Gravity.CENTER_VERTICAL;
				linlay.addView(img_pan, param_pan);
				tablerow.addView(linlay, param_value);
			} else if (field.fieldView.equals("6")) {
				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWv, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

				editText = new EditText(activity);
				editText.setText(field.fieldContent);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setTextSize(21);
				editText.setEnabled(false);
				editText.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				editText.setBackgroundColor(Color.WHITE);
				keyList.add(field.fieldEnName);
				editList.add(editText);
				linlp.gravity = Gravity.CLIP_VERTICAL;
				linlay.addView(editText, linlp);
				hslay.addView(linlay, hslp);
				tablerow.addView(hslay, param_value);
			}

			lay_double.addView(tablerow);
			// 有照片的时候

			if (y == 1) {
				table.addView(lay_double, tableParams);
			}

			if (ishavaeImg) {// 有照片
				//
				if (!jo && (i == size - 5) && y == 2 || !jo && (i == size - 1)
						&& y == 2) {
					table.addView(lay_double, tableParams);
				}
			} else {// 无照片
				if (!jo && (i == size - 1) && y == 2) {
					table.addView(lay_double, tableParams);
				}
			}
		}
		img_line = new ImageView(activity);
		img_line.setBackgroundResource(R.drawable.border_spacing);
		table.setBackgroundColor(Color.WHITE);
		table.addView(img_line, tableParams);
		// btn_lay.setVisibility(View.VISIBLE);
	}
	
	
	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String ss = (String)msg.obj;
				if(!"".equals(ss)){
				ed2= Integer.valueOf(ss);
				 if(ed1 < ed2){
						Tdialog();
					}
				}
				break;
			case 1:
				String ss1 = (String)msg.obj;
				if(!"".equals(ss1)){
				 etext1= Integer.valueOf(ss1);
				 if(etext1>ed1){
						Sdialog();
			 }
				}
				break;
			case 2: 
				String ss2 = (String)msg.obj;
				if(!"".equals(ss2)){
				etext2= Integer.valueOf(ss2);
				 if((etext1+etext2)>ed1){
						Sdialog();
				 }
			 }
				break;
			case 3: 
				String ss3 = (String)msg.obj;
				if(!"".equals(ss3)){
				etext3= Integer.valueOf(ss3);
				 if((etext1+etext2+etext3)>ed1){
						Sdialog();
				 }
			 }
				break;
			case 4: 
				String ss4 = (String)msg.obj;
				if(!"".equals(ss4)){
				etext4= Integer.valueOf(ss4);
				 if((etext1+etext2+etext3+etext4)>ed1){
						Sdialog();
				 }
			 }
				break;
			case 5: 
				String ss5 = (String)msg.obj;
				if(!"".equals(ss5)){
				etext5= Integer.valueOf(ss5);
				 if((etext1+etext2+etext3+etext4+etext5)>ed1){
						Sdialog();
				 }
			 }	
				break;
				
			default:
				break;
			}
		}
	}
	
	
	// 原来的测试

	public void fillTableTest(BasicEntity entity, ArrayList<String> list,
			ViewGroup group) {

		int size = list.size();
		int y = 2;
		boolean jo = (size % 2 == 0);//
		int x = jo ? (size / 2) : (size / 2 + 1);
		Log.i("", ".奇偶.jo=" + jo + ". ....列..y=" + y + "...行..x=" + x
				+ ".......总数..size=" + size);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout table = new LinearLayout(activity);
		table.setOrientation(LinearLayout.VERTICAL);
		params.rightMargin = 22;
		params.leftMargin = 22;
		group.addView(table, params);

		LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
				linlayWt, linlayH);
		LinearLayout.LayoutParams rowParams_img = new LinearLayout.LayoutParams(
				linlayWt, linlayHi);
		LinearLayout.LayoutParams param_value = new LinearLayout.LayoutParams(
				linlayWv, linlayH);
		LinearLayout.LayoutParams param_line = new LinearLayout.LayoutParams(1,
				linlayH);
		LinearLayout.LayoutParams param_line_img = new LinearLayout.LayoutParams(
				1, linlayHi);
		LinearLayout tablerow;
		TextView textView;
		EditText editText;
		LinearLayout linlay;
		LinearLayout.LayoutParams linlp;
		HorizontalScrollView hslay;
		HorizontalScrollView.LayoutParams hslp;
		ImageView img_line;

		Field field = null;
		for (int i = 0; i < x; i++) {
			img_line = new ImageView(activity);
			img_line.setBackgroundResource(R.drawable.rec_boder_hang);
			table.addView(img_line, tableParams);
			tablerow = new LinearLayout(activity);
			tablerow.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < y; j++) {
				if ((i * y + j) <= size - 1)
					field = entity.fields.get(list.get(i * y + j));
				if (j == 0) {
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					// 默认设置四张图片
					if (field.fieldView.equals("4") || (!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
				}
				// 设置最后一行最后两列进行合并单元格
				if ((i * y + j) > size - 1) {
					if (!jo) {
						textView = new TextView(activity);
						textView.setWidth(491);
						tablerow.addView(textView);
						img_line = new ImageView(activity);
						img_line.setBackgroundResource(R.drawable.rec_boder_lie);
						if (field.fieldView.equals("4")) {
							tablerow.addView(img_line, param_line_img);
						} else {
							tablerow.addView(img_line, param_line);
						}
					}
					continue;
				}

				hslay = new HorizontalScrollView(activity);
				hslp = new HorizontalScrollView.LayoutParams(linlayWt, linlayH);
				hslay.setHorizontalScrollBarEnabled(false);
				linlay = new LinearLayout(activity);
				linlp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				textView = new TextView(activity);
				textView.setText(field.fieldChName);
				textView.setTextSize(21);
				textView.setTextColor(activity.getResources().getColor(
						R.color.itemtextcolor));
				// textView.setSingleLine(true);
				// textView.setSelected(true);
				// textView.setEllipsize(TruncateAt.MARQUEE);
				// textView.setBackgroundResource(R.drawable.rec_bg_green2);
				linlp.gravity = Gravity.CENTER;
				linlay.addView(textView, linlp);
				hslay.addView(linlay, hslp);

				img_line = new ImageView(activity);
				img_line.setBackgroundResource(R.drawable.rec_boder_lie);
				if (field.fieldView.equals("4") || (!jo && i == (x - 3))) {
					tablerow.addView(hslay, rowParams_img);
					tablerow.addView(img_line, param_line_img);
				} else {
					tablerow.addView(hslay, rowParams);
					tablerow.addView(img_line, param_line);
				}
				// 2表格显示 输入
				if (!TextUtils.isEmpty(field.fieldContent)
						&& field.fieldView.equals("2")) {
					hslay = new HorizontalScrollView(activity);
					hslp = new HorizontalScrollView.LayoutParams(linlayWv,
							linlayH);
					hslay.setHorizontalScrollBarEnabled(false);
					linlay = new LinearLayout(activity);
					linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

					editText = new EditText(activity);
					editText.setText(field.fieldContent);
					editText.setGravity(Gravity.CENTER_VERTICAL);
					editText.setTextSize(21);
					editText.setTextColor(activity.getResources().getColor(
							R.color.itemtextcolor));
					editText.setBackgroundColor(Color.WHITE);
					keyList.add(field.fieldEnName);
					editList.add(editText);
					linlp.gravity = Gravity.CLIP_VERTICAL;
					linlay.addView(editText, linlp);
					hslay.addView(linlay, hslp);
					tablerow.addView(hslay, param_value);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					if ((!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
				} else if (field.fieldView.equals("2")
						&& TextUtils.isEmpty(field.fieldContent)) {
					hslay = new HorizontalScrollView(activity);
					hslp = new HorizontalScrollView.LayoutParams(linlayWv,
							linlayH);
					hslay.setHorizontalScrollBarEnabled(false);
					linlay = new LinearLayout(activity);
					linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
					editText = new EditText(activity);
					editText.setTextColor(activity.getResources().getColor(
							R.color.itemtextcolor));
					editText.setBackgroundColor(Color.WHITE);

					editText.setTextSize(21);
					keyList.add(field.fieldEnName);
					editList.add(editText);
					linlp.gravity = Gravity.CENTER_VERTICAL;
					linlay.addView(editText, linlp);
					hslay.addView(linlay, hslp);
					tablerow.addView(hslay, param_value);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					if ((!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
					// 3单选框
				} else if (field.fieldView.equals("3")) {

					hslay = new HorizontalScrollView(activity);
					hslp = new HorizontalScrollView.LayoutParams(linlayWv,
							linlayH);
					hslay.setHorizontalScrollBarEnabled(false);
					linlay = new LinearLayout(activity);
					linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);
					final EditText edit = new EditText(activity);
					edit.setBackgroundColor(Color.WHITE);
					edit.setTextSize(21);
					edit.setTextColor(activity.getResources().getColor(
							R.color.itemtextcolor));
					linlp.gravity = Gravity.CENTER_VERTICAL;
					linlay.addView(edit, linlp);
					hslay.addView(linlay, hslp);
					final Field fld = field;
					String values = field.fieldContent;
					keyList.add(field.fieldEnName);
					editList.add(edit);
					if (values.contains("#")) {
						edit.setText(values.split("#")[0]);
						values = values.split("#")[1];
					}
					final String[] valuesArray = values.split(",");
					edit.setFocusable(false);
					edit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new Builder(activity);
							builder.setTitle(fld.fieldChName)
									.setIcon(R.drawable.ic_launcher)
									.setCancelable(true)
									.setSingleChoiceItems(
											valuesArray,
											0,
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													edit.setText(valuesArray[which]);
													dialog.cancel();
												}
											});
							AlertDialog dlg = builder.create();
							dlg.show();
						}
					});
					tablerow.addView(hslay, param_value);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					if ((!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
					// 4图片
				} else if (field.fieldView.equals("4")) {
					LinearLayout lay = new LinearLayout(activity);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							linlayWv, linlayHi);

					ImageView image = new ImageView(activity);
					image.setBackgroundResource(R.drawable.photos);
					image.setTag(field.fieldEnName);

					if (field.fieldContent.trim() != null) {

						imageLoader.displayImage("http://127.0.0.1:8553"
								+ field.fieldContent, image,
								animateFirstListener);
					}
					lay.addView(image, lp);
					image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							((com.epsmart.wzdp.activity.CommonActivity) activity)
									.setTag(v.getTag().toString());
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							activity.startActivityForResult(intent, 1);
						}
					});

					tablerow.addView(lay, lp);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					tablerow.addView(img_line, param_line_img);
					imageMap.put(field.fieldEnName, image);
					tag++;
					// 5选择时间弹出框
				} else if (field.fieldView.equals("5")) {
					LinearLayout lay = new LinearLayout(activity);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							linlayW, linlayHi);
					final EditText edit = new EditText(activity);

					edit.setGravity(Gravity.CENTER_VERTICAL);
					edit.setTextSize(21);
					edit.setHeight(linlayHi);
					edit.setTextColor(activity.getResources().getColor(
							R.color.itemtextcolor));

					keyList.add(field.fieldEnName);
					editList.add(edit);
					lp.gravity = Gravity.CENTER;
					lay.addView(edit, lp);

					if (field.fieldContent != null) {
						edit.setText(field.fieldContent);
					}
					edit.setFocusable(false);
					edit.setBackgroundColor(Color.WHITE);
					edit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectDate(v);
						}
					});
					tablerow.addView(lay, param_value);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					if ((!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
				} else if (field.fieldView.equals("6")) {
					hslay = new HorizontalScrollView(activity);
					hslp = new HorizontalScrollView.LayoutParams(linlayWv,
							linlayH);
					hslay.setHorizontalScrollBarEnabled(false);
					linlay = new LinearLayout(activity);
					linlp = new LinearLayout.LayoutParams(linlayWv, linlayH);

					editText = new EditText(activity);
					editText.setText(field.fieldContent);
					editText.setGravity(Gravity.CENTER_VERTICAL);
					editText.setTextSize(21);
					editText.setFocusable(false);
					editText.setTextColor(activity.getResources().getColor(
							R.color.itemtextcolor));
					editText.setBackgroundColor(Color.WHITE);
					keyList.add(field.fieldEnName);
					editList.add(editText);
					linlp.gravity = Gravity.CLIP_VERTICAL;
					linlay.addView(editText, linlp);
					hslay.addView(linlay, hslp);
					tablerow.addView(hslay, param_value);
					img_line = new ImageView(activity);
					img_line.setBackgroundResource(R.drawable.rec_boder_lie);
					if ((!jo && i == (x - 3)))
						tablerow.addView(img_line, param_line_img);
					else
						tablerow.addView(img_line, param_line);
				}

			}
			table.addView(tablerow, tableParams);
		}
		img_line = new ImageView(activity);
		img_line.setBackgroundResource(R.drawable.rec_boder_hang);
		table.setBackgroundColor(Color.WHITE);
		table.addView(img_line, tableParams);
		btn_lay.setVisibility(View.VISIBLE);
	}

}
