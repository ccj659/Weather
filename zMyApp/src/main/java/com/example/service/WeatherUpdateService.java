package com.example.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apitestapp.AppWidget;
import com.example.apitestapp.CityContainer;
import com.example.apitestapp.MainActivity;
import com.example.apitestapp.R;
import com.example.entity.weather.WeatherFutureJH;
import com.example.entity.weather.WeatherJH;
import com.example.utils.CityBIZ;
import com.example.utils.MatchWeatherIco;
import com.google.gson.Gson;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherUpdateService extends Service {
	private static TreeMap<String, Integer> treeMapICo;
	private final String updateWeather = "com.qlf.appWidgetUpdate";// 更新天气
	String UpdateTime = "com.qlf.openclock";// 打开闹钟
	String openCalendar = "com.qlf.opencalendar";// 打开日历
	String changeCity = "com.qlf.changecity";// 改变城市
	String openMyActivity = "com.qlf.openmyweather";// 打开天气应用
	String upTime = "android.intent.action.TIME_SET";// 更新系统时间
	private int[] timesImg = { R.drawable.nw0, R.drawable.nw1, R.drawable.nw2,
			R.drawable.nw3, R.drawable.nw4, R.drawable.nw5, R.drawable.nw6,
			R.drawable.nw7, R.drawable.nw8, R.drawable.nw9, };
	private int[] dateViews = { R.id.h_left, R.id.h_right, R.id.m_left,
			R.id.m_right };

	RemoteViews remoteViews;
	CityBIZ cityBIZ;
	ArrayList<WeatherJH> weatherJHs;
	ArrayList<String> cityNames;
	WeatherJH weatherJH;
	WeatherFutureJH futureJH;
	private Object URL_ByCity;
	private Object URL_PM;
	private String nowCity;
	private int nowCityIndex;
	private RequestQueue queue;
	private HashMap<String, Integer> mWidgetWeatherIcon;
	private SimpleDateFormat df = new SimpleDateFormat("HHmm");
	SharedPreferences prefereCity ;
	SharedPreferences prefereUser ;
	private String defalutCity;
	private WeatherJH searchCityInfo;
	private boolean sendIsCheck=true;
	private boolean animatIsCheck=true;
	private boolean notifyIsCheck=true;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("AppWidget", "onCreate");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("AppWidget", "WeatherUpdateService");
		
//		View view =View.inflate(getApplicationContext(), R.layout.widget_4x2, null);
//		View back = view.findViewById(R.id.widgetBkgLayer);
//		back.setVisibility(View.INVISIBLE);
		
		
		treeMapICo = new TreeMap<String, Integer>();
		remoteViews = new RemoteViews(getApplication().getPackageName(),
				R.layout.widget_4x2);
		cityBIZ = new CityBIZ(this);
		prefereCity=getSharedPreferences("city", WeatherUpdateService.MODE_PRIVATE);//保存默认城市
		prefereUser=getSharedPreferences("user", WeatherUpdateService.MODE_PRIVATE);//保存默认城市
		
		
		
		
		defalutCity=prefereCity.getString("cityName", null);
		if(defalutCity!=null){
			 searchCityInfo = cityBIZ.searchCityInfo(defalutCity);
		}
		
		weatherJHs = new ArrayList<WeatherJH>();
		initWidgetWeather();
		cityNames = new ArrayList<String>();
		
		//startNotification();
		
		sendIsCheck=prefereUser.getBoolean("sendIsCheck", true);
		
		notifyIsCheck=prefereUser.getBoolean("notifyIsCheck", true);
		if (sendIsCheck) {//打开推送
			
		}
		if (animatIsCheck) {//打开动画
			
		}
		if (notifyIsCheck) {//打开通知
			pendingNotification();
		}
		Log.i("sendIsCheck", sendIsCheck+"s"+animatIsCheck+"ss"+notifyIsCheck);
		
		
		
		
		weatherJHs = cityBIZ.getSelectCityWeather();
		for (int i = 0; i < weatherJHs.size(); i++) {
			cityNames.add(weatherJHs.get(i).today.city);
		}
		
		MyReciver recever = new MyReciver();
		IntentFilter updateIntent = new IntentFilter();
		updateIntent.addAction(changeCity);
		updateIntent.addAction(updateWeather);
		updateIntent.addAction("android.intent.action.TIME_TICK");
		updateIntent.addAction("android.intent.action.TIME_SET");
		updateIntent.addAction("android.intent.action.DATE_CHANGED");
		updateIntent.addAction("android.intent.action.TIMEZONE_CHANGED");
		registerReceiver(recever, updateIntent);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public PendingIntent getDefalutIntent(int flags) {
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				new Intent(), flags);
		return pendingIntent;
	}
	
	public void pendingNotification() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.org3_ww0);
		
		  int icon = R.drawable.ic_launcher;  
	        CharSequence tickerText = "Notification01"; 
	        SimpleDateFormat dateFormat =new SimpleDateFormat("HH:mm");
	        String format = dateFormat.format(new Date(System.currentTimeMillis()));
	    long when = System.currentTimeMillis();  
		Notification notification = new Notification(icon, tickerText, when);  
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
	                 R.layout.notification_layout);
		//remoteViews.setImageViewResource(view.getId(), R.drawable.ic_weather_snow_bg);  
		if (searchCityInfo==null) {
			return;
		}
		String key = searchCityInfo.today.weather;
		try {
			remoteViews.setImageViewResource(R.id.iv_weather,
					mWidgetWeatherIcon.get(key));
		} catch (Exception e) {
			remoteViews.setImageViewResource(R.id.iv_weather,R.drawable.w1);
		}
		remoteViews.setTextViewText(R.id.tv_no_weather, searchCityInfo.today.weather); 
		remoteViews.setTextViewText(R.id.iv_no_city, searchCityInfo.today.city); 
		remoteViews.setTextViewText(R.id.tv_no_time, format); 
		remoteViews.setTextViewText(R.id.tv_no_temper, searchCityInfo.today.temperature); 
		  // remoteViews.setTextViewText(R.id.text, "This is a custom layout");  
		   notification.contentView = remoteViews;  
		   
		   Intent notificationIntent = new Intent(this, MainActivity.class);  
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);  
	        notification.contentIntent = contentIntent;  
	        notification.flags = Notification.FLAG_NO_CLEAR;  //永久存在，不能删除  
	        String ns = Context.NOTIFICATION_SERVICE;  
	        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);  
	        mNotificationManager.notify(1, notification);  

	}

	class MyReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			
			if (action.equals(updateWeather)) {
				updateWeather();
				
			} else if (action.equals(changeCity)) {
				Log.i("cityNames", "cityNames");
				changeCity();
			} else {

				updateTime();

			}
		}
	}

	public void updateWeather() {
		volleyRequest(nowCity);

	}

	public void updateTime() {
		// 定义SimpleDateFormat对象
		df = new SimpleDateFormat("HHmm");
		// 将当前时间格式化成HHmm的形式
		String timeStr = df.format(new Date(System.currentTimeMillis()));

		for (int i = 0; i < timeStr.length(); i++) {
			// 将第i个数字字符转换为对应的数字
			int num2 = Integer.parseInt(timeStr.substring(i, i + 1));
			// 将第i个图片的设为对应的数字图片
			// Log.i("WeatherWidget", "时间：" + num2);
			remoteViews.setImageViewResource(dateViews[i], timesImg[num2]);
		}

		remoteViews.setImageViewResource(R.id.am_pm,
				R.drawable.switch_camera_hide);

		// remoteViews.setTextViewText(R.id.weather_icon_left, "深圳" + "\r\n"
		// + "阵雨 30");
		// remoteViews.setTextViewText(R.id.weather_icon_right, "07/02 周二"
		// + "\r\n" + "五月二五");
		ComponentName componentName = new ComponentName(getApplication(),
				AppWidget.class);
		AppWidgetManager.getInstance(getApplication()).updateAppWidget(
				componentName, remoteViews);
	}

	public void changeCity() {
		if (cityNames.size() == 0) {
			return;
		}
		if (nowCityIndex > cityNames.size() - 1) {
			nowCityIndex = 0;
		}
		Log.i("cityNames", nowCityIndex + "");

		try {
			nowCity = cityNames.get(nowCityIndex);
		} catch (Exception e) {
			nowCity = cityNames.get(0);
			nowCityIndex = 0;
		}
		nowCityIndex++;
		RemoteViews remoteViews = new RemoteViews(getApplication()
				.getPackageName(), R.layout.widget_4x2);
		remoteViews.setTextViewText(R.id.weather_icon_cityWeather, nowCity);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(WeatherUpdateService.this);
		ComponentName componentName = new ComponentName(
				WeatherUpdateService.this, AppWidget.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
		volleyRequest(nowCity);
		Log.i("cityNames", nowCity);

	}

	public String volleyRequest(String city) {
		// 新数据
		// URL_ByCity="http://v.juhe.cn/weather/index?format=2&cityname=+"+city+"+&key=31aaba2ab7cd6063769f8133908c93aa";
		// 老数据
		URL_ByCity = "http://v.juhe.cn/weather/index?format=2&cityname=+"
				+ city + "+&key=5bb5f443a097fb29106d68afda090e9c";
		// URL_PM =
		// "http://v.juhe.cn/weather/geo?format=2&key=d9b6c9014d643f9821b1bd0b55398b77&lon="+lon+"&lat="+lat;

		queue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest((String) URL_ByCity,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							Log.i("URL", response);
							JSONObject object = new JSONObject(response);
							ArrayList<WeatherJH> selectCityWeather2 = cityBIZ
									.getSelectCityWeather();
							ArrayList<String> list = new ArrayList<String>();
							for (int i = 0; i < selectCityWeather2.size(); i++) {
								list.add(selectCityWeather2.get(i).today.city);
							}
							Log.i("WeatherJH", response.toString());
							if (response.toString().length() < 100) { // 判断网络是否错误
								Log.i("WeatherJH", nowCity);

								for (int i = 0; i < list.size(); i++) {
									Log.i("WeatherJH", nowCity);
									if (list.get(i).equals(nowCity)) {
										weatherJH = selectCityWeather2.get(i);
										Log.i("WeatherJH", weatherJH.toString());
									}
								}
							} else {
								JSONObject result = object
										.getJSONObject("result");
								Log.i("result", result.toString());
								Gson gson = new Gson();
								weatherJH = gson.fromJson(result.toString(),
										WeatherJH.class);
							}
							if (list.contains(nowCity)) { // 判断 城市列表是否存在该城市
								cityBIZ.updateCityInfo(nowCity, weatherJH);
								// Toast.makeText(this, "正在更新城市",
								// Toast.LENGTH_SHORT).show();
							} else {

							}

							matchAppWidgetInfo();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("WeatherJH", "错误");
						ArrayList<WeatherJH> selectCityWeather2 = cityBIZ
								.getSelectCityWeather();
						ArrayList<String> list = new ArrayList<String>();
						for (int i = 0; i < selectCityWeather2.size(); i++) {
							list.add(selectCityWeather2.get(i).today.city);
						}
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).equals(nowCity)) {
								weatherJH = selectCityWeather2.get(i);
							}
						}
						matchAppWidgetInfo();
					}

				});
		queue.add(stringRequest);

		return null;

	}

	public void getBigIco() {

		treeMapICo.put("晴", R.drawable.org3_ww0);
		treeMapICo.put("多云", R.drawable.org3_ww1);
		treeMapICo.put("阴", R.drawable.org3_ww2);
		treeMapICo.put("阵雨", R.drawable.org3_ww3);
		treeMapICo.put("雷阵雨", R.drawable.org3_ww4);
		treeMapICo.put("雷阵雨伴有冰雹", R.drawable.org3_ww5);
		treeMapICo.put("雨夹雪", R.drawable.org3_ww6);
		treeMapICo.put("小雨", R.drawable.org3_ww7);
		treeMapICo.put("中雨", R.drawable.org3_ww8);
		treeMapICo.put("大雨", R.drawable.org3_ww9);
		treeMapICo.put("暴雨", R.drawable.org3_ww10);
		treeMapICo.put("大暴雨", R.drawable.org3_ww10);
		treeMapICo.put("特大暴雨", R.drawable.org3_ww10);
		treeMapICo.put("阵雪", R.drawable.org3_ww13);
		treeMapICo.put("小雪", R.drawable.org3_ww14);
		treeMapICo.put("中雪", R.drawable.org3_ww15);
		treeMapICo.put("大雪", R.drawable.org3_ww16);
		treeMapICo.put("暴雪", R.drawable.org3_ww17);
		treeMapICo.put("雾", R.drawable.org3_ww18);
		treeMapICo.put("冻雨", R.drawable.org3_ww19);
		treeMapICo.put("沙尘暴", R.drawable.org3_ww20);
		treeMapICo.put("小雨转中雨", R.drawable.org3_ww8);
		treeMapICo.put("中雨转小雨", R.drawable.org3_ww8);
		treeMapICo.put("中雨转大雨", R.drawable.org3_ww8);
		treeMapICo.put("大雨转暴雨", R.drawable.org3_ww9);
		treeMapICo.put("暴雨-大暴雨", R.drawable.org3_ww10);
		treeMapICo.put("浮尘", R.drawable.org3_ww20);
		treeMapICo.put("扬沙", R.drawable.org3_ww20);
		treeMapICo.put("强沙尘暴", R.drawable.org3_ww20);
		treeMapICo.put("霾", R.drawable.org3_ww20);
		treeMapICo.put("多云转阴", R.drawable.org3_ww1);
		treeMapICo.put("阴转小雨", R.drawable.org3_ww7);
		treeMapICo.put("小雨转多云", R.drawable.org3_ww3);

	}

	public void initWidgetWeather() {
		mWidgetWeatherIcon = new HashMap<String, Integer>();
		mWidgetWeatherIcon.put("暴雪", R.drawable.w17);
		mWidgetWeatherIcon.put("暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("大暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("大雪", R.drawable.w16);
		mWidgetWeatherIcon.put("大雨", R.drawable.w9);
		mWidgetWeatherIcon.put("多云转阴", R.drawable.w1);
		mWidgetWeatherIcon.put("阴转小雨", R.drawable.w7);
		mWidgetWeatherIcon.put("小雨转多云", R.drawable.w3);
		mWidgetWeatherIcon.put("晴转霾", R.drawable.w1);
		mWidgetWeatherIcon.put("多云", R.drawable.w1);
		mWidgetWeatherIcon.put("雷阵雨", R.drawable.w4);
		mWidgetWeatherIcon.put("雷阵雨冰雹", R.drawable.w19);
		mWidgetWeatherIcon.put("晴", R.drawable.w0);
		mWidgetWeatherIcon.put("沙尘暴", R.drawable.w20);
		mWidgetWeatherIcon.put("小雨转中雨", R.drawable.w10);
		mWidgetWeatherIcon.put("中雨转小雨", R.drawable.w10);
		mWidgetWeatherIcon.put("中雨转大雨", R.drawable.w10);
		mWidgetWeatherIcon.put("大雨转暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("特大暴雨", R.drawable.w10);
		mWidgetWeatherIcon.put("雾", R.drawable.w18);
		mWidgetWeatherIcon.put("小雪", R.drawable.w14);
		mWidgetWeatherIcon.put("小雨", R.drawable.w7);
		mWidgetWeatherIcon.put("阴", R.drawable.w2);

		mWidgetWeatherIcon.put("雨夹雪", R.drawable.w6);
		mWidgetWeatherIcon.put("阵雪", R.drawable.w13);
		mWidgetWeatherIcon.put("阵雨", R.drawable.w3);
		mWidgetWeatherIcon.put("中雪", R.drawable.w15);
		mWidgetWeatherIcon.put("中雨", R.drawable.w8);

	}

	public void matchAppWidgetInfo() {
		// 选择图片
		int integer = R.drawable.w1;
		String weather = weatherJH.today.weather;
		Log.i("integer", weather);
		try {
			integer = mWidgetWeatherIcon.get(weather);
		} catch (Exception e) {
			integer = R.drawable.w1;
		}
		Log.i("integer", mWidgetWeatherIcon.get(weather) + "");
		String timeStr = df.format(new Date(System.currentTimeMillis()));
		int num2 = Integer.parseInt(timeStr);
		if (num2 > 1800) {
			if (weather.contains("云")) {
				integer = R.drawable.w31;
			} else if (weather.contains("雨")) {
				integer = R.drawable.w33;
			} else if (weather.contains("雪")) {
				integer = R.drawable.w34;
			} else {
				integer = R.drawable.w30;
			}
		}
		RemoteViews remoteViews = new RemoteViews(getApplication()
				.getPackageName(), R.layout.widget_4x2);
		remoteViews.setImageViewResource(R.id.weather_icon, integer);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(WeatherUpdateService.this);
		ComponentName componentName = new ComponentName(
				WeatherUpdateService.this, AppWidget.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);

		RemoteViews remoteViews1 = new RemoteViews(getApplication()
				.getPackageName(), R.layout.widget_4x2);
		remoteViews1.setTextViewText(R.id.weather_icon_Weather,
				weatherJH.sk.temp + "℃" + " " + weatherJH.today.weather);
		AppWidgetManager appWidgetManager1 = AppWidgetManager
				.getInstance(WeatherUpdateService.this);
		ComponentName componentName1 = new ComponentName(
				WeatherUpdateService.this, AppWidget.class);
		appWidgetManager1.updateAppWidget(componentName1, remoteViews1);

		RemoteViews remoteViews11 = new RemoteViews(getApplication()
				.getPackageName(), R.layout.widget_4x2);
		remoteViews11.setTextViewText(R.id.weather_icon_calendar,
				weatherJH.today.date_y);
		AppWidgetManager appWidgetManager11 = AppWidgetManager
				.getInstance(WeatherUpdateService.this);
		ComponentName componentName11 = new ComponentName(
				WeatherUpdateService.this, AppWidget.class);
		appWidgetManager11.updateAppWidget(componentName11, remoteViews11);
	}

}
