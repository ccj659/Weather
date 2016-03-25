package com.example.entity.weather;

public class MyCityInfo {
	
	public String id;
	public String province;
	public String city;
	public String district;
	
	@Override
	public String toString() {
		return "MyCityInfo [id=" + id + ", province=" + province + ", city="
				+ city + ", district=" + district + "]";
	}

	
	
	
	
}




/*"id":"1", 城市ID
"province":"北京",省份名称
"city":"北京",	城市
"district":"北京"	城市/区名称*/