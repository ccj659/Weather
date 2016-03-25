package com.example.entity.weather;

import java.util.Date;

public class Weather_Result {
	
	public String weaid;
	public String days;
	public String  week;
	public String  cityno;
	public String  citynm;
	public String  cityid;
	public String  temperature;
	public String  humidity;
	public String  weather;
	public String  weather_icon;
	public String  weather_icon1;
	public String  wind;
	public String  winp;
	public String  temp_high;
	public String  temp_low;
	public String  humi_high;
	public String  humi_low;
	public String  weatid;
	public String  weatid1;
	public String  windid;
	public String  winpid;
	
	public Weather_Result() {
		super();
	}

	@Override
	public String toString() {
		return   week + ", 温度:" + temperature + "," + weather
				;
	}
	
	

	
	
	
	
	
}
//days	date	鏃ユ湡
//week	string	鏄熸湡
//citynm	string	鍩庡競/鍦板尯
//temperature	string	娓╁害
//cityid	string	姘旇薄缂栧彿
//humidity	string	婀垮害
//weather	string	澶╂皵 璇﹁:weather.wtype
//weather_icon	string	澶╂皵鍥炬爣(鐩綍b/c/d/n渚涗娇鐢�) 涓嬭浇鍥炬爣
//weather_icon1	string	澶╂皵鍥炬爣1(鐩綍b/c/d/n渚涗娇鐢�) 涓嬭浇鍥炬爣
//wind	string	椋庡悜
//winp	string	椋庡姏
//temp_high	number	鏈�楂樻俯搴�
//temp_low	number	鏈�浣庢俯搴�
//humi_high	number	鏈�楂樻箍搴�
//humi_low	number	鏈�浣庢箍搴�
//weatid	number	澶╂皵ID 璇﹁:weather.wtype
//weatid1	number	澶╂皵ID1 璇﹁:weather.wtype
//windid	number	椋庡悜ID
//winpid	number	椋庡姏ID
//"weaid": "100",
//"days": "2015-10-14",
//"week": "鏄熸湡涓�",
//"cityno": "zjtaizhou",
//"citynm": "鍙板窞",
//"cityid": "101210601",
//"temperature": "17鈩�/17鈩�",
//"humidity": "0鈩�/0鈩�",
//"weather": "鏅�",
//"weather_icon": "http://api.k780.com:88/upload/weather/d/0.gif",
//"weather_icon1": "http://api.k780.com:88/upload/weather/n/0.gif",
//"wind": "瑗垮寳椋�",
//"winp": "寰",
//"temp_high": "17",
//"temp_low": "17",
//"humi_high": "0",
//"humi_low": "0",
//"weatid": "1",
//"weatid1": "1",
//"windid": "15",
//"winpid": "125"
