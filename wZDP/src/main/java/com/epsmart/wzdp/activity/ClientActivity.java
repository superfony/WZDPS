package com.epsmart.wzdp.activity;

import org.json.JSONException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.epsmart.wzdp.aidl.AppInfo;
import com.epsmart.wzdp.aidl.ExecuteServiceAIDL;
import com.epsmart.wzdp.aidl.ICallBack;
import com.epsmart.wzdp.aidl.UserInfo;
import com.epsmart.wzdp.bean.User;
import com.epsmart.wzdp.common.PermissHelp;

public class ClientActivity extends Activity {
	public static final String TAG = "ClientActivity";

	private static final String BIND_ACTION = "com.epsmart.wzdp.aidl.service";
	private ExecuteServiceAIDL executeService;
	private Handler handler;
	protected AppContext appContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext=(AppContext)getApplication();
	}

	

	protected void getServiceConnect(Handler handler) {
		this.handler=handler;
		Intent intent = new Intent();
		intent.setAction(BIND_ACTION);
		startService(intent);
		bindService(intent, mserviceConnection, BIND_AUTO_CREATE);
	}

	ServiceConnection mserviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "onServiceConnected");
			executeService = ExecuteServiceAIDL.Stub.asInterface(service);
			if (executeService != null) {
				handlerInfo();
			}
		}
	};

	private void handlerInfo() {

		AppInfo mInfo = new AppInfo();
		
		try {
			UserInfo userInfo = new UserInfo();
			userInfo = executeService.getServerHarlanInfo(mInfo, mCallBack);
			String userAllInfo=userInfo.getUserAllInfo();
			Log.i("", "....client..userAllInfo...>>"+userAllInfo);
			if(userAllInfo!=null){
			 try {
				User user=PermissHelp.getUser(userAllInfo);
				//MainActivity.user=user;
				handler.sendEmptyMessage(111);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}
			unbindService(mserviceConnection);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	ICallBack.Stub mCallBack = new ICallBack.Stub() {
		@Override
		public void handleByServer(String param) throws RemoteException {
			Toast.makeText(getApplicationContext(), param, Toast.LENGTH_LONG)
					.show();
		}
	};
}
