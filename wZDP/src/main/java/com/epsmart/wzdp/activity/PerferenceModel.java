/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.epsmart.wzdp.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
/**
 * @author fang
 */
public class PerferenceModel {
	public static PerferenceModel pm = null;
	protected static Context context;

	public static PerferenceModel getPM(Context context) {
		PerferenceModel.context = context;
		if (pm != null) {
			return pm;
		} else {
			pm = new PerferenceModel();
		}
		return pm;

	}

	/**
	 * �������� SharedPreferences
	 * @param key
	 * @param value
	 */
	public void insertPreference(String key, String value) {
		SharedPreferences perference = null;
		int sdk = VERSION.SDK_INT;
		if(sdk >VERSION_CODES.GINGERBREAD_MR1){
		perference = context.getSharedPreferences("config",
				Context.MODE_MULTI_PROCESS);
		}else{
			perference = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		perference.edit().putString(key, value).commit();
	}

	/**
	 * ȡ����
	 * @param key
	 * @param flag
	 * @return
	 */
	public String getValue(String key, String flag) {
		String value = null;
		SharedPreferences perference = null;
		int sdk = VERSION.SDK_INT;
		if(sdk >VERSION_CODES.GINGERBREAD_MR1){
			perference = context.getSharedPreferences("config",
					Context.MODE_MULTI_PROCESS);
			}else{
				perference = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			}
		value = perference.getString(key, flag);
		return value;
	}
	
	//Context.MODE_WORLD_READABLE
	
	public void insertPf(String key, String value) {
		SharedPreferences perference = null;
		int sdk = VERSION.SDK_INT;
		if(sdk >VERSION_CODES.GINGERBREAD_MR1){
		perference = context.getSharedPreferences("pf",
				Context.MODE_WORLD_READABLE);
		}else{
			perference = context.getSharedPreferences("pf", Context.MODE_WORLD_READABLE);
		}
		perference.edit().putString(key, value).commit();
	}
	
	public String getPf(String pName,String key) {
		Context otherAppsContext;
		String value=null;
		try {
			otherAppsContext = context.createPackageContext(pName,
					Context.CONTEXT_IGNORE_SECURITY);
			SharedPreferences sharedPreferences = otherAppsContext
					.getSharedPreferences("pf", Context.MODE_WORLD_READABLE);
		   value = sharedPreferences.getString(key, "");
		
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return value;
	}

}
