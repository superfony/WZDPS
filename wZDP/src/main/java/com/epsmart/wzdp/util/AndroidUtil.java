package com.epsmart.wzdp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class AndroidUtil {
	private static String sdcardPath;
	private static final String MSG_BUNDLE_KEY = "result";
	/**
	 * 测量view的宽度
	 * @param view
	 * @return
	 */
	public static int getViewWidth(View view) {
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
//		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
		return width;
	}
	/**
	 * 获取屏幕宽度（px），密度等信息
	 * 
	 * @return view宽度（px）
	 */
	public static int getWindowWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density  = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
		int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
		int windth = dm.widthPixels;
		Log.i(activity.getPackageName(), "===========width(px):"+ windth);
		Log.i(activity.getPackageName(), "===========density:"+ density);
		Log.i(activity.getPackageName(), "===========densityDPI:"+ densityDPI);
		return windth;
	}
	
	public static String getSdCardPath() throws Exception {
		if (sdcardPath == null) {

			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdcardPath = Environment.getExternalStorageDirectory()
						.getPath();// 获取跟目录
			}else{
				throw new Exception("-----------sdcard不存在！");
			}
		}
		return sdcardPath;

	}
	
	/**
	 * 向指定的handler发送消息
	 * @param handler
	 * @param what
	 */
	public static void sendMsg(Handler handler,int what){
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	/**
	 * 向指定的handler发送消息
	 * @param handler
	 * @param what
	 */
	public static void sendMsg(Handler handler,int what,String value){
		Message msg = new Message();
		msg.what = what;
		Bundle bundle = new Bundle();
		bundle.putString(MSG_BUNDLE_KEY, value);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	public static void sendMsg(Handler handler,int what,Bundle bundle){
		Message msg = new Message();
		msg.what = what;
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	/**
	 * 将bean中的数据同步到XML中，需要配置XML中View的tag属性（属性值必须为大写）
	 * @param bean
	 * @param instanceBean
	 * @param view
	 * @throws Exception 
	 * @throws SQLException
	 */
	public static <T> void FromBeanToXml(Class<T> bean, Object instanceBean,
			View view) throws Exception{
		Method[] methods = bean.getMethods();
		try {
			for (Method m : methods) {
				String name = m.getName().toUpperCase();
				View childView = null;
				if (name.startsWith("GET")) { 
					childView = view.findViewWithTag(name.substring(3));

				} else if (name.startsWith("IS")) {
					childView = view.findViewWithTag(name.substring(2));
				}

				if (childView != null && childView instanceof TextView) {
					TextView et = (TextView) childView;
					Object getTextFromBean = m.invoke(instanceBean);
					if (getTextFromBean != null
							&& !getTextFromBean.toString().equals(""))
						et.setText(getTextFromBean.toString());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(AndroidUtil.class.toString() + "（FromBeanToXml）:将bean数据同步到XML中时出错");
		} 
	}
	
	/**
	 * 将XML中的数据同步到bean中
	 * 
	 * @param view
	 * @param bean
	 */
	public static <T> Object FromXmlToBean(View view, Class<T> bean,
			Object instanceBean, SimpleDateFormat sdf) {
		// bean.setxxx
		Method[] methods = bean.getMethods();
		// Object instanceBean = null;
		try {
			// instanceBean = bean.newInstance();
			for (Method _m : methods) {
				String name = _m.getName().toUpperCase();
				if (name.startsWith("SET")) {
					View childView = view.findViewWithTag(name.substring(3));
					if (childView != null && childView instanceof EditText) {
						EditText et = (EditText) childView;
						Class<?> c = _m.getParameterTypes()[0];
						String etText = et.getText().toString().trim();
						if (etText == null || "".equals(etText)||"点击输入时间".equals(etText)) {
							_m.invoke(instanceBean, new Object[]{null});
						} else {
							if (c.getName().equals("int")) {
								_m.invoke(instanceBean,
										Integer.parseInt(etText));
							} else if (c.getName().equals("long")) {
								_m.invoke(instanceBean, Long.parseLong(etText));
							} else if (c == Integer.class) {
								_m.invoke(instanceBean,
										(Integer) Integer.parseInt(etText));
							} else if (c == Double.class) {
								_m.invoke(instanceBean,
										(Double) Double.parseDouble(etText));
							} else if (c == Float.class) {
								_m.invoke(instanceBean,
										(Float) Float.parseFloat(etText));
							} else if (c == Long.class) {
								_m.invoke(instanceBean,
										(Long) Long.parseLong(etText));
							} else if (c == String.class) {
								_m.invoke(instanceBean, etText);
							} else if (c == Date.class) {
								String date = etText;
								date = date.replaceFirst("T", " ");
								_m.invoke(instanceBean, sdf.parse(date));
							} else {
								try {
									throw new Exception("Util方法中未找到类型");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

					}
				}
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return instanceBean;

	}
	
	/**
	 * 保存配置
	 * 
	 * @param ct
	 * @param strKey
	 * @param strValue
	 */
	public static boolean saveConfig(Context ct, String strKey, String strValue) {
		SharedPreferences setting = ct.getSharedPreferences("sharedInfo", 0);
		
		Editor editor = setting.edit();
		editor.putString(strKey, strValue);
		return editor.commit();
	}
	
	/**
	 * 获取保存的配置信息
	 * 
	 * @param ct
	 * @param strKey
	 * @return
	 */
	public static String getConfig(Context ct, String strKey) {
		SharedPreferences setting = ct.getSharedPreferences("sharedInfo", 0);
		String strTemp =setting.getString(strKey, ""); 
		return strTemp;
	}
}
