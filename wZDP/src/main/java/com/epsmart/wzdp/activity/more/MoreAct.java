package com.epsmart.wzdp.activity.more;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TooManyListenersException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AboutUsAct;
import com.epsmart.wzdp.activity.InstallActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.contract.fragment.fsch.ToastTool;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.StatusEntity;

/**
 * 更多设置页面 上传头像信息、关于、离线数据
 * 
 * @author fony
 */

public class MoreAct extends CommonAct {
	private Button ab_btn; // 关于
	private Button setting_btn; // 设置
	private Button off_line_date;
	private Activity activity;
	private ImageView more_head;
	private Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		activity = this;
		initUI();
		
		RequestPram requestPram = new RequestPram();
		requestPram.methodName = RequestParamConfig.getHeadImg;// 获取用户头像信息
		loadReqHead(requestPram);
	}

	private void initUI() {
		ab_btn = (Button) findViewById(R.id.ab);
		setting_btn = (Button) findViewById(R.id.setting);
		off_line_date = (Button) findViewById(R.id.offlinedate);
		more_head = (ImageView) findViewById(R.id.more_head);
		
		// 获取员工头像信息

		ab_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, AboutUsAct.class);
				activity.startActivity(intent);
			}
		});

		setting_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, InstallActivity.class);
				activity.startActivity(intent);
			}
		});

		off_line_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, OffLineData.class);
				activity.startActivity(intent);

			}
		});
		more_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMyDialog(v);
			}
		});
	}

	private void showMyDialog(View v) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.show_picselect_dialog, null);

		dialog = new AlertDialog.Builder(activity).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);

		ImageButton photo_cm = (ImageButton) layout.findViewById(R.id.photo_cm);
		ImageButton photo_cdmi = (ImageButton) layout
				.findViewById(R.id.photo_cdmi);
		ImageButton photo_cancel = (ImageButton) layout
				.findViewById(R.id.photo_cancel);
		photo_cm.setOnClickListener(image_ocl);
		photo_cdmi.setOnClickListener(image_ocl);
		photo_cancel.setOnClickListener(image_ocl);
	}

	OnClickListener image_ocl = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.photo_cm) {
				dialog.cancel();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				activity.startActivityForResult(intent, 1);
			} else if (id == R.id.photo_cdmi) {
				dialog.cancel();
				Intent intents = new Intent();
				intents.setType("image/*");
				intents.setAction(Intent.ACTION_GET_CONTENT);
				activity.startActivityForResult(intents, 2);
			} else if (id == R.id.photo_cancel) {
				dialog.cancel();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		onCammerResult(requestCode, resultCode, data);
	}

	public void onCammerResult(int requestCode, int resultCode, Intent data) {
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
				more_head.setImageBitmap(parseRound(bitmap, 63));
				RequestPram requestPram = new RequestPram();
				requestPram.param = bitmapStr;
				requestPram.methodName = RequestParamConfig.getHeadImage;
				loadReq(requestPram);
			}
			break;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String realPath = getRealPathFromURI(uri);
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 4;
					Bitmap bitmap = BitmapFactory.decodeFile(realPath, opts);
					saveJPG(bitmap);
					bitmap = postBit(bitmap);
					String bitmapStr = bitmapToString(bitmap);
					more_head.setImageBitmap(parseRound(bitmap, 63));
					RequestPram requestPram = new RequestPram();
					requestPram.param = bitmapStr;
					requestPram.methodName = RequestParamConfig.getHeadImage;
					loadReq(requestPram);
				} catch (Exception e) {
					Toast.makeText(activity, "图片过大或内容格式错误", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	/** 数据请求 */
	public void loadReq(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, null, new ProcessResponse(),
				RequestType.THRIFT);
	}
	/** 数据请求 */
	public void loadReqHead(RequestPram requestPram) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		httpModule.executeRequest(requestAction, null, new ProcessResponseHead(),
				RequestType.THRIFT);
	}


	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				StatusEntity staty = (StatusEntity)parseObj;
				String msg = staty.message;
				mHandler.obtainMessage(0, msg).sendToTarget();
			}
		}
	}
	
	class ProcessResponseHead implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				StatusEntity staty = (StatusEntity)parseObj;
				String result = staty.result;
				String message = staty.message;
				if("1".equals(result) && !TextUtils.isEmpty(message)){
					mHandler.obtainMessage(1, message).sendToTarget();
				}else{
				mHandler.obtainMessage(0, message).sendToTarget();
				}
			}
		}
	}

	protected FlowHandler mHandler = new FlowHandler();
	@SuppressLint("HandlerLeak")
	public class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				String head = (String)msg.obj;
//				StatusEntity sety=(StatusEntity)msg.obj;
//				if("1".equals((String) msg.obj)&&!TextUtils.isEmpty(sety.message)){
				more_head.setImageBitmap(parseRound(stringToBitmap(head), 63));
//				}else
//					ToastTool.toast(activity, ((StatusEntity) (msg.obj)).message);
			}else
			ToastTool.toast(activity, ((String)(msg.obj)));
			closeDialog();
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

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

	public String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();

		return encoder.encode(bytes);
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
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

	/** 图片进行压缩处理 */
	private Bitmap postBit(Bitmap bitmapOrg) {

		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = 130;
		int newHeight = 130;

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
		return resizedBitmap;//parseRound(resizedBitmap, 63);
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
}
