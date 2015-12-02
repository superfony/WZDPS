package com.epsmart.wzdp.activity.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.contract.CommonReqAct;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

public class QueryEquDialog {
	private static final String TAG = QueryEquDialog.class.getName();
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
	private AppContext app_context;
	private String[] version = null;
	private BaseHttpModule httpModule;
	private AppContext appContext;

	public QueryEquDialog(final Activity activity,
			final QueryDialogListener listener, BaseHttpModule httpModule) {
		this.mActivity = activity;
		appContext=(AppContext)activity.getApplication();
		this.listener = listener;
		this.httpModule = httpModule;

		layoutInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = layoutInflater.inflate(R.layout.equ_query, null);
		popView.setFocusableInTouchMode(true);
		popView.setFocusable(true);

		popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.UpInAndOutAnimation);
		popupWindow.update();

		Resources res = activity.getResources();

		TextView ev_one = (TextView) popView
				.findViewById(R.id.search_title_one);
		TextView ev_two = (TextView) popView
				.findViewById(R.id.search_title_two);
		TextView ev_three = (TextView) popView
				.findViewById(R.id.search_title_three);
		TextView ev_four = (TextView) popView
				.findViewById(R.id.search_title_four);

		et_one = (EditText) popView.findViewById(R.id.search_et_one);// 条件一
		et_two = (EditText) popView.findViewById(R.id.search_et_two);// 条件二
		et_three = (EditText) popView.findViewById(R.id.search_et_three);// 条件三
		et_four = (EditText) popView.findViewById(R.id.search_et_four);// 条件四
		app_context = (AppContext) activity.getApplication();

		ev_one.setText("项目名称：");
		ev_two.setText("供应商名称：");
		ev_three.setText("物料类型：");
		ev_four.setText("监理公司：");
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

				req = RequestXmlHelp.getCommonXML(RequestXmlHelp.getReqXML(
						"projectname", et_one.getText().toString())
						.append(RequestXmlHelp.getReqXML("suppliername",et_two.getText().toString()))
						.append(RequestXmlHelp.getReqXML("materialtype",et_three.getText().toString()))
						.append(RequestXmlHelp.getReqXML("supervisorcompany",et_four.getText().toString())));
				listener.doQuery(req);
				popupWindow.dismiss();
			}

		});
	}

	protected ProgressDialog progressDialog;

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(this.mActivity, title, msg, true);
	}

	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	public void loadDataquery(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestPram.param = ((CommonReqAct) mActivity).getVers();
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvplanrep;
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			closeDialog();
			if (parseObj instanceof BasicResponseNew) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			} else if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			}
		}
	}

	private FlowHandler mHandler = new FlowHandler();
	private BasicResponse response;
	private BasicResponseNew responseNew;

	private class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				onSuccessNew((BasicResponseNew) msg.obj);
			} else if (msg.what == 1) {
				onSuccess((BasicResponse) msg.obj);
			}
		}
	}

	private void onSuccess(BasicResponse response) {
		this.response = response;
		if (null == response.entity || response.entity.rows.size() < 1)
			return;
	}

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;

		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		String ver = response.basicEntityList.get(0).fields.get("version").fieldContent;
		version = ver.split(",");

		AlertDialog.Builder builder = new Builder(mActivity);
		builder.setTitle("选择版本号")
				.setCancelable(true)
				.setSingleChoiceItems(version, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								et_one.setText(version[which]);

							}
						});
		AlertDialog dlg = builder.create();
		dlg.show();

	}

	public void show(View anchor) {// TODO 设置显示
		popupWindow.setHeight(dipToPixels(400));
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
}
