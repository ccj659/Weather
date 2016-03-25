package com.example.entity.weather;

import java.util.ArrayList;
import java.util.List;
//http://v.juhe.cn/weather/geo?format=2&key=您申请的KEY&lon=116.39277&lat=39.933748
public class Weathers {
	
	
	
	
//	    "success": "1",
//	    "result": [
	public int success;
	public List<Weather_Result> result ;
	
	@Override
	public String toString() {
		return "Weathers [success=" + success + ", infos=" + result.toString() + "]";
	}
	public Weathers(int success, ArrayList<Weather_Result> infos) {
		super();
		this.success = success;
		this.result = infos;
	}
	public Weathers() {
		super();
	}
	
}
