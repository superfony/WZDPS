package com.epsmart.wzdp.activity.dailyoffice.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Decoder.BASE64Decoder;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyPerson;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyPersonHandler;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyPersonPro;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.Person;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.StatusEntity;

/*
 * 在线交流--新建帖子界面
 */
public class DailyOnlineAct extends DailyOnlineCommonAct {
	public String req = null;
	private EditText daily_contact, daily_theme, daily_abstract;
	private LinearLayout button_adds;
	private List<String> group; // 组列表
	private List<ArrayList<HashMap<String, String>>> child; // 子列表
	private List<DailyPerson> list = new ArrayList<DailyPerson>();
	private FixedGridLayout fdlay;
	private ArrayList<DailyPerson> pros = new ArrayList<DailyPerson>();
	private ContactsInfoAdapter contactsAdapter;
	
	private Handler changelay=new Handler(){

		@Override
		public void handleMessage(Message msg) {
             addBtn((Person)msg.obj);

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_online_post);
		initview();
		contactsAdapter=new ContactsInfoAdapter();
		getExpandableListView().setAdapter(contactsAdapter);
		getExpandableListView().setCacheColorHint(0); // 设置拖动列表的时候防止出现黑色背景
		RequestPram requestPram = new RequestPram();
		loadData(requestPram);
		
		getExpandableListView().setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int group,
					int child, long arg4){
				Person person=pros.get(group).person.get(child);
				changelay.obtainMessage(0, person).sendToTarget();
				return true;
			}
		});

	}

	public void loadData(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.getPersionList;
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DailyPersonHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			Message msg = new Message();
			if (parseObj instanceof DailyPersonPro) {

				list = ((DailyPersonPro) parseObj).list;
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
			if (parseObj instanceof StatusEntity) {
				msg.obj = "服务异常";
				mHandler.sendMessage(msg);
			}
		}
	}

	protected FlowHandler mHandler = new FlowHandler();

	public class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
//			ArrayList<HashMap<String, String>> pnames = new ArrayList<HashMap<String, String>>();
			pros.addAll((ArrayList<DailyPerson>) msg.obj);
			contactsAdapter.notifyDataSetChanged();
		}
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
		return parseRound(
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length), 60);

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




	class ContactsInfoAdapter extends BaseExpandableListAdapter {
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return pros.get(groupPosition).person.get(childPosition);
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return pros.get(groupPosition).person.size();
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			return getGenericView(pros.get(groupPosition).person
					.get(childPosition));
		}
		@Override
		public Object getGroup(int groupPosition) {
			return pros.get(groupPosition).depname;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return pros.size();
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String string = pros.get(groupPosition).depname;
			return getGenericView(string);
		}

		// 创建组/子视图
		public View getGenericView(String s) {
			LinearLayout lay = new LinearLayout(DailyOnlineAct.this);
			lay.setOrientation(LinearLayout.HORIZONTAL);
			ImageView image = new ImageView(DailyOnlineAct.this);
			lay.addView(image);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 80);
			TextView text = new TextView(DailyOnlineAct.this);
			text.setLayoutParams(lp);
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			text.setPadding(36, 0, 0, 0);
			text.setTextSize(18);
			text.setText(s);
			lay.addView(text);
			return lay;
		}

		public View getGenericView(Person s) {

			LinearLayout lay = new LinearLayout(DailyOnlineAct.this);
			lay.setOrientation(LinearLayout.HORIZONTAL);
			ImageView image = new ImageView(DailyOnlineAct.this);
			image.setVisibility(View.GONE);
			String iamgestr = s.persionimg;
			if (!TextUtils.isEmpty(iamgestr)) {
				image.setImageBitmap(stringToBitmap(iamgestr));
			} else {

			}

			lay.addView(image);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 80);
			TextView text = new TextView(DailyOnlineAct.this);
			text.setLayoutParams(lp);
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			text.setPadding(36, 0, 0, 0);
			text.setTextSize(18);
			text.setText(s.persionname);
			lay.addView(text);
			return lay;
		}
		@Override
		public boolean hasStableIds() {
			return false;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	public void initview() {
		search_lay = (RelativeLayout) getActionBar().getCustomView()
				.findViewById(R.id.search_lay);
		search_btn = (Button) getActionBar().getCustomView().findViewById(
				R.id.search_btn);

		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.daily_online_title);

		fdlay = (FixedGridLayout) findViewById(R.id.navigation);
		daily_theme = (EditText) findViewById(R.id.daily_theme);
		daily_abstract = (EditText) findViewById(R.id.daily_abstract);
		search_lay.setVisibility(View.VISIBLE);
		search_btn.setOnClickListener(clickLis);
		search_btn.setBackgroundResource(R.drawable.daily_release_item);
		search_btn.setOnTouchListener(new Button.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundResource(R.drawable.daily_release_itemchange);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundResource(R.drawable.daily_release_item);
				}
				return false;
			}
		});

		search_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int count=fdlay.getChildCount();
				Button btn;
				Person person;
				StringBuffer stringbuffer=new StringBuffer();
				
				if(count>0){
					for(int i=0;i<count;i++){
						btn=(Button)fdlay.getChildAt(i);
						stringbuffer.append(((Person)btn.getTag()).persionid).append("#");
					}
				}

				req = RequestXmlHelp.getCommonXML(RequestXmlHelp
						.getReqXML("persions",
								stringbuffer.substring(0, stringbuffer.length()-1).toString())
						.append(RequestXmlHelp.getReqXML("subtitle",
								daily_theme.getText().toString()))
						.append(RequestXmlHelp.getReqXML("subcontent",
								daily_abstract.getText().toString()))
				// .append(RequestXmlHelp.getReqXML("filepath",
				// filepath))
						);
				RequestPram requestPram = new RequestPram();
				submitMethod(requestPram, req);

			}
		});
	}

	private void addBtn(Person person) {
		int length = fdlay.getChildCount();
		Button btn = new Button(this);
		
		btn.setText(person.persionname);
		btn.setTag(person);
		btn.setBackgroundResource(R.drawable.clearwords_bg_sel);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fdlay.removeView(view);
			}
		});
		fdlay.addView(btn, length);

	}

	// 提交
	protected void submitMethod(RequestPram requestPram, String req) {
		requestPram.bizId = 1004;
		requestPram.password = "password12";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.buildSub;
		requestPram.param = req;
		super.submitMethod(requestPram);
	}
}
