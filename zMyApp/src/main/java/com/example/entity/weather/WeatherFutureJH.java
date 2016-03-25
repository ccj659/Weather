package com.example.entity.weather;

public class WeatherFutureJH {
	public String temperature;
	public String weather;
	public String wind;
	public String week;
	public String date;
	@Override
	public String toString() {
		return "WeatherFutureJH [temperature=" + temperature + ", weather="
				+ weather + ", wind=" + wind + ", week=" + week + ", date="
				+ date + "]";
	}

	
}


/*"temperature": "8℃~20℃",
"weather": "晴转霾",
"weather_id": {
    "fa": "00",
    "fb": "53"
},
"wind": "西南风微风",
"week": "星期五",
"date": "20140321"*/