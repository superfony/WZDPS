package com.epsmart.wzdp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.epsmart.wzdp.R;
/**
 * 日期时间选择控件 
 * 使用方法：
   private EditText inputDate;//需要设置的日期时间文本编辑框
   private String initDateTime="2012年9月3日 14:44",//初始日期时间值 
       在点击事件中使用：
   inputDate.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			DateTimePickDialogUtil dateTimePicKDialog=new DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
			dateTimePicKDialog.dateTimePicKDialog(inputDate);
			
		}
	}); 
   
 * @author 
 * @version 1.0
 */
public class DateTimePickerDialogUtil implements  OnDateChangedListener,OnTimeChangedListener{
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;
	
	/**
	 * 日期时间弹出选择框构造函数
	 * @param activity：调用的父activity
	 * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DateTimePickerDialogUtil(Activity activity,String initDateTime)
	{
		this.activity = activity;
		this.initDateTime=initDateTime;
		
	}
	
	public void init(DatePicker datePicker,TimePicker timePicker)
	{
		Calendar calendar= Calendar.getInstance();
		if(!(null==initDateTime||"".equals(initDateTime)))
		{
			calendar = this.getCalendarByInitData(initDateTime);
		}else
		{
			initDateTime=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
		}
		
		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), DateTimePickerDialogUtil.this );
		//datePicker.init(year, monthOfYear, dayOfMonth, onDateChangedListener)
//		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));//fony
//		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	/**
	 * 弹出日期时间选择框方法
	 * @param inputDate:为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog dateTimePicKDialog(final EditText inputDate){
		LinearLayout dateTimeLayout  = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		timePicker.setIs24HourView(true);
		init(datePicker,timePicker);
		timePicker.setOnTimeChangedListener(this);
				
		ad = new AlertDialog.Builder(activity).setIcon(R.drawable.datetimeicon2).setTitle(initDateTime).setView(dateTimeLayout).setPositiveButton("设置",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								inputDate.setText(dateTime);
							}
						}).setNeutralButton("清空", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								inputDate.setText("");
							}
						}).setNegativeButton("取消",
						null).create();
		ad.setCanceledOnTouchOutside(true);
		ad.show();
		onDateChanged(null, 0, 0, 0);
		return ad;
	}
	private Calendar getCalendarByInitData(String initDateTime){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm");
		Date date;
		try {
			date = sdf.parse(initDateTime);
			calendar.setTime(date);
		} catch (ParseException e) {
			Toast.makeText(activity, "输入的日期格式有误", Toast.LENGTH_LONG).show();
			System.out.println("输入的日期格式有误");
			e.printStackTrace();
			
		}
		
		return calendar;
	

	}
	

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
	{
		onDateChanged(null, 0, 0, 0);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth)
	{
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		dateTime=sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}
	
}

