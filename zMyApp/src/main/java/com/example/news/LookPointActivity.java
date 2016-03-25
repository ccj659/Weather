package com.example.news;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.apitestapp.NewsActivity;
import com.example.apitestapp.R;
import com.example.entity.localnews.ContentList;
import com.example.entity.localnews.LocalNews;
import com.example.entity.localnews.LookPoint;
import com.example.entity.localnews.PiontDetail;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LookPointActivity extends Activity {
	
	String url="https://route.showapi.com/477-1?province=四川&showapi_appid=11312&showapi_timestamp=20151108103104&star=AAAAA&showapi_sign=451063fbc3b74793a6c9ba534d54a068";
	private RequestQueue queue;
	private Intent intent;
	private String cityName;
	private ArrayList<PiontDetail> piontDetails;
	private LookPoint lookPoint;
	ProgressBar progressBar_flush;
	ListView list_lookpoint;
	ImageView imageView1_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_point_layout);
		intent=getIntent();
		cityName = intent.getStringExtra("cityName");
		queue=	Volley.newRequestQueue(LookPointActivity.this);
		progressBar_flush =(ProgressBar) findViewById(R.id.progressBar_flush);
		list_lookpoint=(ListView) findViewById(R.id.list_lookpoint);
		imageView1_back=(ImageView) findViewById(R.id.imageView1_back);
		imageView1_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		volleyForNews();
		
	}
	
	public void volleyForNews() {
		SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
		String str =format.format(new Date(System.currentTimeMillis()));
		String url="https://route.showapi.com/477-1?province="+cityName+"&showapi_appid=11312&showapi_timestamp="+str+"&star=AAAAA&showapi_sign=451063fbc3b74793a6c9ba534d54a068";
		StringRequest stringRequest =new StringRequest(url, new Listener<String>() {
		
			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject =new JSONObject(arg0);
					JSONObject	jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
					//JSONObject	jsonObject2 = jsonObject1.getJSONObject("pagebean");
					Gson gson =new Gson();
					 lookPoint =gson.fromJson(jsonObject1.toString(), LookPoint.class);
					Log.i("lookPoint", lookPoint.toString());
					piontDetails = lookPoint.list;
					MyAdapter myAdapter =new MyAdapter();
					list_lookpoint.setAdapter(myAdapter);
					if (piontDetails==null||piontDetails.size()==0) {
						Toast.makeText(LookPointActivity.this, "sorry 没有该城市的数据",Toast.LENGTH_SHORT ).show();
					}
					
					Log.i("lookPoint", piontDetails.size()+"size");
					progressBar_flush.setVisibility(View.INVISIBLE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(LookPointActivity.this, "sorry 没有该城市的数据",Toast.LENGTH_SHORT ).show();

				}
			
		});
		
		
		queue.add(stringRequest);
	}

	

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return piontDetails.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return piontDetails.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			Log.i("arg0", position+"position");
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=View.inflate(LookPointActivity.this, R.layout.lookpoint_item, null);
			TextView tv_title =(TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_uptime =(TextView) convertView.findViewById(R.id.tv_uptime);
			TextView tv_where =(TextView) convertView.findViewById(R.id.tv_where);
			PiontDetail list = piontDetails.get(position);
			if(list==null){
				return null;
			}	
			tv_title.setText(list.viewspot);
			tv_uptime.setText(list.star);
			tv_where.setText(list.price);
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(LookPointActivity.this, "!请到正规售票点购票,路线可以按照炫彩地图查询!", Toast.LENGTH_SHORT).show();
					
				}
			});
			
			
			return convertView;
		}
		
	}
	
		
	



	
	
}	
