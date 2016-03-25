package com.example.entity.weather;

public class Weather_PM25 {
	public String weaid;
	public String cityno;
	public String citynm;
	public String cityid;
	public String aqi;
	public String aqi_scope;
	public String aqi_levid;
	public String aqi_levnm;
	public String aqi_remark;
	@Override
	public String toString() {
		return "Weather_PM25 [weaid=" + weaid + ", cityno=" + cityno
				+ ", citynm=" + citynm + ", cityid=" + cityid + ", aqi=" + aqi
				+ ", aqi_scope=" + aqi_scope + ", aqi_levid=" + aqi_levid
				+ ", aqi_levnm=" + aqi_levnm + ", aqi_remark=" + aqi_remark
				+ "]";
	}
	

	
	
	
	
	
	
	
	
	
}
/*"success": "1",
"result": {
    "weaid": "100",
    "cityno": "zjtaizhou",
    "citynm": "台州",
    "cityid": "101210601",
    "aqi": "47",
    "aqi_scope": "0-50",
    "aqi_levid": "1",
    "aqi_levnm": "优",
    "aqi_remark": "参加户外活动呼吸清新空气"
}*/