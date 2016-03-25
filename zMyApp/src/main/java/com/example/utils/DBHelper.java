package com.example.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="database.db";
	private static final String TABLE_NAME="city";
	SQLiteDatabase database ;
	
	
	public DBHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
		// TODO Auto-generated constructor stub
		
	}
//
//	public String temp;//now
//	public String wind_direction;
//	public String wind_strength;
//	public String humidity;
//	public String time;
//	public String city;
//	public String date_y;
//	public String temperature;
//	public String weather;
//	public String wind;           // dressing_advice varchar,uv_index varchar,wash_index varchar,travel_index varchar,exercise_index varchar,drying_index varchar
//	public String dressing_index;
//	public String dressing_advice;
//	public String uv_index;
//	public String wash_index;
//	public String travel_index;
//	public String exercise_index;
//	public String drying_index;
//	public String temperature;
//	public String weather;
//	public String wind;
//	public String week;
//	public String date;
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql ="create table allcity (id varchar,province varchar,city varchar,district varchar )";
		String sqlcity ="create table cityInfos (temp varchar,wind_strength varchar,humidity varchar,city varchar,date_y varchar,temperature varchar,weather varchar,dressing_index varchar,dressing_advice varchar,uv_index varchar,wash_index varchar,travel_index varchar,exercise_index varchar,drying_index varchar );";
	
		String future ="create table future (city varchar,temperature varchar,weather varchar,week varchar );";
		db.execSQL(sql);
		db.execSQL(sqlcity);
		db.execSQL(future);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
