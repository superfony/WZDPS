package com.epsmart.wzdp.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.achar.DoStatisticsAct;
import com.epsmart.wzdp.activity.contract.ContractMenuActivity;
import com.epsmart.wzdp.activity.dailyoffice.DailyActivity;
import com.epsmart.wzdp.activity.dailyoffice.DailyMenuActivity;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyNoticeNum;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyNoticeNumHandler;
import com.epsmart.wzdp.activity.more.MoreAct;
import com.epsmart.wzdp.activity.movedapprove.MovedMenuActivity;
import com.epsmart.wzdp.activity.scene.SceneMenuActivity;
import com.epsmart.wzdp.activity.supply.SupplyMenuActivity;
import com.epsmart.wzdp.bean.PermissionHandler;
import com.epsmart.wzdp.bean.PermissionResponse;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;
import com.epsmart.wzdp.scan.example.qr_codescan.MipcaActivityCapture;
import com.epsmart.wzdp.updata.UpdateManager;
import com.epsmart.wzdp.util.Pub_method;
import com.epsmart.wzdp.view.BadgeView;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayout;

/**
 * fony 默认请求权限列表信息
 */
public class MainActivity extends ClientActivity {
	private String TAG = MainActivity.class.getName();
	public static String provenance;
	private BaseHttpModule httpModule;
	private Activity activity;
	// public static User user;
	private CircleImageView supply;// 供应商现场管理
	private CircleImageView project;// 项目现场管理
	private CircleImageView online;// 在线分析
	private CircleImageView move_contract;// 特高压项目管理
	private CircleImageView move_office;// 日常办公
	private CircleImageView approval;// 移动审批
	private CircleImageView conference;//视频会议
	// 是否记住不再提示
	boolean not_prompt = false;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	// private TextView mTextView;
	// private ImageView mImageView;
	private PackageManager pm = null;
	private List<ResolveInfo> activities = null;
	// private AppContext appContext;
	private String office;
	private String move;
	private String tables;
	public static BadgeView bv_review;// 通知公告消息提醒
	private TextView banners_text, banners_content;
	private LinearLayout banner_lay, warnning_lay;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	// 视频下载弹出框
	private Dialog approvaDialog;
	private String bestProvider;
	private RequestPram requestPram;


	static {
		if (RequestParamConfig.isDubeg) {
			// user = new User();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_main);
		// startService(new Intent(MainActivity.this,GpsActivity.class));

		UpdateManager.getUpdateManager().checkAppUpdate(this, false);// 检查是否更新
		// mTextView = (TextView) findViewById(R.id.result);
		// mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		ImageView install = (ImageView) findViewById(R.id.install_image);// 设置按钮
		Button img = (Button) findViewById(R.id.code_image);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		install.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, MoreAct.class);
				activity.startActivity(intent);
			}
		});

		CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		TextView selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView) circleMenu
				.getSelectedItem()).getName());

		supply = (CircleImageView) circleMenu.findViewById(R.id.supply_image);
		project = (CircleImageView) circleMenu.findViewById(R.id.project_image);
		supply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("001")) {
					PermissHelp.showToast(activity);
					return;
				}
				provenance = "supply";
				Intent intent = new Intent(MainActivity.this,
						SupplyMenuActivity.class);
				intent.putExtra("tables", tables);
				MainActivity.this.startActivity(intent);
			}
		});

		project.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("002")) {
					PermissHelp.showToast(activity);
					return;
				}
				provenance = "scene";
				Intent intent = new Intent(MainActivity.this,
						SceneMenuActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		online = (CircleImageView) circleMenu.findViewById(R.id.online_image);// 在线统计
		online.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("029")) {
					PermissHelp.showToast(activity);
					return;
				}
				provenance = "statistics";
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DoStatisticsAct.class);
				MainActivity.this.startActivity(intent);
			}
		});

		move_contract = (CircleImageView) circleMenu
				.findViewById(R.id.move_contract);// 特高压项目管理
		move_contract.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("032")) {
					PermissHelp.showToast(activity);
					return;
				}
				provenance = "contract";
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ContractMenuActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		move_office = (CircleImageView) circleMenu
				.findViewById(R.id.move_office);// 日常办公
		move_office.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("030")) {
					PermissHelp.showToast(activity);
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("office", office);
				intent.setClass(MainActivity.this, DailyMenuActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});

		approval = (CircleImageView) circleMenu
				.findViewById(R.id.move_approval);// 移动审批
		approval.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("031")) {
					PermissHelp.showToast(activity);
					return;
				}
				// Toast.makeText(activity, "没有权限", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("move", move);
				intent.setClass(MainActivity.this, MovedMenuActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		conference = (CircleImageView) circleMenu
				.findViewById(R.id.conference_image);// 视频会议
		conference.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("039")) {
					PermissHelp.showToast(activity);
					return;
				}
				if (appContext.user == null) {
					Toast.makeText(activity, "获取用户信息失败！", Toast.LENGTH_LONG)
							.show();
					return;
				}
				ComponentName apk2Component1 = new ComponentName(
						"com.fastonz.fastmeeting",
						"com.fastonz.fastmeeting.ui.StartTheMiddleTierActivity");
				pm = activity.getPackageManager();
				Intent mIntent = new Intent();
				Bundle mBundle = new Bundle();
				mBundle.putString("userName", appContext.user.getFace());
				mBundle.putString("userPwd", appContext.user.getPwd());
				mIntent.putExtras(mBundle);

				mIntent.setComponent(apk2Component1);
				activities = pm.queryIntentActivities(mIntent,
						PackageManager.MATCH_DEFAULT_ONLY);
				if (activities == null || activities.size() < 1) {
					showApprovaDialog();
					return;
				}
				startActivity(mIntent);
			}
		});

		final int POSITION_TOP_LEFT = 1;
		banner_lay = (LinearLayout) findViewById(R.id.banner_lay);// 通知公告警告消息条幅
		warnning_lay = (LinearLayout) findViewById(R.id.warnning_lay);// 通知公告消息条幅
		bv_review = new BadgeView(this, warnning_lay);
		bv_review.setBackgroundResource(R.drawable.widget_count_bg);
		bv_review.setIncludeFontPadding(false);
		bv_review.setGravity(Gravity.CENTER);
		bv_review.setTextSize(8f);
		bv_review.setTextColor(Color.WHITE);
		bv_review.setBadgePosition(POSITION_TOP_LEFT);
		warnning_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("tag", "0");
				intent.setClass(MainActivity.this, DailyActivity.class);
				MainActivity.this.startActivity(intent);
				warnning_lay.setVisibility(View.GONE);
				bv_review.setVisibility(View.GONE);
				banner_lay.setVisibility(View.GONE);
			}
		});
		banners_text = (TextView) findViewById(R.id.banners_text);
		banners_content = (TextView) findViewById(R.id.banners_content);

		// getServiceConnect(mHandler);// AIDL 方式获取 功能权限
		/*********** 测试 *********/
		reqPermission();

		//gps定位
		getGps();

	}

	private void getGps(){
		// 判断GPS是否正常启动
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		System.out.println("...........>>"
				+ !lm.isProviderEnabled(LocationManager.GPS_PROVIDER));
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Toast.makeText(this, "请开启GPS导航...",
			// Toast.LENGTH_SHORT).show();
			return;
		}
		// 为获取地理位置信息时设置查询条件
		bestProvider = lm.getBestProvider(getCriteria(), true);

		// 监听状态
		lm.addGpsStatusListener(listener);

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				locationListener);
	}

	// 显示“视频下载安装”对话框
	protected void showApprovaDialog() {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View layout = inflater.inflate(R.layout.approva_dialog, null);
		approvaDialog = new AlertDialog.Builder(activity).create();
		approvaDialog.setCancelable(false);
		approvaDialog.show();
		approvaDialog.getWindow().setContentView(layout);
		approvaDialog.setCancelable(true);
		TextView edit_approva = (TextView) layout
				.findViewById(R.id.edit_approva);
		ImageView close_btn = (ImageView) layout.findViewById(R.id.close_btn);
		// Button exit_cancel = (Button) layout.findViewById(R.id.exit_cancel);
		edit_approva.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				approvaDialog.dismiss();
			}
		});
		close_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				approvaDialog.dismiss();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/* 请求权限 */
	private void reqPermission() {
		if (appContext.user == null) {
			Toast.makeText(this, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Toast.makeText(this, "数据配置完毕！", Toast.LENGTH_SHORT).show();
		}
		RequestPram requestPram = new RequestPram();
		showModuleProgressDialog("提示", "数据配置中...");
		httpModule = new BaseHttpModule(this);
		httpModule.init();
		httpModule.setRequestListener(requestListener);
		httpModule.setServiceNameSpace(RequestParamConfig.serviceNameSpace);
		httpModule.setServiceUrl(RequestParamConfig.ServerUrl);

		RequestAction requestAction = new RequestAction();
		requestAction.reset();

		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.userLogin;
		requestPram.pluginId = 119;//
		requestPram.userName = appContext.user.getUid();
		/******** 测试代码 获取用户权限***start ******/
		// requestAction.putParam("txt", "permission.txt");//TODO 权限测试文件
		/******** 测试代码 获取用户权限****end *****/
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new PermissionHandler(),
				new ProcessResponse(), RequestType.THRIFT);

	}

	/* 处理表单提交返回消息 */
	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			} else if (parseObj instanceof PermissionResponse) {
				AppContext app_context = (AppContext) activity.getApplication();
				app_context.permission = (PermissionResponse) parseObj;
				PermissHelp.permiss = app_context.permission;
				mHandler.sendEmptyMessage(11);
			} else if (parseObj instanceof DailyNoticeNum) {
				DailyNoticeNum resp = ((DailyNoticeNum) parseObj);
				mHandler.obtainMessage(2, resp).sendToTarget();
			}
		}
	}

	protected ProgressDialog progressDialog;
	/* 处理网络错误 */
	protected RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(Response response) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			httpModule.processResponse(httpModule, response,
					getXmlParser(response), httpModule.getResponseProcess());
		}

		@Override
		public void onFail(Exception e) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			String msg = "未知错误";
			if (e instanceof HttpException) {
				HttpException he = (HttpException) e;
				msg = he.getMessage();
			}

			Message obtainMessage = mHandler.obtainMessage(0);
			obtainMessage.obj = msg;
			obtainMessage.sendToTarget();
		}
	};

	protected BaseXmlParser getXmlParser(Response response) {
		Log.i("MainActivity",
				"response for permission=" + response.getResponseAsString());
		response.setResponseAsString(response.getResponseAsString().replace(
				"&", "#"));
		return httpModule.getBaseXmlParser(response,
				httpModule.getParseHandler());
	}

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(this, title, msg, true);
	}

	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	/** 处理网络异常等信息 **/
	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();
			switch (msg.what) {
				case 0:
					Toast.makeText(activity, (String) msg.obj + "",
							Toast.LENGTH_LONG).show();
					break;
				case 1:
					StatusEntity se = (StatusEntity) msg.obj;
//				Toast.makeText(activity, se.message + requestPram.param, Toast.LENGTH_LONG)
//						.show();
					Toast.makeText(activity, se.message + "", Toast.LENGTH_LONG)
							.show();
					break;
				case 111:
					reqPermission();
					break;
				case 11:
					reqNoticeNum();
					if (!PermissHelp.isPermiss("001")) {
						supply.setBackgroundResource(R.drawable.icon_supplyxz);
					}
					if (!PermissHelp.isPermiss("032")) {
						move_contract
								.setBackgroundResource(R.drawable.icon_contractxz);
					}
					if (!PermissHelp.isPermiss("002")) {
						project.setBackgroundResource(R.drawable.icon_projectxz);
					}
					if (!PermissHelp.isPermiss("029")) {
						online.setBackgroundResource(R.drawable.icon_onlinexz);
					}
					if (!PermissHelp.isPermiss("030")) {
						move_office.setBackgroundResource(R.drawable.icon_officexz);
					}
					if (!PermissHelp.isPermiss("028")) {
						office = "work";
					}
//				if (!PermissHelp.isPermiss("031")) {
//					approval.setBackgroundResource(R.drawable.icon_approvalxz);
//				}
					if (!PermissHelp.isPermiss("013")) {
						move = "quality";
					}
					if (!PermissHelp.isPermiss("025")) {
						move = "service";
					}
//				if (!PermissHelp.isPermiss("035")) {
//					office = "online";
//				}
//				if (!PermissHelp.isPermiss("036")) {
//					office = "notice";
//				}
//				if (!PermissHelp.isPermiss("037")) {
//					appro = "services";
//				}
//				if (!PermissHelp.isPermiss("038")) {
//					appro = "observe";
//				}
//				if (!PermissHelp.isPermiss("039")) {
//					conference.setBackgroundResource(R.drawable.icon_approvalxz);
//				}
//				if (!PermissHelp.isPermiss("040")) {
//					tables = "tables";
//				}
					break;
				case 2:
					DailyNoticeNum resp = (DailyNoticeNum) msg.obj;
					if (!TextUtils.isEmpty(resp.noreadnum)) {
						warnning_lay.setVisibility(View.VISIBLE);
						bv_review.setText(resp.noreadnum);
						bv_review.show();
						banners_content.setText(resp.content);
					}
					if (!TextUtils.isEmpty(resp.alarm)) {
						banner_lay.setVisibility(View.VISIBLE);
						banners_text.setText(resp.alarm);
					}
					startTimer();
					break;
				default:
					break;
			}
		}
	}

	private LocationManager lm;

	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(task, 3000, 10000);
		}

	}

	TimerTask task = new TimerTask() {

		public void run() {

			// 获取位置信息
			// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
			try {
				final Location location = lm.getLastKnownLocation(bestProvider);
				loadData(location);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	};

	// 位置监听
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			// updateView(location);
			loadData(location);

		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
				// GPS状态为可见时
				case LocationProvider.AVAILABLE:
					Log.i(TAG, "当前GPS状态为可见状态");
					break;
				// GPS状态为服务区外时
				case LocationProvider.OUT_OF_SERVICE:
					Log.i(TAG, "当前GPS状态为服务区外状态");
					break;
				// GPS状态为暂停服务时
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Log.i(TAG, "当前GPS状态为暂停服务状态");
					break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = lm.getLastKnownLocation(provider);
			System.out.println(",,,,,,,,,,,>>>>>>>>>>>>>>>location>>>"+ location);
			// updateView(location);
			loadData(location);
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
			// updateView(null);
		}

	};

	private void loadData(Location location) {
		Log.i("", "............loadData...........................");
		RequestAction requestAction = new RequestAction();
		requestPram = new RequestPram();
		if(location != null){
			requestPram.param = RequestXmlHelp
					.getCommonXML(RequestXmlHelp
							.getReqXML("longitude",
									String.valueOf(location.getLongitude()))// 经度
							.append(RequestXmlHelp.getReqXML("latitude",
									String.valueOf(location.getLatitude())))// 纬度
							.append(RequestXmlHelp.getReqXML("padid",
									Pub_method.getDeviceID(MainActivity.this))));// 设备号
			requestPram.methodName = RequestParamConfig.LocationUpdate;
			requestPram.userName = appContext.user.getUid();
			requestAction.setReqPram(requestPram);
			httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
					new ProcessResponse(), RequestType.THRIFT);
		}

	}

	/**
	 * 请求通知公告未读消息总记录数
	 */
	private void reqNoticeNum() {
		RequestPram requestPram = new RequestPram();
		showModuleProgressDialog("提示", "数据配置中...");
		httpModule = new BaseHttpModule(this);
		httpModule.init();
		httpModule.setRequestListener(requestListener);
		httpModule.setServiceNameSpace(RequestParamConfig.serviceNameSpace);
		httpModule.setServiceUrl(RequestParamConfig.ServerUrl);

		RequestAction requestAction = new RequestAction();
		requestAction.reset();

		requestPram.methodName = RequestParamConfig.getNoRedNum;
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DailyNoticeNumHandler(),
				new ProcessResponse(), RequestType.THRIFT);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			cleanmTimerTask();

			dialog();
			return false;
		}
		return false;

	}

	private void cleanmTimerTask() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}

	// 推出弹出框
	Dialog dialog;

	private void dialog() {

		LayoutInflater inflater = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.exit_dialog, null);

		dialog = new AlertDialog.Builder(activity).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);

		Button exit_cancel = (Button) layout.findViewById(R.id.exit_cancel);
		Button drop_out = (Button) layout.findViewById(R.id.drop_out);

		drop_out.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();
			}
		});
		exit_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SCANNIN_GREQUEST_CODE:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					Toast.makeText(MainActivity.this,"result="+bundle.getString("result")+"",Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	// 状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
				// 第一次定位
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					Log.i(TAG, "第一次定位");
					break;
				// 卫星状态改变
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					Log.i(TAG, "卫星状态改变");
					// 获取当前状态

					GpsStatus gpsStatus = lm.getGpsStatus(null);
					// 获取卫星颗数的默认最大值
					int maxSatellites = gpsStatus.getMaxSatellites();

					System.out.println("搜索到：" + maxSatellites + "颗卫星");
					break;
				// 定位启动
				case GpsStatus.GPS_EVENT_STARTED:
					// Log.i(TAG, "定位启动");
					break;
				// 定位结束
				case GpsStatus.GPS_EVENT_STOPPED:
					break;
			}
		};
	};

	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(true);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
	}
}
