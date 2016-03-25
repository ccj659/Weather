package com.example.entity.weather;

public class WeatherTodayJH {

	public String city;
	public String date_y;
	public String temperature;
	public String weather;
	public String wind;
	public String dressing_index;
	public String dressing_advice;
	public String uv_index;
	public String wash_index;
	public String travel_index;
	public String exercise_index;
	public String drying_index;
	@Override
	public String toString() {
		return "WeatherTodayJH [city=" + city + ", date_y=" + date_y
				+ ", temperature=" + temperature + ", weather=" + weather
				+ ", wind=" + wind + ", dressing_index=" + dressing_index
				+ ", dressing_advice=" + dressing_advice + ", uv_index="
				+ uv_index + ", wash_index=" + wash_index + ", travel_index="
				+ travel_index + ", exercise_index=" + exercise_index
				+ ", drying_index=" + drying_index + "]";
	}
	
	
}
/*"resultcode": "200",
"reason": "查询成功!",
"result": {
    "sk": {	当前实况天气
        "temp": "21",	当前温度
        "wind_direction": "西风",	当前风向
        "wind_strength": "2级",	当前风力	
        "humidity": "4%",	当前湿度
        "time": "14:25"	更新时间
    },
    "today": {
        "city": "天津",
        "date_y": "2014年03月21日",
        "week": "星期五",
        "temperature": "8℃~20℃",	今日温度
        "weather": "晴转霾",	今日天气
        "weather_id": {	天气唯一标识
            "fa": "00",	天气标识00：晴
            "fb": "53"	天气标识53：霾 如果fa不等于fb，说明是组合天气
        },
        "wind": "西南风微风",
        "dressing_index": "较冷", 穿衣指数
        "dressing_advice": "建议着大衣、呢外套加毛衣、卫衣等服装。",	穿衣建议
        "uv_index": "中等",	紫外线强度
        "comfort_index": "",
        "wash_index": "较适宜",	洗车指数
        "travel_index": "适宜",	旅游指数
        "exercise_index": "较适宜",	晨练指数
        "drying_index": ""
    },
    "future": {	未来几天天气
        "day_20140321": {
            "temperature": "8℃~20℃",
            "weather": "晴转霾",
            "weather_id": {
                "fa": "00",
                "fb": "53"
            },
            "wind": "西南风微风",
            "week": "星期五",
            "date": "20140321"
        },
        "day_20140322": {
            "temperature": "9℃~21℃",
            "weather": "霾转多云",
            "weather_id": {
                "fa": "53",
                "fb": "01"
            },
            "wind": "东北风微风转东南风微风",
            "week": "星期六",
            "date": "20140322"
        },*/