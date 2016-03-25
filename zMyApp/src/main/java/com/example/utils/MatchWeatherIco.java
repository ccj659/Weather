package com.example.utils;

import com.example.apitestapp.R;

import java.util.TreeMap;


public   class MatchWeatherIco {
	
	


	public static TreeMap<String, Integer> getBigIco (){
		TreeMap<String, Integer> treeMapICo=new TreeMap<String, Integer>();
		treeMapICo.put("晴", R.drawable.org3_ww0);
		treeMapICo.put("晴转多云", R.drawable.org3_ww1);
		treeMapICo.put("多云转阴", R.drawable.org3_ww1);
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
		treeMapICo.put("阴转小雨", R.drawable.org3_ww7);
		treeMapICo.put("小雨转多云", R.drawable.org3_ww3);


		return treeMapICo;


	}
	
	public  TreeMap<String, Integer> getSmallIco (){
		TreeMap<String, Integer> treeMapICo =new TreeMap<String, Integer>();
		treeMapICo.put("多云转阴", R.drawable.ww1);
		treeMapICo.put("阴转小雨", R.drawable.ww7);
		treeMapICo.put("小雨转多云", R.drawable.ww3);
		treeMapICo.put("晴", R.drawable.ww0);
		treeMapICo.put("多云", R.drawable.ww1);
		treeMapICo.put("阴", R.drawable.ww2);
		treeMapICo.put("阵雨", R.drawable.ww3);
		treeMapICo.put("雷阵雨", R.drawable.ww4);
		treeMapICo.put("雷阵雨伴有冰雹", R.drawable.ww5);
		treeMapICo.put("雨夹雪", R.drawable.ww6);
		treeMapICo.put("小雨", R.drawable.ww7);
		treeMapICo.put("中雨", R.drawable.ww8);
		treeMapICo.put("大雨", R.drawable.ww9);
		treeMapICo.put("暴雨", R.drawable.ww10);
		treeMapICo.put("大暴雨", R.drawable.ww10);
		treeMapICo.put("特大暴雨", R.drawable.ww10);
		treeMapICo.put("阵雪", R.drawable.ww13);
		treeMapICo.put("小雪", R.drawable.ww14);
		treeMapICo.put("中雪", R.drawable.ww15);
		treeMapICo.put("大雪", R.drawable.ww16);
		treeMapICo.put("暴雪", R.drawable.ww17);
		treeMapICo.put("雾", R.drawable.ww18);
		treeMapICo.put("冻雨", R.drawable.ww19);
		treeMapICo.put("沙尘暴", R.drawable.ww20);
		treeMapICo.put("小雨转中雨", R.drawable.ww8);
		treeMapICo.put("中雨转小雨", R.drawable.ww8);
		treeMapICo.put("中雨转大雨", R.drawable.ww8);
		treeMapICo.put("大雨转暴雨", R.drawable.ww9);
		treeMapICo.put("小雨-中雨", R.drawable.ww7);
		treeMapICo.put("中雨-大雨", R.drawable.ww8);
		treeMapICo.put("大雨-暴雨", R.drawable.ww9);
		treeMapICo.put("暴雨-大暴雨", R.drawable.ww10);
		treeMapICo.put("浮尘", R.drawable.ww20);
		treeMapICo.put("扬沙", R.drawable.ww20);
		treeMapICo.put("强沙尘暴", R.drawable.ww20);
		treeMapICo.put("霾", R.drawable.ww20);
		
		
		
		
		return treeMapICo;
		
		
	}
	
	
	public  TreeMap<String, Integer> getTodayAnimat (){
		TreeMap<String, Integer> treeMapICo =new TreeMap<String, Integer>();
		treeMapICo.put("晴",0);
		treeMapICo.put("多云",1);
		treeMapICo.put("阴", 2);
		treeMapICo.put("阵雨", 3);
		treeMapICo.put("雷阵雨", 4);
		treeMapICo.put("雷阵雨伴有冰雹", 5);
		treeMapICo.put("雨夹雪", 6);
		treeMapICo.put("小雨", 7);
		treeMapICo.put("中雨", 8);
		treeMapICo.put("大雨", 9);
		treeMapICo.put("暴雨", 10);
		treeMapICo.put("大暴雨", 10);
		treeMapICo.put("特大暴雨", 10);
		treeMapICo.put("阵雪", 13);
		treeMapICo.put("小雪", 14);
		treeMapICo.put("中雪", 15);
		treeMapICo.put("大雪", 16);
		treeMapICo.put("暴雪", 17);
		treeMapICo.put("雾", 18);
		treeMapICo.put("冻雨", 19);
		treeMapICo.put("沙尘暴", 20);
		treeMapICo.put("小雨转中雨", 7);
		treeMapICo.put("中雨转小雨", 8);
		treeMapICo.put("中雨转大雨", 9);
		treeMapICo.put("大雨转暴雨", 10);
		treeMapICo.put("浮尘", 20);
		treeMapICo.put("扬沙", 20);
		treeMapICo.put("强沙尘暴", 20);
		treeMapICo.put("霾", 20);
		treeMapICo.put("多云转阴", 1);
		treeMapICo.put("阴转小雨",7);
		treeMapICo.put("小雨转多云", 3);
		
		
		
		return treeMapICo;
		
		
	}
	
	
	public  TreeMap<String, Integer> getTodayGirl ( ){
		TreeMap<String, Integer> treeMapICo =new TreeMap<String, Integer>();
		treeMapICo.put("晴",0);
		treeMapICo.put("多云",0);
		treeMapICo.put("阴", 0);
		treeMapICo.put("阵雨", 1);
		treeMapICo.put("雷阵雨", 1);
		treeMapICo.put("雷阵雨伴有冰雹", 1);
		treeMapICo.put("雨夹雪", 1);
		treeMapICo.put("小雨", 1);
		treeMapICo.put("中雨", 1);
		treeMapICo.put("大雨", 1);
		treeMapICo.put("暴雨", 1);
		treeMapICo.put("大暴雨", 1);
		treeMapICo.put("特大暴雨", 1);
		treeMapICo.put("阵雪", 2);
		treeMapICo.put("小雪", 2);
		treeMapICo.put("中雪", 2);
		treeMapICo.put("大雪", 2);
		treeMapICo.put("暴雪", 2);
		treeMapICo.put("雾", 0);
		treeMapICo.put("冻雨", 2);
		treeMapICo.put("沙尘暴", 1);
		treeMapICo.put("小雨-中雨", 1);
		treeMapICo.put("中雨-大雨", 1);
		treeMapICo.put("大雨-暴雨", 1);
		treeMapICo.put("暴雨-大暴雨", 1);
		treeMapICo.put("浮尘", 1);
		treeMapICo.put("扬沙", 1);
		treeMapICo.put("强沙尘暴", 1);
		treeMapICo.put("霾", 1);
		treeMapICo.put("多云转阴", 1);
		treeMapICo.put("阴转小雨", 2);
		treeMapICo.put("小雨转多云", 2);
		treeMapICo.put("小雨转阴", 2);
		
		
		
		return treeMapICo;
		
		
	}

	
	
	
}

