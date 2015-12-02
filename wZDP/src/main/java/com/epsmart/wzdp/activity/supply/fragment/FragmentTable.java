package com.epsmart.wzdp.activity.supply.fragment;

import java.sql.SQLException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandler;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.db.dao.DaoManager;
import com.epsmart.wzdp.db.table.SimpleData;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 关键点见证表Fragment
 * 
 * @author fony 测试table动态加载
 * @param <E>
 */
@SuppressLint("NewApi")
public class FragmentTable<E> extends SupplyFragmemt {
	private View view;
	protected WorkOrder workOrder;
	private BasicResponse response;
	private String TAG = FragmentTable.class.getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_table, container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		text_lay = (LinearLayout) view.findViewById(R.id.show_text_lay);
		table_lay = (LinearLayout) view.findViewById(R.id.show_input_lay);
		btn_lay = (LinearLayout) view.findViewById(R.id.show_submit_lay);
		submit_btn = (Button) view.findViewById(R.id.submit_button);
		cancel_btn = (Button) view.findViewById(R.id.cancel_button);
		submit_btn.setOnClickListener(clickLis);
		cancel_btn.setOnClickListener(clickLis);
		if (response == null)
			loadData(requestPram);
		else
			initTable(response.entity);

		/********* 测试从本地获取数据 **********/
//		if (true) {
//			DaoManager dm = DaoManager.getInstance().getDao(
//					activity.getDbHelper(), SimpleData.class);
//		List<E>  list=dm.getAllData(); 
//		try {
//			Log.i("", ".....getCount......>>" + dm.getCount());
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		 try {
//			SimpleData sim =(SimpleData) list.get(0);
//			Log.i("", "...........>>" + sim);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		
//		}
//		}
		/*********************************/
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	/** 数据请求 */
	public void loadData(RequestPram param) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		param.methodName="2";
		requestAction.setReqPram(param);
		requestAction.putParam("txt", "showTable_log.txt");
		httpModule.executeRequest(requestAction, new BasicResponseHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			}
		}
	}

	/** 设置请求参数 */
	protected void submitMethod(RequestPram requestPram) {
		requestPram.methodName = "3";
		requestPram.param = fillHelp.getparams();
		super.submitMethod(requestPram);
	}

	@Override
	protected void cancelMethod() {
		super.cancelMethod();
	}

	private FlowHandler mHandler = new FlowHandler();

	private class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();
			if (msg.what == 0) {
				onSuccess((BasicResponse) msg.obj);
			}
		}
	}

	private void onSuccess(BasicResponse response) {
		this.response = response;
		initTable(response.entity);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}