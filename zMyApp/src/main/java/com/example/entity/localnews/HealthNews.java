package com.example.entity.localnews;

import java.util.ArrayList;

public class HealthNews {
	public String allNum ;
	public String  allPages;
	public ArrayList<HealthContentList>  contentlist;
	public String  currentPage;
	public String  maxResult;
	@Override
	public String toString() {
		return "LocalNews [allNum=" + allNum + ", allPages=" + allPages
				+ ", contentlist=" + contentlist + "]";
	}
}
