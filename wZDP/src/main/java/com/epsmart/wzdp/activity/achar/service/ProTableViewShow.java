package com.epsmart.wzdp.activity.achar.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.search.QueryPlanDialog;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.view.MyHScrollView;
import com.epsmart.wzdp.view.MyHScrollView.OnScrollChangedListener;
public class ProTableViewShow extends AbstractChart {
//	private LinearLayout table;
	private Context context;
	private String title;
	
	
	private ListView mListView1;//
	private RelativeLayout mHead;//
	private LinearLayout title_relalay;
	private MyAdapter myAdapter;
	private HorizontalScrollView headSrcrollView;
	private List<String[]> als = new ArrayList<String[]>();

	private TextView tv;
	private View view;
	private TextView tv01;
	
	
//	/** 数据列表 **/
//	private ArrayList<ArrayList<String>> lists;
//	private View view;
//	
//
	public ProTableViewShow(Context context, String title) {
		this.context = context;
		this.title = title;
	}
//
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
		LinearLayout.LayoutParams row_lp = new LinearLayout.LayoutParams(190,
				56);
		List<String[]> als = charBean.getyTableValue();// 值
		String[] titles=charBean.getLineTitle();
		view = View.inflate(context, R.layout.projecttable, null);
		//tv = (TextView) view.findViewById(R.id.show_title);
//		tv.setText(title);
		//tv.setTextSize(18);
		mHead = (RelativeLayout) view.findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		
		headSrcrollView = (HorizontalScrollView) mHead
				.findViewById(R.id.horizontalScrollView1);
		headSrcrollView.setHorizontalFadingEdgeEnabled(false);
		headSrcrollView.scrollTo(0, 0);
		// 设置行标题的
		tv01 = (TextView) mHead.findViewById(R.id.textView01);
		tv01.setTextSize(15);
		tv01.setText(titles[0]);
		title_relalay = (LinearLayout) mHead.findViewById(R.id.title_relalay);
		for (int i = 1; i < titles.length; i++) {
			TextView tv = new TextView(context);
			tv.setText(titles[i]);
			tv.setTextSize(15);
			title_relalay.addView(tv, row_lp);
		}
		
		
		mListView1 = (ListView) view.findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		myAdapter = new MyAdapter(context, R.layout.list_item_oneline, als);
		mListView1.setAdapter(myAdapter);
		return view;
	}
	
	

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	/**
	 * 
	 */
	public class MyAdapter extends BaseAdapter {
		public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();

		int id_row_layout;
		LayoutInflater mInflater;
		private List<String[]> list;
		private Context context;

		public MyAdapter(Context context, int id_row_layout, List<String[]> list) {
			super();
			this.id_row_layout = id_row_layout;
			this.list = list;
			mInflater = LayoutInflater.from(context);
			this.context = context;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentView) {
			ViewHolder holder = null;
			String[] svalue = list.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(id_row_layout, null);

				LinearLayout linlay = (LinearLayout) convertView
						.findViewById(R.id.value_linlay);
				holder = new ViewHolder(svalue.length, linlay, context);
				holder.txt1 = (TextView) convertView
						.findViewById(R.id.textView1);
				
				MyHScrollView scrollView1 = (MyHScrollView) convertView
						.findViewById(R.id.listHorizontalScrol);
				holder.scrollView = scrollView1;

				((MyHScrollView) headSrcrollView)
						.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
								scrollView1));
				convertView.setTag(holder);
				mHolderList.add(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position % 2 == 0) {
				convertView.setBackgroundColor(Color.WHITE);
			} else {
				convertView.setBackgroundColor(Color.parseColor("#ffddac"));
			}

			final String[] args = list.get(position);
			holder.txt1.setText(args[0]);
			holder.putvalue(args);
			return convertView;
		}

		class OnScrollChangedListenerImp implements OnScrollChangedListener {
			MyHScrollView mScrollViewArg;

			public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
				mScrollViewArg = scrollViewar;
			}

			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				mScrollViewArg.smoothScrollTo(l, t);
			}
		};

		class ViewHolder {
			TextView txt1;
			TextView[] textViewArray;
			HorizontalScrollView scrollView;

			ViewHolder(int length, LinearLayout linearlay, Context context) {
				textViewArray = new TextView[length];
				TextView tv = null;
				LinearLayout.LayoutParams row_lp = new LinearLayout.LayoutParams(
						190, 56);

				for (int i = 1; i < length; i++) {
					tv = new TextView(context);
					linearlay.addView(tv, row_lp);
					tv.setTextSize(15);
					textViewArray[i] = tv;

				}
			}

			public void putvalue(String[] args) {
				for (int i = 1; i < args.length; i++) {
					textViewArray[i].setText(args[i]);

				}
			}
		}
	}
	
	/**
	 * 绘制图表
	 * 
	 * @param response
	 */
	private ArrayList<BasicEntity> list;

	protected List<String[]> getStringList(String str) {
		List<String[]> values = new ArrayList<String[]>();
		if (str == null)
			return null;

		String array[] = str.split("#");
		for (int i = 0; i < array.length; i++) {
			values.add(array[i].split(","));
		}
		return values;
	}
	
	
}