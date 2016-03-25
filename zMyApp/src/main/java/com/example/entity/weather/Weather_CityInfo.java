package com.example.entity.weather;

public class Weather_CityInfo {
//    "weaid": "1",
//    "citynm": "����",
//    "cityno": "beijing",
//    "cityid": "101010100"
	private String weaid;
	private String citynm;
	private String cityno;
	private String cityid;
	public String getWeaid() {
		return weaid;
	}
	public void setWeaid(String weaid) {
		this.weaid = weaid;
	}
	public String getCitynm() {
		return citynm;
	}
	public void setCitynm(String citynm) {
		this.citynm = citynm;
	}
	public String getCityno() {
		return cityno;
	}
	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	@Override
	public String toString() {
		return "CityInfo [weaid=" + weaid + ", citynm=" + citynm + ", cityno=" + cityno + ", cityid=" + cityid + "]";
	}
	public Weather_CityInfo(String weaid, String citynm, String cityno, String cityid) {
		super();
		this.weaid = weaid;
		this.citynm = citynm;
		this.cityno = cityno;
		this.cityid = cityid;
	}
	public Weather_CityInfo() {
		super();
	}
	
	
}
