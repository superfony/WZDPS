package com.epsmart.wzdp.activity.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import Decoder.BASE64Encoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;
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
 */

public class FillTableHelp {
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
	private int linlayW = 400;// 表格显示区域宽度
	private int linlayHi = 140;// 图片显示的区域高度
	private int linlayWv = 290;// 图片显示的宽度、表单值区域显示区域宽度
	private int linlayH = 70;// 表格高度
	private int linlayWt = 200;// 标题显示区域宽度
	
	
	public ArrayList<String> txtShowList;
	private BasicEntity entity;

	public void setTxtShowList(ArrayList<String> txtShowList) {
		this.txtShowList = txtShowList;
	}

	// private ImageView image;
	public LinearLayout getBtn_lay() {
		return btn_lay;
	}

	public void setBtn_lay(LinearLayout btn_lay) {
		this.btn_lay = btn_lay;
	}

	public FillTableHelp(Activity activity, ViewGroup group) {
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
	public void fillTable(BasicEntity entity, ArrayList<String> list) {
		this.entity=entity;
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
                     Log.w("fillTableHelp","获取拍照图片路径="+field.fieldContent);
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

	/************************************************************************/
	// 存储监造组 和监造师

	private String[] jgroup = null;
	private HashMap<String, ArrayList<Persion>> jgroupmap = null;
	private EditText phoneEd;
	private EditText groupEd;
	private static int positoin;

	class Persion {
		String name;
		String tel;
	}

	public void fillTableGroup(BasicEntity entity, ArrayList<String> list) {
		this.entity=entity;
		int size = list.size();
		int y = 2;
		boolean jo = (size % 2 == 0);//
		int x = jo ? (size / 2) : (size / 2 + 1);
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
						Log.i("",
								".........................supervisorname...............................》》");
						jgroupmap = getGroupInfo(values.split("#")[1]);
						edit.setText(values.split("#")[0]);
						edit.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								AlertDialog.Builder builder = new Builder(
										activity);
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
								AlertDialog.Builder builder = new Builder(
										activity);
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

						imageLoader.displayImage("http://127.0.0.1:8551"
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
					if (field.fieldEnName.equals("supervisorphone")) {
						phoneEd = editText;
					}

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
		strb.append(getTextParams());
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
		strb.append(getTextParams());
		strb.append("</opdetail>");
		return strb.toString();

	}
	
	public String getTextParams(){
		StringBuffer strb = new StringBuffer();
		Log.i("", "......txtShowList>>"+txtShowList);
		if(txtShowList==null){
			return null;
		}
		for(int i=0;i<txtShowList.size();i++){
			Field field=entity.fields.get(txtShowList.get(i));
			
			strb.append("<param>").append("<key>").append(txtShowList.get(i))
			.append("</key>").append("<value>")
			.append(field.fieldContent).append("</value>")
			.append("</param>");
			
		}
		
		return strb.toString();
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
				et.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			}
		};
		Calendar calendar = Calendar.getInstance();
		Dialog dialog = new DatePickerDialog(activity, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
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
		int newWidth = linlayWv;
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



}
