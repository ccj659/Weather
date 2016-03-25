package com.example.apitestapp;

import java.util.ArrayList;
import java.util.List;

import com.example.service.WeatherUpdateService;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AppWidget extends AppWidgetProvider {
	String UpdateTime ="com.qlf.openclock";//打开闹钟
	String openCalendar ="com.qlf.opencalendar";//打开日历
	String changeCity="com.qlf.changecity";//改变城市
	String openMyActivity="com.qlf.openmyweather";//打开天气应用
	String upTime="android.intent.action.TIME_SET";//更新系统时间
	private final String updateWeather = "com.qlf.appWidgetUpdate";//更新天气
	private Context context;
	private List<PackageInfo> allPackageInfos;
	private ArrayList<PackageInfo> sysPackageInfos;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context=context;
		super.onReceive(context, intent);
		Log.i("AppWidget", intent.getAction());
		String action = intent.getAction();
		if (action.equals("android.intent.action.SCREEN_ON")) {
			context.startService(new Intent(context, WeatherUpdateService.class));
		}
		
		if (action.equals("android.intent.action.USER_PRESENT")) {// 用户唤醒设备时启动服务
			context.startService(new Intent(context, WeatherUpdateService.class));
		} else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			context.startService(new Intent(context, WeatherUpdateService.class));
		}
		
		
		 if (intent.getAction().equals(openCalendar))
	      {         Log.i("AppWidget", intent.getAction());         
	          //只能通过远程对象来设置appwidget中的控件状态
	          RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	          	intent.setComponent((new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity")));
		        context.startActivity(intent);
	          
	          
	          
	          //获得appwidget管理实例，用于管理appwidget以便进行更新操作
	          AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	           
	          //相当于获得所有本程序创建的appwidget
	          ComponentName componentName = new ComponentName(context,AppWidget.class);
	           
	          //更新appwidget
	          appWidgetManager.updateAppWidget(componentName, remoteViews);
	      }
		 if (intent.getAction().equals(changeCity))//改变城市
	      {              
	      }
		 if (intent.getAction().equals(openMyActivity))//打开天气应用
	      {            Log.i("AppWidget", intent.getAction());      
	          //只能通过远程对象来设置appwidget中的控件状态
	          RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	          //获得appwidget管理实例，用于管理appwidget以便进行更新操作
	          AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	          //相当于获得所有本程序创建的appwidget
	          ComponentName componentName = new ComponentName(context,AppWidget.class);
	          //更新appwidget
	          appWidgetManager.updateAppWidget(componentName, remoteViews);
	      }
		
		 
		 if (intent.getAction().equals( openMyActivity))//打开天气应用
	      {         Log.i("AppWidget", intent.getAction());         
	          //只能通过远程对象来设置appwidget中的控件状态
	          RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	          intent.setComponent(new ComponentName("com.example.apitestapp", "com.example.apitestapp.MainActivity"));   
	          context.startActivity(intent);
	          AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	          ComponentName componentName = new ComponentName(context,AppWidget.class);
	          appWidgetManager.updateAppWidget(componentName, remoteViews);
	      }
		 
		 if (intent.getAction().equals( upTime))//更新时间
	      {           Log.i("AppWidget", intent.getAction());       
	          RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	          AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	          ComponentName componentName = new ComponentName(context,AppWidget.class);
	          appWidgetManager.updateAppWidget(componentName, remoteViews);
	      }
		 
		 
		 
		 
		 if (intent.getAction().equals(updateWeather))
	      {             Log.i("AppWidget", intent.getAction());     
	          //只能通过远程对象来设置appwidget中的控件状态
	          RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	          Toast.makeText(context, "正在更新", Toast.LENGTH_SHORT).show();  
	         //通过远程对象将按钮的文字设置为”hihi”
	      }
		 
		 
		 
	      super.onReceive(context, intent);
		
	}


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//registerOnClick(context, appWidgetManager, appWidgetIds);
		
		 
	        Intent intentss = new Intent(context, WeatherUpdateService.class);
	        Log.i("AppWidget", "intentss");
	        context.startService(intentss);
	        Log.i("AppWidget", "intentss");
		
		 Intent intent = new Intent();
	     intent.setAction(UpdateTime);
	        //设置pendingIntent的作用
	        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
	        RemoteViews remoteViews  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	        //绑定事件
	        remoteViews.setOnClickPendingIntent(R.id.TimeLeftHotArea, pendingIntent);
	        //更新Appwidget
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	        
	        
	        Intent intent1 = new Intent();
	        intent1.setAction(openCalendar);
	        //设置pendingIntent的作用
	        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
	        RemoteViews remoteViews1  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	        //绑定事件
	        remoteViews1.setOnClickPendingIntent(R.id.TimeRightHotArea, pendingIntent1);
	        //更新Appwidget
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews1);
	        
	        Intent intent11 = new Intent();
	        intent11.setAction(changeCity);
	        //设置pendingIntent的作用
	        PendingIntent pendingIntent11 = PendingIntent.getBroadcast(context, 0, intent11, 0);
	        RemoteViews remoteViews11  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	        //绑定事件
	        remoteViews11.setOnClickPendingIntent(R.id.TextInfoLeftHotArea, pendingIntent11);
	        //更新Appwidget
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews11);
	        
	        
	        
	        Intent intent111 = new Intent();
	        intent111.setAction(openMyActivity);
	        //设置pendingIntent的作用
	        PendingIntent pendingIntent111 = PendingIntent.getBroadcast(context, 0, intent111, 0);
	        RemoteViews remoteViews111  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	        //绑定事件
	        remoteViews111.setOnClickPendingIntent(R.id.WeatherIconHotArea, pendingIntent111);
	        //更新Appwidget
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews111);
	        
	        Intent intent1111 = new Intent();
	        intent1111.setAction(updateWeather);
	        //设置pendingIntent的作用
	        PendingIntent pendingIntent1111 = PendingIntent.getBroadcast(context, 0, intent1111, 0);
	        RemoteViews remoteViews1111  = new RemoteViews(context.getPackageName(),R.layout.widget_4x2);
	        //绑定事件
	        remoteViews1111.setOnClickPendingIntent(R.id.TextInfoRightHotArea, pendingIntent1111);
	        //更新Appwidget
	        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews1111);
		
	}
	


	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}
	
	
	
	/**
	  * 过滤出系统应用
	  */
	 private void getSystemApp()
	 {

	  allPackageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES); // 取得系统安装所有软件信息
	  sysPackageInfos = new ArrayList<PackageInfo>();

	  if (allPackageInfos != null && !allPackageInfos.isEmpty())
	  {
	   for (PackageInfo apckageInfo : allPackageInfos)
	   {
	    ApplicationInfo appInfo = apckageInfo.applicationInfo;// 得到每个软件信息
	    if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
	    {
	     sysPackageInfos.add(apckageInfo);// 系统软件
	    }
	   }
	  }
	 }

	 /**
	  * 启动系统闹钟
	  */
	 private void startSystemAlarm()
	 {
	  getSystemApp();

	  String activityName = "";
	  String packageName = "";
	  String alarmPackageName = "";
	  for (int i = 0; i < sysPackageInfos.size(); i++)
	  {
	   PackageInfo packageInfo = sysPackageInfos.get(i);
	   packageName = packageInfo.packageName;
	   // 包名中包含clock
	   if (packageName.indexOf("Alarm") != -1)
	   {
	    if (!(packageName.indexOf("DeskClock") != -1))
	    {
	     ActivityInfo activityInfo = packageInfo.activities[0];
	     // activity名中包含 Alarm和 DeskClock 大部分的闹钟程序名中都是按照这种规则命名 不能保证所有闹钟都是这样的
	     if (activityInfo.name.indexOf("Alarm") != -1 || activityInfo.name.indexOf("DeskClock") != -1)
	     {
	      activityName = activityInfo.name;
	      alarmPackageName = packageName;
	     }
	    }
	   }
	  }
	  
	  if ((activityName != "") && (alarmPackageName != ""))
		  
	  {
	   Intent intent = new Intent();
//	   /startSystemAlarm()
	   //com.android.alarmclock~com.meizu.flyme.alarmclock.AlarmClock

	   Log.i("AppWidget", alarmPackageName+"~"+activityName);
	   intent.setComponent(new ComponentName(alarmPackageName, activityName));
	   context.startActivity(intent);
	  }
	  else
	  {
	   Toast.makeText(context, "启动系统闹钟失败！", Toast.LENGTH_SHORT).show();
	  }
	 }

	
	
	
	
	
	
	
	

}
