package com.example.entity.weather;

public class WeatherNow {
	public String temp;
	public String wind_direction;
	public String wind_strength;
	public String humidity;
	public String time;
	@Override
	public String toString() {
		return "WeatherNow [temp=" + temp + ", wind_direction="
				+ wind_direction + ", wind_strength=" + wind_strength
				+ ", humidity=" + humidity + ", time=" + time + "]";
	}
	
	
	
	
}



/*"sk": {	当前实况天气
        "temp": "21",	当前温度
        "wind_direction": "西风",	当前风向
        "wind_strength": "2级",	当前风力	
        "humidity": "4%",	当前湿度
        "time": "14:25"	更新时间
    },*/