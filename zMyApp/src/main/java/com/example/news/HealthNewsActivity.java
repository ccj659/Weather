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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apitestapp.NewsActivity;
import com.example.apitestapp.NewsDetail;
import com.example.apitestapp.R;
import com.example.entity.localnews.HealthContentList;
import com.example.entity.localnews.HealthNews;
import com.example.entity.localnews.LocalNews;
import com.example.entity.localnews.PiontDetail;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HealthNewsActivity extends Activity {
	private ArrayList<HealthContentList> contentlist;
	private RequestQueue queue;
	private TextView textView1;
	private ProgressBar progressBar_flush;
	private ListView list_lookpoint;
	private ImageView imageView1_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_point_layout);
		queue=	Volley.newRequestQueue(HealthNewsActivity.this);
		textView1=(TextView) findViewById(R.id.textView1);
		textView1.setText("健康资讯");
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
		
		
		list_lookpoint.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent =new Intent(HealthNewsActivity.this,NewsDetail.class);
				intent.putExtra("link", contentlist.get(position).url);
				Log.i("arg0", position+"~~"+contentlist.get(position).url);
				startActivity(intent);
			}
		});
		
		volleyForNews();
		
		
		
		
		
	}
	
	
	
	
	public void volleyForNews() {
		SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
		String str =format.format(new Date(System.currentTimeMillis()));
		String url="https://route.showapi.com/90-87?key=&page=&showapi_appid=11312&showapi_timestamp="+str+"&tid=&showapi_sign=451063fbc3b74793a6c9ba534d54a068";	
		StringRequest stringRequest =new StringRequest(url, new Listener<String>() {
		
			

			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject =new JSONObject(arg0);
					JSONObject	jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
					JSONObject	jsonObject2 = jsonObject1.getJSONObject("pagebean");
					Gson gson =new Gson();
					HealthNews  healthNews = gson.fromJson(jsonObject2.toString(), HealthNews.class);
					contentlist  = healthNews.contentlist;
					MyAdapter adapter =new MyAdapter();
					list_lookpoint.setAdapter(adapter);
					progressBar_flush.setVisibility(View.INVISIBLE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(HealthNewsActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
				
				
				}
			
		});
		
		
		queue.add(stringRequest);
	}
	
	
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return contentlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return contentlist.get(position);
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
			convertView=View.inflate(HealthNewsActivity.this, R.layout.lookpoint_item, null);
			TextView tv_title =(TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_uptime =(TextView) convertView.findViewById(R.id.tv_uptime);
			TextView tv_where =(TextView) convertView.findViewById(R.id.tv_where);
			 HealthContentList list = contentlist.get(position);
			if(list==null){
				return null;
			}	
			tv_title.setText(list.title);
			tv_uptime.setText(list.media_name);
			tv_where.setText(list.keywords);
			
			
			
			return convertView;
		}
		
	}
	
	
	
	
	
}
