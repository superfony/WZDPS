package com.epsmart.wzdp.util;

import java.io.File;
import java.util.Date;

public class JavaUtil {

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		// 创建单个目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "成功！");
			return false;
		}
	}
	/**
	 *  计算结束时间-开始时间是否大于24小时
	 * @param date1  开始时间
	 * @param date2  结束时间
	 * @return 
	 * @throws Exception
	 */
	 public static boolean compare24(String date1, String date2) throws Exception { 
	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	        java.util.Date start = sdf.parse(date1); 
	        java.util.Date end = sdf.parse(date2); 
	        long cha = end.getTime() - start.getTime(); 
	        double result = cha * 1.0 / (1000 * 60 * 60); 
	        if(result>=24){ 
	             return true; 
	        }else{ 
	             return false; 
	        } 
	 }
	 /**
	  * 两个日期类型进行比较  间隔大于24小时 返回true
	  * @param start
	  * @param end
	  * @return
	  * @throws Exception
	  */
	 public static boolean compare24(Date start, Date end) throws Exception { 
	        long cha = end.getTime() - start.getTime(); 
	        double result = cha * 1.0 / (1000 * 60 * 60); 
	        if(result>=24){ 
	             return true; 
	        }else{ 
	             return false; 
	        } 
	 }
	 

}
