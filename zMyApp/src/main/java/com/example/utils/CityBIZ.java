package com.example.utils;



import java.util.ArrayList;

import com.example.entity.weather.MyCityInfo;
import com.example.entity.weather.WeatherFutureJH;
import com.example.entity.weather.WeatherJH;
import com.example.entity.weather.WeatherNow;
import com.example.entity.weather.WeatherTodayJH;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityBIZ {
	private static final String table = "allcity";
	private static final String table1 = "selectcity";
	private DBHelper dbHelper;
	private SQLiteDatabase database;
	private String tag="biz";
	
	public  CityBIZ (Context context){
		dbHelper = new DBHelper(context, 1);
		database =dbHelper.getWritableDatabase();
	}
	public int  addCity(MyCityInfo cityInfo){
		String nullColumnHack;
		ContentValues values;
		//String sql ="create table allcity (id varchar,province varchar,city varchar,district varchar,ischecked varchar )";
		//cityid varchar,cityno varchar,citynm varchar,weaid varchar
		String sql ="insert into allcity values(?,?,?,?);";
		Log.i("addCity", cityInfo.toString());
		Object[] bindArgs={cityInfo.id,cityInfo.province,cityInfo.city,cityInfo.district};
		database.execSQL(sql, bindArgs);
		
		
		return 0;
		
	} 
	public ArrayList<MyCityInfo> queryCityInfo(){
		ArrayList<MyCityInfo> cityInfos =new ArrayList<MyCityInfo>();
		Cursor c =database.query(table, null, null, null, null, null, null);
		while(c.moveToNext()){
			MyCityInfo cityInfo =new MyCityInfo();
			//cityid varchar,cityno varchar,citynm varchar,weaid varchar
			cityInfo.id=(c.getString(c.getColumnIndex("id")));
			cityInfo.province=(c.getString(c.getColumnIndex("province")));
			cityInfo.city=(c.getString(c.getColumnIndex("city")));
			cityInfo.district=(c.getString(c.getColumnIndex("district")));
			cityInfos.add(cityInfo);
			
		}
		
		Log.i("DB_cityInfos", cityInfos.toString());
		
		c.close();
		
		
		return cityInfos;
		
	}
	public MyCityInfo  checkCity(String cityName){
		String[] selectionArgs={cityName+""};
		MyCityInfo cityInfo =new MyCityInfo();
		Cursor c =database.query("allcity", null, "district=?", selectionArgs, null, null, null);
		if (c.getCount()==0) {
			return null;
		}
		c.moveToFirst();
		cityInfo.id=(c.getString(c.getColumnIndex("id")));
		cityInfo.province=(c.getString(c.getColumnIndex("province")));
		cityInfo.city=(c.getString(c.getColumnIndex("city")));
		cityInfo.district=(c.getString(c.getColumnIndex("district")));
		return cityInfo;
	}
	
	public ArrayList<MyCityInfo> getSelectCity(){
		Cursor c =database.query("table", null, null, null, null, null, null);
		//selectcity
		ArrayList<MyCityInfo> selectCityInfos =new ArrayList<MyCityInfo>();
		while(c.moveToNext()){
			MyCityInfo cityInfo =new MyCityInfo();
			cityInfo.id=(c.getString(c.getColumnIndex("id")));
			cityInfo.province=(c.getString(c.getColumnIndex("province")));
			cityInfo.city=(c.getString(c.getColumnIndex("city")));
			cityInfo.district=(c.getString(c.getColumnIndex("district")));
			selectCityInfos.add(cityInfo);
			
		}
		
		Log.i("selectCityInfos", selectCityInfos.toString());
		
		c.close();
		return selectCityInfos;
		
		
		
	}
	
	public int deleteCity(String id){
		String whereClause="id";
		String[] whereArgs={id};
		database.delete(table1, whereClause, whereArgs);
		
		
		return 1;
	}
	
	
	public int addSelectCityInfo(MyCityInfo cityInfo){
		String sql ="insert into selectcity values(?,?,?,?);";
		Log.i(tag, cityInfo.toString());
		Object[] bindArgs={cityInfo.id,cityInfo.province,cityInfo.city,cityInfo.district};
		database.execSQL(sql, bindArgs);
		
		return 1;
	}
	
	public ArrayList<WeatherJH> getSelectCityWeather(){
		ArrayList<WeatherJH> arrayList =new ArrayList<WeatherJH>();
		Cursor c =database.query("cityInfos", null, null, null, null, null, null);
		
		//temp varchar,wind_strength varchar,humidity varchar,city varchar,,
		//date_y varchar,temperature varchar,dressing_index varchar,dressing_advice varchar,uv_index varchar
		//,wash_index varchar,travel_index varchar,exercise_index varchar,drying_index varchar )"
		while(c.moveToNext()){
			WeatherTodayJH weathertoday =new WeatherTodayJH();
			WeatherNow weatherNow =new WeatherNow();
			WeatherJH weatherJH =new WeatherJH();
			
			//weatherNow.id=(c.getString(c.getColumnIndex("id")));
			weatherNow.temp=(c.getString(c.getColumnIndex("temp")));
			weatherNow.wind_strength=(c.getString(c.getColumnIndex("wind_strength")));
			weatherNow.humidity=(c.getString(c.getColumnIndex("humidity")));
			weathertoday.city=(c.getString(c.getColumnIndex("city")));
			weathertoday.date_y=(c.getString(c.getColumnIndex("date_y")));
			weathertoday.temperature=(c.getString(c.getColumnIndex("temperature")));
			weathertoday.weather=(c.getString(c.getColumnIndex("weather")));
			Log.i("weathertoday", weathertoday.weather);
			weathertoday.dressing_index=(c.getString(c.getColumnIndex("dressing_index")));
			weathertoday.dressing_advice=(c.getString(c.getColumnIndex("dressing_advice")));
			weathertoday.uv_index=(c.getString(c.getColumnIndex("uv_index")));
			weathertoday.wash_index=(c.getString(c.getColumnIndex("wash_index")));
			weathertoday.exercise_index=(c.getString(c.getColumnIndex("exercise_index")));
			weathertoday.drying_index=(c.getString(c.getColumnIndex("drying_index")));

			String[] selectionArgs={weathertoday.city};
			Cursor future =database.query("future", null, "city=?", selectionArgs, null, null, null);

			//Cursor future =database.query("future", null, null, null, null, null, null);
			//selectcity
			
			ArrayList<WeatherFutureJH> weatherFutureJHs =new ArrayList<WeatherFutureJH>();
	//city varchar,temperature varchar,weather varchar,week varchar
			while(future.moveToNext()){
				WeatherFutureJH futureJH =new WeatherFutureJH();
				futureJH.temperature=future.getString(future.getColumnIndex("temperature"));
				futureJH.weather=future.getString(future.getColumnIndex("weather"));
				futureJH.week=future.getString(future.getColumnIndex("week"));
				weatherFutureJHs.add(futureJH);
			}
			
			weatherJH.sk=weatherNow;
			weatherJH.today=weathertoday;
			weatherJH.future=weatherFutureJHs;
			arrayList.add(weatherJH);
			Log.i("weatherJH", "weatherJH"+weatherJH.future);
		}
		
		
		return arrayList;
		
		
	}
	public void deleteFutureByName(String cityName){
		//future (city
		String whereClause="city=?";
		String[] whereArgs={cityName};
		database.delete("future", whereClause, whereArgs);
		
		
	}
	
	
	public void addCityInfo(WeatherJH weatherJH){
		String sql ="insert into future values(?,?,?,?);";
		String sql1 ="insert into cityInfos values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		//Log.i(tag, cityInfo.toString());
		//city varchar,temperature varchar,weather varchar,week varchar
		if (weatherJH==null) {
			return;
			}
		deleteFutureByName(weatherJH.today.city);
		
		for(int i=0;i<weatherJH.future.size();i++){
			WeatherFutureJH futureJH =weatherJH.future.get(i);
			Object[] bindArgs={weatherJH.today.city,futureJH.temperature,futureJH.weather,futureJH.week};
			database.execSQL(sql, bindArgs);
			Log.i("future", "future");
		}
		//temp varchar,wind_strength varchar,humidity varchar,city varchar,
				//date_y varchar,temperature varchar,dressing_index varchar,dressing_advice varchar,uv_index varchar
				//,wash_index varchar,travel_index varchar,exercise_index varchar,drying_index varchar )"
		Object[] bindArg={weatherJH.sk.temp,weatherJH.sk.wind_strength,weatherJH.sk.humidity
				,weatherJH.today.city,weatherJH.today.date_y,weatherJH.today.temperature,weatherJH.today.weather
				,weatherJH.today.dressing_index,weatherJH.today.dressing_advice,weatherJH.today.uv_index
				,weatherJH.today.wash_index,weatherJH.today.travel_index,weatherJH.today.exercise_index,weatherJH.today.drying_index};
		database.execSQL(sql1, bindArg);
		Log.i("weatherJH", "weatherJH");
//		weatherNow.temp=(c.getString(c.getColumnIndex("temp")));
//		weatherNow.wind_strength=(c.getString(c.getColumnIndex("wind_strength")));
//		weatherNow.humidity=(c.getString(c.getColumnIndex("humidity")));
//		weathertoday.city=(c.getString(c.getColumnIndex("city")));
//		weathertoday.date_y=(c.getString(c.getColumnIndex("date_y")));
//		weathertoday.temperature=(c.getString(c.getColumnIndex("temperature")));
//		weathertoday.dressing_index=(c.getString(c.getColumnIndex("dressing_index")));
//		weathertoday.dressing_advice=(c.getString(c.getColumnIndex("dressing_advice")));
//		weathertoday.uv_index=(c.getString(c.getColumnIndex("uv_index")));
//		weathertoday.wash_index=(c.getString(c.getColumnIndex("wash_index")));
//		weathertoday.exercise_index=(c.getString(c.getColumnIndex("exercise_index")));
//		weathertoday.drying_index=(c.getString(c.getColumnIndex("drying_index")));	
		
		
	}
	public void updateCityInfo(String cityName,WeatherJH weatherJH){
		String sql ="insert into future values(?,?,?,?);";
		deleteFutureByName(weatherJH.today.city);
		for(int i=0;i<weatherJH.future.size();i++){
			WeatherFutureJH futureJH =weatherJH.future.get(i);
			Object[] bindArgs={weatherJH.today.city,futureJH.temperature,futureJH.weather,futureJH.week};
			database.execSQL(sql, bindArgs);
			Log.i("future", "future");
		}
		
		ContentValues values =new ContentValues();
		values.put("temp", weatherJH.sk.temp);
		values.put("wind_strength", weatherJH.sk.wind_strength);
		values.put("humidity", weatherJH.sk.humidity);
		values.put("city",weatherJH.today.city );
		values.put("date_y", weatherJH.today.date_y);
		values.put("temperature", weatherJH.today.temperature);
		values.put("weather", weatherJH.today.weather);
		values.put("dressing_index",weatherJH.today.dressing_index );
		values.put("dressing_advice", weatherJH.today.dressing_advice);
		values.put("uv_index", weatherJH.today.uv_index);
		values.put("wash_index", weatherJH.today.wash_index);
		values.put("travel_index", weatherJH.today.travel_index);
		values.put("exercise_index", weatherJH.today.exercise_index);
		values.put("drying_index", weatherJH.today.drying_index);
		
		
		
		String[] whereArgs={cityName};
		
		
		database.update("cityInfos", values, "city=?", whereArgs);
		
	}
	public void deleteCityInfo(String cityName){
		String whereClause="city=?";
		deleteFutureByName(cityName);
		String[] whereArgs={cityName};
		database.delete("cityInfos", whereClause, whereArgs);
		
	}
	
	public   WeatherJH  searchCityInfo(String cityName){
		String[] selectionArgs={cityName};
		Cursor c =database.query("cityInfos", null, "city=?", selectionArgs, null, null, null);
		if(c.getCount()==0){
			return null;
		}
		
		WeatherTodayJH weathertoday =new WeatherTodayJH();
		WeatherNow weatherNow =new WeatherNow();
		WeatherJH weatherJH =new WeatherJH();
		c.moveToFirst();
		//weatherNow.id=(c.getString(c.getColumnIndex("id")));
		weatherNow.temp=(c.getString(c.getColumnIndex("temp")));
		weatherNow.wind_strength=(c.getString(c.getColumnIndex("wind_strength")));
		weatherNow.humidity=(c.getString(c.getColumnIndex("humidity")));
		weathertoday.city=(c.getString(c.getColumnIndex("city")));
		weathertoday.date_y=(c.getString(c.getColumnIndex("date_y")));
		weathertoday.temperature=(c.getString(c.getColumnIndex("temperature")));
		weathertoday.weather=(c.getString(c.getColumnIndex("weather")));
		weathertoday.dressing_index=(c.getString(c.getColumnIndex("dressing_index")));
		weathertoday.dressing_advice=(c.getString(c.getColumnIndex("dressing_advice")));
		weathertoday.uv_index=(c.getString(c.getColumnIndex("uv_index")));
		weathertoday.wash_index=(c.getString(c.getColumnIndex("wash_index")));
		weathertoday.exercise_index=(c.getString(c.getColumnIndex("exercise_index")));
		weathertoday.drying_index=(c.getString(c.getColumnIndex("drying_index")));

		String[] selectionArgss={weathertoday.city};
		Cursor future =database.query("future", null, "city=?", selectionArgss, null, null, null);

		ArrayList<WeatherFutureJH> weatherFutureJHs =new ArrayList<WeatherFutureJH>();
		while(future.moveToNext()){
			WeatherFutureJH futureJH =new WeatherFutureJH();
			futureJH.temperature=future.getString(future.getColumnIndex("temperature"));
			futureJH.weather=future.getString(future.getColumnIndex("weather"));
			futureJH.week=future.getString(future.getColumnIndex("week"));
			weatherFutureJHs.add(futureJH);
		}
		
		weatherJH.sk=weatherNow;
		weatherJH.today=weathertoday;
		weatherJH.future=weatherFutureJHs;
		return weatherJH;
		
	}

	
	
}
