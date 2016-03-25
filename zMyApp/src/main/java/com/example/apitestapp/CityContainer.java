package com.example.apitestapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.base.Constants;
import com.example.entity.weather.City;
import com.example.entity.weather.MyCityInfo;
import com.example.entity.weather.WeatherJH;
import com.example.utils.CityBIZ;
import com.example.utils.MatchWeatherIco;
import com.example.utils.URLEncoderUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class CityContainer extends Activity {
	protected static final int ADD_CITY_IS_OK=2;
	protected static final int ADD_SELECTED_CITY_IS_OK = 0;
	protected static final int VOLLEY_CITYINFO_IS_OK = 1;
	private  TreeMap<String, Integer> treeMapICo;
	private String URL_City;
	Button im_search_city;
	public int positions;//删除的城市的位置
	private MyCityInfo myCityInfo;
	GridView hotcity_gridView1;
	String [] hot={"北京","上海","重庆","四川","深圳","青岛","天津","哈尔滨","厦门","杭州","广州","昆明","拉萨","西安"};
	private ArrayList<String> hotCity=new ArrayList<String>();
	private ArrayList<MyCityInfo> cityList=new ArrayList<MyCityInfo>();
	public ArrayList<String> citys=new ArrayList<String>();
	AutoCompleteTextView city_searchView1;
	MatchWeatherIco weatherIco;
	ListView listView ;
	CityBIZ cityBiz;
	ArrayList<WeatherJH> selectCityInfos =new ArrayList<WeatherJH>();
	ArrayList<String>  cityNames=new ArrayList<String>();
	ArrayList<String>  allCityNames=new ArrayList<String>();
	Boolean isRun=false;
	WeatherJH weatherJH;
	int choiceIndex;
	//district
	String city;
	private MyAdapter adapter;
	private String URL_ByCity;
	private RequestQueue mQueue;
	public  String defalutCity=null;
	
	private Handler mHandler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ADD_SELECTED_CITY_IS_OK:
				selectCityInfos.add(weatherJH);
				//VolleyForCity();
				adapter.notifyDataSetChanged();
				progressBar1_addcity.setVisibility(View.GONE);
				Toast.makeText(CityContainer.this, "添加成功", Toast.LENGTH_SHORT).show();
			break;
			case ADD_CITY_IS_OK:
				isRun=true;
				//VolleyForCity();
				adapter.notifyDataSetChanged();
				progressBar1_addcity.setVisibility(View.GONE);
				Toast.makeText(CityContainer.this, "添加成功", Toast.LENGTH_SHORT).show();
			break;
			
			
			}
		}
	};
	
	private RequestQueue Queue;
	private TreeMap<String, Integer> smallIco;
	private SharedPreferences preferences;
	private Editor edit;
	LinearLayout main_city_layout;
	ProgressBar progressBar1_addcity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_container);
		city_searchView1=(AutoCompleteTextView) findViewById(R.id.city_searchView1);
		cityBiz=new CityBIZ(CityContainer.this);
		progressBar1_addcity=(ProgressBar) findViewById(R.id.progressBar1_addcity);
		main_city_layout=(LinearLayout) findViewById(R.id.main_city_layout);
		progressBar1_addcity.setVisibility(View.GONE);
		preferences=getSharedPreferences("city", CityContainer.MODE_PRIVATE);//保存默认城市
		defalutCity=preferences.getString("cityName", null);
		edit = preferences.edit();
		treeMapICo=MatchWeatherIco.getBigIco();
		listView=(ListView) findViewById(R.id.addcity_list);
		selectCityInfos=cityBiz.getSelectCityWeather();
		cityList=cityBiz.queryCityInfo();
		if(cityList==null||cityList.size()<1){ //判断 城市列表是否存在
			Toast.makeText(CityContainer.this, "正在加载城市列表请稍后",Toast.LENGTH_LONG ).show();
			//main_city_layout.setClickable(View.)
			progressBar1_addcity.setVisibility(View.VISIBLE);
			VolleyForCitys();
			Log.e("cityList", "不存在");
		}

		for (WeatherJH iterable : selectCityInfos) {//遍历所有已经添加的城市的名字
			cityNames.add(iterable.today.city);
		}
		for(int i=0;i<cityList.size();i++){
			allCityNames.add(cityList.get(i).district);//凌海
		}
		
		Log.e("cityList", cityList.size() + "");
		adapter=new MyAdapter();//给自己的城市列表添加适配器
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				positions=position;
				Builder dialog =new AlertDialog.Builder(CityContainer.this);
				dialog.setTitle("请选择");
				CharSequence[] provinces={"设为默认","删除该城市"};
				dialog.setSingleChoiceItems(provinces, -1, new DialogInterface.OnClickListener() 
	                { 
						@Override
	                    public void onClick(DialogInterface dialog, int which) 
	                    { 
							choiceIndex = which; 
	                    } 
	                }); 
				dialog.setTitle("确认");
			 	dialog.setPositiveButton("是", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if (choiceIndex==0) {
							defalutCity =cityNames.get(positions);
							edit.putString("cityName", defalutCity);
							edit.commit();
							adapter.notifyDataSetChanged();
							Toast.makeText(CityContainer.this, "设置成功", Toast.LENGTH_SHORT).show();
							
						}else if(choiceIndex==1){
							cityBiz.deleteCityInfo(cityNames.get(positions));
							//mHandler.sendEmptyMessage(_SELECTED_CITY_IS_OK);
							selectCityInfos.remove(positions);
							adapter.notifyDataSetChanged();
							Toast.makeText(CityContainer.this, "哥们,删除了", Toast.LENGTH_SHORT).show();
						}else{
							
						}

					}
				});
			 	
			 	dialog.setNegativeButton("否", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
			 	dialog.show();
				return false;
			}
			
		});
		hotcity_gridView1=(GridView) findViewById(R.id.hotcity_gridView1);
		URL_City="http://v.juhe.cn/weather/citys?key="+ Constants.WEATHEAR_KEY;
		URL_ByCity="http://v.juhe.cn/weather/index?format=2&cityname=+"+city+"+&key="+ Constants.WEATHEAR_KEY;
		adapterHotCitys();//适配 搜索条件,热门城市
	}
	public void adapterHotCitys() {
		ArrayAdapter<String> hotadapt = new ArrayAdapter<String>(CityContainer.this, android.R.layout.simple_dropdown_item_1line,   
				hot);   
		hotcity_gridView1.setAdapter(hotadapt);
		ArrayAdapter<String> searchAdapter=new ArrayAdapter<String>(CityContainer.this , android.R.layout.activity_list_item,android.R.id.text1, allCityNames);
		city_searchView1.setAdapter(searchAdapter);//设置搜索的城市
		hotcity_gridView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				city=hot[position];
				if(allCityNames.size()==0){
					cityList=cityBiz.queryCityInfo();
					for(int i=0;i<cityList.size();i++){
						allCityNames.add(cityList.get(i).district);//凌海

					}
				}
				
				if(!allCityNames.contains(city)){
					Toast.makeText(CityContainer.this, "兄弟,城市不存在!", Toast.LENGTH_SHORT).show();
					return;
				}
				if(cityNames.contains(city)){
					Toast.makeText(CityContainer.this, "该城市已经存在", Toast.LENGTH_SHORT).show();
					//cityBiz.updateCityInfo(city, weatherJH);
				}else{
					VolleyForCity();
					
				}
			}
			
		});
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.im_backtomain:
			Intent intent =new Intent();
			if(!(selectCityInfos.size()<1)){
				intent.putExtra("cityName", selectCityInfos.get(selectCityInfos.size()-1).today.city);
				setResult(0x002,intent);
			}
			
			
			int enterAnim=R.animator.into;
			int exitAnim=R.animator.into;
			overridePendingTransition(enterAnim, exitAnim);
			finish();
			
			break;
			
		case R.id.im_addcity:
			
			city=city_searchView1.getText().toString().trim();
			if(!allCityNames.contains(city)){
				Toast.makeText(CityContainer.this, "兄弟,城市不存在!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(cityNames.contains(city)){
				Toast.makeText(CityContainer.this, "该城市已经存在", Toast.LENGTH_SHORT).show();
				//cityBiz.updateCityInfo(city, weatherJH);
				
			}else{
				VolleyForCity();
				
			}
			
		
		break;
		default:
			break;
		}
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK==event.getAction()){
			if(!(selectCityInfos.size()<1)){
				Intent intent =new Intent();
				intent.putExtra("cityName", selectCityInfos.get(selectCityInfos.size()-1).today.city);
				setResult(0x002,intent);
			}
			finish();
			
			
		}
		
		return super.onKeyDown(keyCode, event);
	}

		
	public void VolleyForCitys() {//下载城市列表到数据库
		URL_City="http://v.juhe.cn/weather/citys?key="+Constants.WEATHEAR_KEY;
		Queue = Volley.newRequestQueue(CityContainer.this);
		Log.e("response", URL_City);
		StringRequest stringRequest = new StringRequest(URL_City,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("response", response);
						Gson gson = new Gson();
						City city=gson.fromJson(response, City.class);
						cityList=city.result;
						//Log.e("cityList", cityList.size()+"SS");
						if (null==cityList) {
							 Toast.makeText(CityContainer.this, "网络不存在", Toast.LENGTH_SHORT).show();
							 return;
							 
						}
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								while (!isRun) {
									for (MyCityInfo myCityinfo : cityList) {
										cityBiz.addCity(myCityinfo);
									}
									mHandler.sendEmptyMessage(ADD_CITY_IS_OK);
									
								}	
								
							}
						}).start();
						
						 
						
					}
				}, new Response.ErrorListener() {
					
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(CityContainer.this, "网络请求错误"+"", Toast.LENGTH_LONG).show();
						
					}

				});
		Queue.add(stringRequest);
	}
	
	public void VolleyForCity() {
		//新
		URL_ByCity="http://v.juhe.cn/weather/index?format=2&cityname=+"+ URLEncoderUtils.encode(city)+"+&key="+Constants.WEATHEAR_KEY;
		mQueue = Volley.newRequestQueue(CityContainer.this);
		StringRequest stringRequest = new StringRequest(URL_ByCity,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("response", response);
						 JSONObject object;
						try {
							object = new JSONObject(response);
							JSONObject result = object.getJSONObject("result");
							Gson gson = new Gson();
							
							 
							 weatherJH=gson.fromJson(result.toString(), WeatherJH.class);
							 Log.e("weatherJH", weatherJH.toString() + "w");
							 Log.e("weatherJH", weatherJH.future.get(1).temperature + "w");
							 for(int i=0;i<weatherJH.future.size();i++){
								 Log.e("cityBiz", weatherJH.future.get(i).toString() + "");
							 }
							cityBiz.addCityInfo(weatherJH);//添加数据库
							Log.e("weatherJH", "weatherJH");
							//selectCityInfos.add(weatherJH);
							Log.e("weatherJH1", "weatherJH1");
							cityNames.add(city);
							mHandler.sendEmptyMessage(ADD_SELECTED_CITY_IS_OK);
								
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
//							}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(CityContainer.this, "网络错误", Toast.LENGTH_LONG).show();
						
					}

				});
		mQueue.add(stringRequest);
	}
	
	
	
	
	class MyAdapter extends  BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return selectCityInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return selectCityInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=View.inflate(CityContainer.this, R.layout.addcity_item, null);
			TextView tv=(TextView) convertView.findViewById(R.id.tv_addcity_item);
			TextView tv_temp=(TextView) convertView.findViewById(R.id.tv_weath_citysss);
			ImageView view  =(ImageView) convertView.findViewById(R.id.iv_city_weather);
			TextView tv_city_default=(TextView) convertView.findViewById(R.id.tv_city_default);
			
			
		
			
			if(selectCityInfos.size()==0){
				
			}else{
				if(defalutCity!=null&&selectCityInfos.get(position).today.city.equals(defalutCity)){
					
					tv_city_default.setText("默认");
				}else {
					tv_city_default.setVisibility(View.INVISIBLE);
				}
				
				try {
					tv.setText(selectCityInfos.get(position).today.city);
					tv_temp.setText(selectCityInfos.get(position).today.temperature);
					Log.e("selectCityInfos", selectCityInfos.get(position).toString());
					String weather=selectCityInfos.get(position).today.weather;
					Log.e("tv_temp", weather);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				//view.setText(selectCityInfos.get(position).toString());
				
				try {
					view.setImageResource(treeMapICo.get(selectCityInfos.get(position).today.weather));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//view.setImageResource(R.drawable.ww1);
				}
				
			}
		
			
			
			
			return convertView;
		}
		
		
		
		
		
	}

	
	
	
	
	
}
