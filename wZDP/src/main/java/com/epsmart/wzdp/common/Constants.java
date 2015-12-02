package com.epsmart.wzdp.common;


import android.content.Intent;
import android.os.Environment;
import android.view.View;

public class Constants {
	public static View supplyLeftTextView;
	public static View supplyTopTextView;
	public static int supplyLeftTag;
	public static final String TAG = "com.lnsoft.dycjmobilesystem";
	public static final int LEFT_FRAGMENT_WIDTH = 200;
	public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().getPath()
			+ "/com.lnsoft.dycjmobilesystem/db/";
	public static final String DATABASE_NAME = "dycjmobilesystem.db3";

	public static final byte[] SOCKET_ORDER_TRANSFORMER_CURRENT= new byte[]{0x67, 0x74, 0x00, 0x00, 0x00, 0x00};//�õ��ñ�ѹ����ǰʵʱ���
	public static final byte[] SOCKET_ORDER_TRANSFORMER_ONE_HOUR= new byte[]{0x67, 0x74, 0x10, 0x00, 0x00, 0x00};//�õ��ñ�ѹ��1Сʱǰ��ʷ���
	public static final byte[] SOCKET_ORDER_TRANSFORMER_TWO_HOUR= new byte[]{0x67, 0x74, 0x20, 0x00, 0x00, 0x00};//�õ��ñ�ѹ��2Сʱǰ��ʷ���
	public static final byte[] SOCKET_ORDER_TRANSFORMER_TWELVE_HOUR= new byte[]{0x67, 0x74, 0x12, 0x00, 0x00, 0x00};//�õ��ñ�ѹ��12Сʱǰ��ʷ���

	public static String ip;
	public static final int port = 7899;
	public static Intent logServiceIntent;
	
	public static final String CURRENT_MAX_VALUE="CURRENT_MAX_VALUE";
	public static final String CURRENT_MIN_VALUE="CURRENT_MIN_VALUE";
	public static final String VOLTAGE_MAX_VALUE="VOLTAGE_MAX_VALUE";
	public static final String VOLTAGE_MIN_VALUE="VOLTAGE_MIN_VALUE";
	public static final String POWERFACTOR_MAX_VALUE = "POWERFACTOR_MAX_VALUE"; 
	public static final String THREEUNBALANCERATE_MAX_VALUE = "THREEUNBALANCERATE_MAX_VALUE"; 
	
	public static final int SOCKET_TIMEOUT = 60 *1000;
	
//	public static String WARING_DEV_TYPE_TRAN = "��ѹ��";
//	public static String WARING_DEV_TYPE_MB = "����";
//	
//	public static final String DEV_TO_TRANSFORMER = "��ѹ��";
//	public static final String DEV_TO_METERBOX = "����";
	
}
