package com.epsmart.wzdp.common;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.widget.Toast;

import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.PermissionResponse;
import com.epsmart.wzdp.bean.User;
import com.epsmart.wzdp.bean.WorkOrder;

public class PermissHelp {
	public static PermissionResponse permiss;
     /*是否可以点击*/
	public static boolean isPermiss(String code) {
		boolean isMis = false;
		if(permiss==null)
			return false;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("ffinger");
		if(field!=null){
			String flag=field.fieldContent;
			flag=flag!=null?flag:"0";
			isMis=flag.equals("1")?true:false;
		}
		return isMis;
	}
	
	public static String getUserType(String code) {
		String flag=null;
		if(permiss==null)
			return null;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("ffinger");
		if(field!=null){
			 flag=field.fieldContent;
		}
		return flag;
	}
	
	/*增*/
	public static boolean isAdd(String code) {
		boolean isMis = false;
		if(permiss==null)
			return false;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("add");
		if(field!=null){
			String flag=field.fieldContent;
			flag=flag!=null?flag:"0";
			isMis=flag.equals("1")?true:false;
		}
		return isMis;
	}
	
	/*删*/
	
	public static boolean is(String code) {
		boolean isMis = false;
		if(permiss==null)
			return false;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("del");
		if(field!=null){
			String flag=field.fieldContent;
			flag=flag!=null?flag:"0";
			isMis=flag.equals("1")?true:false;
		}
		return isMis;
	}
	/*改*/
	
	public static boolean isUpd(String code) {
		boolean isMis = false;
		if(permiss==null)
			return false;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("upd");
		if(field!=null){
			String flag=field.fieldContent;
			flag=flag!=null?flag:"0";
			isMis=flag.equals("1")?true:false;
		}
		return isMis;
	}
	/*查*/
	
	public static boolean isQuery(String code) {
		boolean isMis = false; 
		if(permiss==null)
			return false;
		WorkOrder worder=permiss.workOrders.get(code);
		Field  field=worder.fields.get("query");
		if(field!=null){
			String flag=field.fieldContent;
			flag=flag!=null?flag:"0";
			isMis=flag.equals("1")?true:false;
		}
		return isMis;
	}
	
	 public static void showToast(Activity activity){
		 Toast.makeText(activity, "没有权限", Toast.LENGTH_LONG).show();
	 }

	 public  static User getUser(String userInfoAll) throws JSONException{
		 if(userInfoAll==null)
			 return null;
		 User user=new User();
		 JSONObject jo=new JSONObject(userInfoAll);
		int uid= Integer.parseInt(jo.getString("usernamecode"));
		 user.setUid(uid);
		 String name=jo.getString("username");
		 user.setName(name);
		 String userpwd=jo.getString("userpwd");
		 user.setPwd(userpwd);
		 String systemID=jo.getString("systemID");
		 user.setLocation(systemID);
		 String passowrd=jo.getString("password");
		 user.setFace(passowrd);

		 return user;
	 }
	

}
