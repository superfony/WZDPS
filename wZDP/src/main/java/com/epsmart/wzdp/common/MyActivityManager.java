package com.epsmart.wzdp.common;

import java.util.Stack;

import android.app.Activity;

public class MyActivityManager { 
    private static Stack<Activity> activityStack; 
    private static MyActivityManager instance; 
    private MyActivityManager() { 
    } 
    public static MyActivityManager getMyActivityManagerInstance() { 
        if (instance == null) { 
            instance = new MyActivityManager(); 
        } 
        return instance; 
    } 
    
    /**
     * 获取栈中窗体数量
     * @param activity
     */
    public int getActivityCount(){
    	if(!activityStack.empty()){
    		return activityStack.size();
    	}else{
    		return 0;
    	}
    }

    /**
     * 退出栈顶Activity 
     */
    public void popActivity(Activity activity) { 
        if (activity != null) { 
           //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作 
            activity.finish(); 
            activityStack.remove(activity);
            activity = null; 
        } 
    } 

    /**
     * 获得当前栈顶Activity 
     */
    public Activity currentActivity() { 
        Activity activity = null; 
       if(!activityStack.empty()) 
         activity= activityStack.lastElement(); 
        return activity; 
    } 
     
    /**
     * 将当前Activity推入栈中 
     */
    public void pushActivity(Activity activity) { 
        if (activityStack == null) { 
            activityStack = new Stack<Activity>(); 
        } 
        activityStack.add(activity); 
    } 

    /**
     * 退出栈中某窗体之后的所有Activity  
     */
    public void popAllActivityAfter(Class<?> cls) { 
        while (true) { 
            Activity activity = currentActivity(); 
            if (activity == null) { 
                break; 
            } 
            if (activity.getClass().equals(cls)) { 
                break; 
            } 
            popActivity(activity); 
        } 
    } 

    /**
     * 退出栈中所有Activity  
     */
    public void popAllActivity() { 
        while (true) { 
            Activity activity = currentActivity(); 
            if (activity == null) { 
                break; 
            } 
            popActivity(activity); 
        } 
        
        System.exit(0);
    } 
} 

