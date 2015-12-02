package com.epsmart.wzdp.activity.achar.service;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.view.FonyHScrollView;

public class TableViewShow extends AbstractChart {
	private LinearLayout table;
	private Context context;
	private String title;
	/** 数据列表 **/
	private ArrayList<ArrayList<String>> lists;
	private View view;

	public TableViewShow(Context context, String title) {
		this.context = context;
		this.title = title;
	}

	public void execute(BasicEntity entity) {
		super.execute(entity);
	}

	public void setTableData() {

	}

	/**
	 * 添加每行数据和背景
	 */
	@SuppressLint("NewApi")
	public View getview() {
		Log.d("TableViewShow()", ".......getView()..view=" + view);
		if (view == null)
			view = View.inflate(context, R.layout.table, null);
		table = (LinearLayout) view.findViewById(R.id.tablelayout);
		LinearLayout title_row = (LinearLayout) view
				.findViewById(R.id.title_row);
		FonyHScrollView hscroll_title = (FonyHScrollView) view
				.findViewById(R.id.hscroll_title);
		FonyHScrollView hscroll_content = (FonyHScrollView) view
				.findViewById(R.id.hscroll_content);

		hscroll_title.setAnotherView(hscroll_content);
		hscroll_content.setAnotherView(hscroll_title);

		LinearLayout.LayoutParams row_lp = new LinearLayout.LayoutParams(190,
				56);
		LinearLayout.LayoutParams tablerow_lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout tablerow = null;
		TextView textView = null;
		int count;// 列数小于10，填充整个屏幕 大于显示可滑动
		List<String[]> list = charBean.getyTableValue();
		List<double[]> listx = charBean.getxValue();

		tablerow = new LinearLayout(context);
		tablerow.setLayoutParams(new LinearLayout.LayoutParams(1280,
				LayoutParams.WRAP_CONTENT));// 设置表格的行高
		tablerow.setOrientation(LinearLayout.HORIZONTAL);
		textView = new TextView(context);
		textView.setText("");// 行标题
		setTextView(textView, 15);
		tablerow.addView(textView, row_lp);

		count = listx.get(0).length;// 获取列数
		for (int i = 0; i < count; i++) {
			textView = new TextView(context);
			String text = "";
			if (i < charBean.getLineTitle().length) {
				text = charBean.getLineTitle()[i];
			}
			textView.setText(text);// 行标题
			setTextView(textView, 15);
			tablerow.addView(textView, row_lp);
		}
		tablerow.setBackgroundColor(Color.parseColor("#ff5555"));

		if (count > 6) {
			title_row.addView(tablerow, tablerow_lp);
		} else {
			title_row.addView(tablerow);
		}

		for (int i = 0; i < list.size(); i++) {
			String point[] = list.get(i);
			tablerow = new LinearLayout(context);
			tablerow.setLayoutParams(new LinearLayout.LayoutParams(1280,
					LayoutParams.WRAP_CONTENT));// 设置表格的行高
			tablerow.setOrientation(LinearLayout.HORIZONTAL);
			textView = new TextView(context);

			String bText = "";
			if (i < charBean.getbTitles().length)
				bText = charBean.getbTitles()[i];
			textView.setText(bText);// 添加列标题
			tablerow.addView(textView, row_lp);
			textView = null;
			int colNum = point.length;
			for (int j = 0; j < colNum; j++) {
				textView = new TextView(context);
				setTextView(textView, 13);
				textView.setText(point[j]);// 数据项
				tablerow.addView(textView, row_lp);
				textView = null;
			}
			if (i % 2 == 0) {
				tablerow.setBackgroundColor(Color.WHITE);
			} else {
				tablerow.setBackgroundColor(Color.parseColor("#ffddac"));
			}

			if (count > 6) {
				table.addView(tablerow, tablerow_lp);
				tablerow = null;
			} else {
				table.addView(tablerow);
				tablerow = null;
			}

		}
		return view;
	}

	private void setTextView(TextView textView, float size) {
		textView.setPadding(3, 0, 0, 0);
		textView.setGravity(Gravity.CENTER);// 居中
		textView.setTextSize(size);
		textView.setTextColor(Color.BLACK);
		textView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		textView.setClickable(true);
		textView.setFocusable(true);
	}

	/**
	 * 数据的排序
	 */
	public void dataSort(int coloumNumber, boolean isDown) {
		if (lists == null) {
			return;
		}
		if (coloumNumber > lists.size())
			return;
		ArrayList<String> couStatistics = new ArrayList<String>();
		for (int i = 1; i < lists.size(); i++) {
			for (int j = 1; j < lists.size() - i; j++) {
				if (isDown) {
					// 降序
					if (Integer.valueOf(lists.get(j).get(coloumNumber)) < Integer
							.valueOf(lists.get(j + 1).get(coloumNumber))) {
						couStatistics = lists.get(j);
						lists.remove(j);
						lists.add(j + 1, couStatistics);
					}
				} else {
					// 排序
					if (Integer.valueOf(lists.get(j).get(coloumNumber)) > Integer
							.valueOf(lists.get(j + 1).get(coloumNumber))) {
						couStatistics = lists.get(j);
						lists.remove(j);
						lists.add(j + 1, couStatistics);
					}
				}
			}
		}
	}

}