package com.example.view;

import java.util.ArrayList;

import com.example.apitestapp.R;

import android.app.Activity;
import android.os.Bundle;

public class ViewTest extends Activity{
	
	public ViewTest() {
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment3_layout);
		//loadChart();
	}
	
	
//    private void loadChart() {
//        ArrayList<MyChartItem> list = new ArrayList<MyChartItem>();
//        list.add(new MyChartItem("����", 23.2f));
//        list.add(new MyChartItem("����", 22.4f));
//        list.add(new MyChartItem("����", 24.7f));
//        list.add(new MyChartItem("4/18", 23.5f));
//        list.add(new MyChartItem("4/19", 25.5f));
//        list.add(new MyChartItem("4/20", 25.5f));
//        list.add(new MyChartItem("4/21", 25.5f));
//        list.add(new MyChartItem("4/22", 25.5f));
//        list.add(new MyChartItem("4/23", 25.5f));
//        
//        MyChartView tu = (MyChartView) findViewById(R.id.myChartView1);
//        //tu.SetTuView(list, "��λ: ���϶�");
//    }
	
	
	
}
