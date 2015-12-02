package com.epsmart.wzdp.activity.more;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.db.dao.DaoManager;
import com.epsmart.wzdp.db.table.SubmitDateTable;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.j256.ormlite.dao.Dao;

public class OffLineData extends OffLineCommonAct {

	private ListView listView;
	private OffLineDataAdapter adapter;
	private List<SubmitDateTable> list = new ArrayList<SubmitDateTable>();
	private Activity activity;
	private SubmitDateTable resp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.off_line_data);
		activity = this;
		initUI();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.off_line_list);
		adapter = new OffLineDataAdapter(activity, getOffData());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				boolean isOnline = ((AppContext) activity.getApplication()).isOnline;// 当前网络状况
				if (isOnline) {
					resp = (SubmitDateTable) adapter.getItem(position);
					RequestPram requestPram = new RequestPram();
					requestPram.methodName = resp.methodName;
					requestPram.param = resp.param;
					requestPram.userName = Integer.valueOf(resp.userName);
					RequestAction requestAction = new RequestAction();
					requestAction.reset();
					requestAction.setReqPram(requestPram);
                   //默认的解析方式
					httpModule.executeRequest(requestAction,
							new DefaultSaxHandler(), new ProcessResponse(),
							RequestType.THRIFT);
					// listView.removeView(v);
					// list.remove(position);
					// adapter.notifyDataSetChanged();
				}
			}
		});
	}

	/* 处理表单提交返回消息 */
	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			}
		}
	}

	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(activity, (String) msg.obj + "",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				StatusEntity se = (StatusEntity) msg.obj;
				Toast.makeText(activity, se.message + "", Toast.LENGTH_LONG)
						.show();
				DaoManager<SubmitDateTable> dm = (DaoManager<SubmitDateTable>) DaoManager.getInstance();
				Dao<SubmitDateTable, Integer> dao = dm.getDao(SubmitDateTable.class);
				try {
					dao.delete(resp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				list.remove(resp);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;

			}
		}
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private List<SubmitDateTable> getOffData() {
		DaoManager<SubmitDateTable> dm = (DaoManager<SubmitDateTable>) DaoManager
				.getInstance();
		dm.setContext(activity);
		Dao<SubmitDateTable, Integer> dao = null;
		dao = dm.getDao(SubmitDateTable.class);
		try {
			list = dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
