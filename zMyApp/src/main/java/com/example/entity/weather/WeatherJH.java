package com.example.entity.weather;

import java.util.ArrayList;

public class WeatherJH {
	public WeatherNow sk;
	public WeatherTodayJH today;
	public ArrayList<WeatherFutureJH> future;
	@Override
	public String toString() {
		return "WeatherJH [sk=" + sk + ", today=" + today + ", future="
				+ future.toString() + "]";
	}
	
	
	
}
