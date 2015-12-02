package com.epsmart.wzdp.updata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;

/**
 * 应用程序更新工具包 fony
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;

	private static UpdateManager updateManager;

	private Context mContext;
	// 通知对话框
	private Dialog noticeDialog;
	// 下载对话框
	private Dialog downloadDialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 查询动画
	private ProgressDialog mProDialog;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	// 终止标记
	private boolean interceptFlag;
	// 提示语
	private String updateMsg = "";
	// 返回的安装包url
	private String apkUrl = "";
	// 下载包保存路径
	private String savePath = "";
	// apk保存完整路径
	private String apkFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	private TextView reminder_info;

	private String curVersionName = "";
	private int curVersionCode;
	private Update mUpdate;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", 3000).show();
				break;
			}
		};
	};

	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	/**
	 * 检查App更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *            是否显示提示消息
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg) {
			if (mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...",
						true, true);
			else if (mProDialog.isShowing()
					|| (latestOrFailDialog != null && latestOrFailDialog
							.isShowing()))
				return;
		}
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				// 进度条对话框不显示 - 检测结果也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放释放进度条对话框
				if (isShowMsg && mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				// 显示检测结果
				if (msg.what == 1) {
					mUpdate = (Update) msg.obj;
					if (mUpdate != null) {
						Log.i("curVersionCode",
								"curVersionCode=" + curVersionCode
										+ "newVersion="
										+ mUpdate.getVersionCode());
						if (curVersionCode < mUpdate.getVersionCode()) {
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							showNoticeDialog();
						} else if (isShowMsg) {
							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						}
					}
				} else if (isShowMsg) {
					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					Update update = ApiClient
							.checkVersion((AppContext) mContext
									.getApplicationContext());
					msg.what = 1;
					msg.obj = update;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			// 关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}

		LayoutInflater inflater = LayoutInflater.from(mContext);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.software_version, null);

		latestOrFailDialog = new AlertDialog.Builder(mContext).create();
//		builder.setView(layout).create();
		latestOrFailDialog.show();
		latestOrFailDialog.getWindow().setContentView(layout);
		reminder_info = (TextView) layout.findViewById(R.id.reminder_info);
		if (dialogType == DIALOG_TYPE_LATEST) {
			reminder_info.setText("您当前已经是最新版本");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			reminder_info.setText("无法获取版本更新信息");

		}
		Button confirm_info = (Button) layout.findViewById(R.id.confirm_info);
		confirm_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				latestOrFailDialog.dismiss();
			}
		});

	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.software_update, null);

		noticeDialog = new AlertDialog.Builder(mContext).create();
		noticeDialog.show();
		noticeDialog.getWindow().setContentView(layout);
		Button immediate_update = (Button) layout.findViewById(R.id.immediate_update);
		Button talk_later = (Button) layout.findViewById(R.id.talk_later);
		TextView updateInfo = (TextView) layout.findViewById(R.id.updateInfo);
		updateInfo.setText(updateMsg);
		immediate_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				noticeDialog.dismiss();
				showDownloadDialog();
			}
		});
		talk_later.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				noticeDialog.dismiss();
			}
		});
		
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.update_progress, null);

		downloadDialog = new AlertDialog.Builder(mContext).create();
		downloadDialog.show();
		downloadDialog.getWindow().setContentView(layout);
		mProgress = (ProgressBar) layout.findViewById(R.id.update_progress);
		mProgressText = (TextView) layout
				.findViewById(R.id.update_progress_text);
		Button update_load = (Button) layout.findViewById(R.id.update_load);
		update_load.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				downloadDialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadApk();

	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "wzdp" + mUpdate.getVersionName() + ".apk";
				String tmpApk = "wzdp" + mUpdate.getVersionName() + ".tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/epsmart/updateapp/";
					Log.i("", ".....savePath.>>" + savePath);
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				if (apkFilePath == null || "".equals(apkFilePath)) {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				File ApkFile = new File(apkFilePath);
				if (ApkFile.exists()) {
					downloadDialog.dismiss();
					installApk();
					return;
				}
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				DecimalFormat df = new DecimalFormat("0.00");
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(ApkFile)) {
							// 通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	/**
	 * 安装apk
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}