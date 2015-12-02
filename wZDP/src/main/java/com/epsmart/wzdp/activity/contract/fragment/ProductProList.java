package com.epsmart.wzdp.activity.contract.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.achar.service.CharResponse;
import com.epsmart.wzdp.activity.achar.service.CharXmlHandler;
import com.epsmart.wzdp.activity.contract.CommonReqAct;
import com.epsmart.wzdp.activity.contract.fragment.excel.jxlCreate;
import com.epsmart.wzdp.activity.contract.fragment.fsch.FileExplorer;
import com.epsmart.wzdp.activity.contract.fragment.fsch.FileExplorer.FileExplorerListener;
import com.epsmart.wzdp.activity.contract.fragment.fsch.SFTPChannel;
import com.epsmart.wzdp.activity.contract.fragment.fsch.SFTPConstants;
import com.epsmart.wzdp.activity.contract.fragment.fsch.ToastTool;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.search.QueryProgressDialog;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.CommonRegex;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.view.MyHScrollView;
import com.epsmart.wzdp.view.MyHScrollView.OnScrollChangedListener;
import com.jcraft.jsch.ChannelSftp;

@SuppressLint("HandlerLeak")
public class ProductProList extends CommonReqAct {
	private ListView mListView1;//
	private RelativeLayout mHead;//
	private LinearLayout title_relalay;
	private MyAdapter myAdapter;
	private HorizontalScrollView headSrcrollView;
	private List<String[]> als = new ArrayList<String[]>();

	protected QueryProgressDialog queryProgDialog;
	private List<String> uploadFiles;
	private String templetaddress;
	private File file;
	private TextView tv;
	private LinearLayout show_content;
	private LinearLayout bottom_btns;
	private Button download_btn;// 下载按钮
	private Button select_btn;// 选择按钮
	private Button upload_btn;// 上传按钮
	private Button export_btn;//导出数据生成excel
	private TextView tv01;
	private BasicEntity entiry = null;
	protected List<String[]> list1 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// activity = this;
		initview();
		init();
		initNet();
		loadData(new RequestPram());

	}

	public void initview() {

		app_context = (AppContext) activity.getApplication();
		search_lay = (RelativeLayout) getActionBar().getCustomView()
				.findViewById(R.id.search_lay);
		search_btn = (Button) getActionBar().getCustomView().findViewById(
				R.id.search_btn);
		show_content = (LinearLayout) findViewById(R.id.show_content);
		tv = (TextView) findViewById(R.id.show_title);

		bottom_btns = (LinearLayout) findViewById(R.id.bottom_btns);
		download_btn = (Button) findViewById(R.id.download_btn);
		select_btn = (Button) findViewById(R.id.select_btn);
		upload_btn = (Button) findViewById(R.id.upload_btn);
		export_btn = (Button) findViewById(R.id.export_btn);

		mHead = (RelativeLayout) findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		myAdapter = new MyAdapter(this, R.layout.list_item, als);
		mListView1.setAdapter(myAdapter);

		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
//		title_image.setBackgroundResource(R.drawable.progress_cs);
		
		if ("tower".equals(getIntent().getStringExtra("types"))) {// 铁塔
			title_image.setBackgroundResource(R.drawable.progress_cs);
		} else if ("porcelain".equals(getIntent().getStringExtra("types"))) {// 瓷
			title_image.setBackgroundResource(R.drawable.ci_prog);
		} else if ("reunite".equals(getIntent().getStringExtra("types"))) {// 复合
			title_image.setBackgroundResource(R.drawable.fuhe_prog);
		} else if ("wireway".equals(getIntent().getStringExtra("types"))) {// 导线
			title_image.setBackgroundResource(R.drawable.dao_prog);
		} else if ("ground".equals(getIntent().getStringExtra("types"))) {// 地线
			title_image.setBackgroundResource(R.drawable.di_prog);
		} else if ("cable".equals(getIntent().getStringExtra("types"))) {// 光缆
			title_image.setBackgroundResource(R.drawable.guang_prog);
		} else if ("fittings".equals(getIntent().getStringExtra("types"))) {// 金具
			title_image.setBackgroundResource(R.drawable.jinju_prog);
		}
		search_lay.setOnClickListener(searchLisp);// 查询
		search_btn.setOnClickListener(searchLisp);

		bottom_btns.setVisibility(View.VISIBLE);
		download_btn.setOnClickListener(btnsLister);
		select_btn.setOnClickListener(btnsLister);
		upload_btn.setOnClickListener(btnsLister);
		export_btn.setOnClickListener(btnsLister);
	}

	private void init() {
		headSrcrollView = (HorizontalScrollView) mHead
				.findViewById(R.id.horizontalScrollView1);
		headSrcrollView.setHorizontalFadingEdgeEnabled(false);
		headSrcrollView.scrollTo(0, 0);

		// 设置行标题的
		tv01 = (TextView) mHead.findViewById(R.id.textView01);
		title_relalay = (LinearLayout) mHead.findViewById(R.id.title_relalay);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			try{
				headSrcrollView.onTouchEvent(arg1);
			}catch(Exception e){
                
			}
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
					textViewArray[i - 1] = tv;

				}
			}

			public void putvalue(String[] args) {
				for (int i = 1; i < args.length; i++) {
					textViewArray[i - 1].setText(args[i]);

				}
			}
		}
	}

	OnClickListener btnsLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select_btn:
				FileExplorer explorer = FileExplorer.openFileExplorer(activity,
						explorerListener, Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "");
				try {
					explorer.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.upload_btn:
				if (null == uploadFiles || uploadFiles.size() < 1) {
					ToastTool.toast(activity, "未添加附件");
					return;
				}
				new UploadTask(activity).execute("");
				break;
			case R.id.download_btn:
				RequestPram requestPram = new RequestPram();
				loadDataDown(requestPram);
				break;
			case R.id.export_btn:
				if (list1 != null && list1.size() > 0) {
					final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							closeDialog();
							Toast.makeText(activity, msg.obj.toString(),
									Toast.LENGTH_LONG).show();
						}
					};
					showModuleProgressDialog("提示", "正在导出...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							jxlCreate jxl = new jxlCreate();
							jxl.setHandler(handler);
							jxl.excelCreate(entiry, list1);
						}
					}).start();

				}
				break;
			}
		}
	};

	/** 下载 */
	public void loadDataDown(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		if("tower".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "1")
					.append(RequestXmlHelp.getReqXML(
							"type", "1")));
		}else if("porcelain".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "2")
					.append(RequestXmlHelp.getReqXML(
							"type", "2")));
		}else if("reunite".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "3")
					.append(RequestXmlHelp.getReqXML(
							"type", "3")));
		}else if("wireway".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "4")
					.append(RequestXmlHelp.getReqXML(
							"type", "4")));
		}else if("ground".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "5")
					.append(RequestXmlHelp.getReqXML(
							"type", "5")));
		}else if("cable".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "6")
					.append(RequestXmlHelp.getReqXML(
							"type", "6")));
		}else if("fittings".equals(getIntent().getStringExtra("types"))){
			requestPram.param = RequestXmlHelp.getCommonXML(RequestXmlHelp
					.getReqXML("report", "7")
					.append(RequestXmlHelp.getReqXML(
							"type", "7")));
		}
		
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		// requestPram.userName = MainActivity.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvTempDownload;
		requestAction.setReqPram(requestPram);
		// super.loadData(requestPram);
		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	private FileExplorerListener explorerListener = new FileExplorerListener() {

		@Override
		public void onClosed(List<String> selectedFiles) {
			Log.d("FileExplorerListener", "onClosed," + selectedFiles);
			uploadFiles = selectedFiles;
			final LinearLayout uploadFilesLayout = (LinearLayout) activity
					.findViewById(R.id.uploadFilesLayout);
			uploadFilesLayout.removeAllViews();
			if (null == selectedFiles || selectedFiles.size() < 1)
				return;

			int size = selectedFiles.size();
			for (int i = 0; i < size; i++) {
				final ImageView imageView = new ImageView(activity);
				imageView.setTag(selectedFiles.get(i));
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						uploadFiles.remove((String) (v.getTag()));
						uploadFilesLayout.removeView(imageView);
					}
				});
				imageView.setBackgroundResource(R.drawable.file);
				uploadFilesLayout.addView(imageView);
			}
		}
	};

	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;

	}

	// 附件上传的异步操作
	class UploadTask extends AsyncTask<String, Void, String> {

		private Handler handler;

		UploadTask(final Context context) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 0:
						ToastTool.toast(context, "附件上传失败！");
						break;
					case 1:
						ToastTool.toast(context, "附件上传成功！");
						break;
					}
				}
			};
		}

		@Override
		protected String doInBackground(String... params) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
					.getDateTimeInstance();
			sdf.applyPattern("yyyyMMddhhmmssSSS");
			for (int i = 0; i < 1; i++) {
				file = new File(uploadFiles.get(0));
				String filename = sdf.format(calendar.getTime()) + "."
						+ getExtensionName(file.getName());
				System.out.println("syso..........>>>>>>>>>>" + filename);
				String remoteFilePath = "/opt/Tomcat6/webapps/wzdp/wzdp/app/downloadApp/excel/"
						+ filename;
				JschUpload(file.getAbsolutePath(), remoteFilePath, handler,
						filename);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			showModuleProgressDialog("提示", "附件上传中,请稍后...");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (null != result) {
				ToastTool.toast(activity, result);
			}
		}

	}

	/**
	 * jsch 方式上传附件
	 * 
	 * @param src
	 * @param dst
	 */
	private void JschUpload(String src, String dst, Handler handler,
			String filename) {
		Map<String, String> sftpDetails = new HashMap<String, String>();
		// 设置主机ip，端口，用户名，密码
		sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "127.0.0.1");
		sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "Admin@123");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "28022");

		SFTPChannel channel = getSFTPChannel();
		ChannelSftp chSftp = null;
		try {
			chSftp = channel.getChannel(sftpDetails, 60000);
			chSftp.put(src, dst, ChannelSftp.OVERWRITE); // 代码段2
			handler.sendEmptyMessage(1);

			RequestAction request = new RequestAction();
			request.serviceName = "ehvTempUpload";
			request.putParam("userName",
					Integer.toString(appContext.user.getUid()));
			
			String mytype = null;
			if("tower".equals(getIntent().getStringExtra("types"))){
				mytype = "1";
			}else if("porcelain".equals(getIntent().getStringExtra("types"))){
				mytype = "2";
			}else if("reunite".equals(getIntent().getStringExtra("types"))){
				mytype = "3";
			}else if("wireway".equals(getIntent().getStringExtra("types"))){
				mytype = "4";
			}else if("ground".equals(getIntent().getStringExtra("types"))){
				mytype = "5";
			}else if("cable".equals(getIntent().getStringExtra("types"))){
				mytype = "6";
			}else if("fittings".equals(getIntent().getStringExtra("types"))){
				mytype = "7";
			}
			request.putParam("reqParam", RequestXmlHelp
					.getCommonXML(RequestXmlHelp
							.getReqXML("filename", filename)
							.append(RequestXmlHelp.getReqXML("templet",
									"wzdp/wzdp/app/downloadApp/excel/"
											+ filename))
							.append(RequestXmlHelp.getReqXML("pspid",
									getIntent().getStringExtra("PSPID")))
							.append(RequestXmlHelp.getReqXML("type", "2"))
							.append(RequestXmlHelp.getReqXML("mytype", mytype))));
			String resultback = upload(request);

			if (resultback == null) {
				return;
			}
			String result = CommonRegex.getMiddleValue("result", resultback);
			String message = CommonRegex.getMiddleValue("message", resultback);
			if ("0".equals(result)) {
				uHandler.obtainMessage(0, message).sendToTarget();
			} else {
				uHandler.obtainMessage(1, message).sendToTarget();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(0);
		} finally {
			if(chSftp!=null){
				chSftp.quit();
			}
			try {
				channel.closeChannel();
			} catch (Exception e) {
				handler.sendEmptyMessage(0);
				// e.printStackTrace();
			}
		}
	}

	/** 处理网络异常等信息 **/
	private BaseHandler uHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_LONG)
						.show();
				break;
			case 1:
				Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		}

	}

	// 下载附件
	private ProgressDialog mProDialog;

	private void jschDown(final String src, final String dst) {

		if (mProDialog == null) {
			mProDialog = ProgressDialog.show(activity, null, "正在下载...", true,
					true);
		}

		final Handler handlerdown = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mProDialog != null && mProDialog.isShowing()) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				switch (msg.what) {
				case 0:
					ToastTool.toast(activity, "下载失败！");
					break;
				case 1:
					ToastTool.toast(activity, "下载成功！");
					break;
				}
			}
		};

		new Thread() {
			@Override
			public void run() {

				Map<String, String> sftpDetails = new HashMap<String, String>();
				sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "127.0.0.1");
				sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
				sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "Admin@123");
				sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "28022");
				SFTPChannel channel = null;
				ChannelSftp chSftp = null;
				try {
					channel = getSFTPChannel();
					chSftp = channel.getChannel(sftpDetails, 60000);

					chSftp.get(dst, src);
					handlerdown.sendEmptyMessage(1);
				} catch (Exception e) {
					e.printStackTrace();
					handlerdown.sendEmptyMessage(0);

				} finally {
					if(chSftp!=null){
						chSftp.quit();
					}
					try {
						channel.closeChannel();
					} catch (Exception e) {
						handlerdown.sendEmptyMessage(0);
						e.printStackTrace();
					}
				}

			}

		}.start();

	}

	public SFTPChannel getSFTPChannel() {
		return new SFTPChannel();
	}

	// 附件上传成功后的 附件信息上传调用接口 webservices
	public String upload(RequestAction requestAction) {
		StringBuilder result = new StringBuilder();
		String ras = null;
		// 1.实例化SoapObject 对象,指定webService的命名空间,以及调用方法名称
		Log.d("serviceName", requestAction.serviceName);
		SoapObject request = new SoapObject(
				RequestParamConfig.serviceNameSpace, requestAction.serviceName);// 这里需要放开
		// 2.设置调用方法参数
		if (requestAction.queryKeys.size() >= 1) {
			List<String> queryKeys = requestAction.queryKeys;
			Bundle queryBundle = requestAction.queryBundle;
			int size = queryKeys.size();
			for (int i = 0; i < size; i++) {
				request.addProperty(queryKeys.get(i),
						queryBundle.get(queryKeys.get(i)));

				System.out.println("upload ProductProList key=" + queryKeys.get(i) + ",value=" + queryBundle.get(queryKeys.get(i)));
			}
		}

		// 3.设置SOAP请求信息(参数部分为SOAP协议版本号,与你要调用的webService中版本号一致)
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		// 4.注册Envelope
		(new MarshalBase64()).register(envelope);
		// 5.构建传输对象,并指明WSDL文档URL,Android传输对象
		HttpTransportSE transport = new HttpTransportSE(
				RequestParamConfig.ServerUrl);
		transport.debug = true;
		// 6.调用WebService(其中参数为1:命名空间+方法名称,2:Envelope对象)
		try {
			transport.call(RequestParamConfig.serviceNameSpace
					+ requestAction.serviceName, envelope);
			if (envelope.getResponse() != null) {
				if (envelope.getResponse() instanceof SoapObject) {
					SoapObject response = (SoapObject) envelope.getResponse();
					int count = response.getPropertyCount();
					for (int i = 0; i < count; i++) {
						result.append(response.getProperty(i).toString());
					}
					ras = result.toString();
				} else if (envelope.getResponse() instanceof SoapPrimitive) {
					SoapPrimitive response = (SoapPrimitive) envelope
							.getResponse();
					ras = response.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return ras;
	}

	/** 数据请求 */
	public void loadData(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.param = getIntent().getStringExtra("reqParam");
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvReportDownload;
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, new CharXmlHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	{
		listener = new QueryDialogListener() {
			@Override
			public void doQuery(String req) {
				RequestPram requestPram = new RequestPram();
				requestPram.param = req;
				loadDataq(requestPram);
			}
		};
	}

	// 查询
	protected OnClickListener searchLisp = new OnClickListener() {
		@Override
		public void onClick(View v) {
			activity.setVers(getIntent().getStringExtra("reqPq"));
			activity.setPspid(getIntent().getStringExtra("PSPID"));
			queryProgDialog = new QueryProgressDialog(activity, listener,
					httpModule);// TODO
			queryProgDialog.show(v);
		}
	};

	/** 点击查询按钮数据请求 */
	public void loadDataq(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.ehvReportDownload;
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, new CharXmlHandler(),
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
			} else if (parseObj instanceof CharResponse) {
				mHandler.obtainMessage(3, parseObj).sendToTarget();
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
			if (msg.what == 0) {//
				onSuccessNew((BasicResponseNew) msg.obj);
			} else if (msg.what == 1) {// 返回数据为空 或 查询异常信息
				onSuccess((BasicResponse) msg.obj);
			} else if (msg.what == 3) {
				onSuccessTable((CharResponse) msg.obj);
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
			values.add((array[i] + " ").split("@\\*"));
		}
		return values;
	}

	private void onSuccessTable(CharResponse charResponse) {

		String title = null;
		if (null == charResponse.entityList
				|| charResponse.entityList.size() < 1) {
			Toast.makeText(this, "无数据！！", Toast.LENGTH_SHORT).show();
			return;
		}

		list = charResponse.entityList;
		entiry = list.get(0);
		String charType = entiry.fields.get("chartype").fieldContent;// 类型
		title = entiry.fields.get("title").fieldContent;
		tv.setText(title);
		tv.setTextSize(18);

		// 获取行标题
		String[] lineTitle = null;
		// 画表格
		Field field = null;
		LinearLayout.LayoutParams row_lp = new LinearLayout.LayoutParams(190,
				56);

		field = entiry.fields.get("lineTitle");// 报表用到
		if (field != null) {
			lineTitle = field.fieldContent.split(",");
		}
		tv01.setText(lineTitle[0]);
		title_relalay.removeAllViews();
		for (int i = 1; i < lineTitle.length; i++) {
			TextView tv = new TextView(this);
			tv.setText(lineTitle[i]);
			tv.setTextSize(15);
			title_relalay.addView(tv, row_lp);
		}
		field = entiry.fields.get("yValue");// 报表用到
		list1 = getStringList(field.fieldContent);
		als.clear();
		als.addAll(list1);
		myAdapter.notifyDataSetChanged();

	}

	private void onSuccess(BasicResponse response) {
		Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show();
	}

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;

		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		templetaddress = response.basicEntityList.get(0).fields
				.get("templetaddress").fieldContent;
		final String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		if (TextUtils.isEmpty(templetaddress))
			templetaddress = "/opt/Tomcat6/webapps/wzdp/wzdp/app/downloadApp/Testdown.xls";
		Log.w("templetaddress","......="+templetaddress);
		Log.w("productProList",".........="+filePath);
		jschDown(filePath, templetaddress);
	}
}
