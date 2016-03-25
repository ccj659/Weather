package com.example.news;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
import com.example.apitestapp.NewsDetail;
import com.example.apitestapp.R;
import com.example.entity.localnews.ContentList;
import com.example.entity.localnews.LocalNews;
import com.example.entity.localnews.SportNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SportNewsActivity extends Activity {
	//com.intent.example.news.SportSportNewsActivity
	ListView listView;
	ImageView imageView1_back ;
	private RequestQueue queue;
	protected ArrayList<ContentList> contentlist;
	BitmapUtils bitmapUtils;
	private BitmapCache bitmapCache;
	private ImageLoader imageLoader;
	private RequestQueue mQueue;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private PullToRefreshListView mptrListView;
	ViewPager local_viewpager;
	protected int page=1;
	protected LocalNews localNews;
	private String cityName="上海";
	ProgressBar progressBar_flush;
	private ViewPager viewPager;
	private int[] imgIdArray;
	private ImageView[] tips;
	private ImageView[] mImageViews;
	protected TreeMap<String, SportNews> maps;
	private ArrayList<SportNews> sportNews;
	private int pager=1;
	protected JSONObject jsonObject1;
	TextView textView1;
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.local_news_layout);
		
	        
		Intent intent =getIntent();
		cityName=intent.getStringExtra("cityName");
		imageView1_back=(ImageView) findViewById(R.id.imageView1_back);
		textView1=(TextView) findViewById(R.id.textView1);
		textView1.setText("体育头条");
		//hot_news_pager=(ViewPager) findViewById(R.id.hot_news_pager);
		progressBar_flush=(ProgressBar) findViewById(R.id.progressBar_flush);
		mptrListView = (PullToRefreshListView) findViewById(R.id.ptr_lv);
		mptrListView.setMode(Mode.BOTH);
		//local_viewpager=(ViewPager) findViewById(R.id.local_viewpager);
		bitmapUtils=new BitmapUtils(this);
		queue=	Volley.newRequestQueue(SportNewsActivity.this);
		mptrListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		mQueue = Volley.newRequestQueue(this);
		bitmapCache = new BitmapCache();
		imageLoader = new ImageLoader(mQueue, bitmapCache);
		//hot_news_pager.setAdapter(new HotNewsAdapter());
		//Log.i("arg0", str);
		volleyForNews();//加载
		//local_viewpager.setAdapter();
		imageView1_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mptrListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//VolleyRequestForResult();
				
				Intent intent =new Intent(SportNewsActivity.this,NewsDetail.class);
				intent.putExtra("link", sportNews.get(position-1).url);
				Log.i("arg0", position+"~~"+sportNews.get(position).url);
				startActivity(intent);
			}
			
		});
		
		mptrListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				//��ø�������ʱ��
				String time = DateUtils.formatDateTime(SportNewsActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE);
				mptrListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
				mptrListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
				mptrListView.getLoadingLayoutProxy().setReleaseLabel("刷新成功");
				mptrListView.getLoadingLayoutProxy().setLastUpdatedLabel(
						"刷新时间" + time);
				page=1;
				volleyForNews();
			
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				//�������������ı�
				mptrListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
				mptrListView.getLoadingLayoutProxy().setPullLabel("上拉加载");
				mptrListView.getLoadingLayoutProxy().setReleaseLabel("加载成功");
				pager++;
				//int allPage=Integer.parseInt(localNews.allPages);
				
				volleyForNews();
				
				//new UpdateData().execute();
			}
		});
		
	}



	public void volleyForNews() {
		SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
		String str =format.format(new Date(System.currentTimeMillis()));
		//String url="https://route.showapi.com/170-47?areaId=&areaName="+cityName+"&page="+page+"&showapi_appid=11768&showapi_timestamp="+str+"&title=&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";	
		String url="https://route.showapi.com/196-1?num=25&page="+pager+"&showapi_appid=11768&showapi_timestamp="+str+"&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";
		StringRequest stringRequest =new StringRequest(url, new Listener<String>() {
		
			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject =new JSONObject(arg0);
					jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
					JSONObject[] jsonObjects=new JSONObject[20];
					for (int i = 0; i < 20; i++) {
						jsonObjects[i]= jsonObject1.getJSONObject(""+i);
						Log.i("maps", jsonObjects[i].toString());
					}
					//jsonObject2.get("description");
//					public String description;
//					public String picUrl;
//					public String time;
//					public String title;
//					public String url;
					sportNews=new ArrayList<SportNews>();
					for (int i = 0; i < 20; i++) {
						SportNews news =new SportNews();
						news.description=jsonObjects[i].getString("description");
						Log.i("sportNews", news.description);
						news.picUrl=jsonObjects[i].getString("picUrl");
						news.time=jsonObjects[i].getString("time");
						news.title=jsonObjects[i].getString("title");
						news.url=jsonObjects[i].getString("url");
						sportNews.add(news);
					}
					
					MyAdapter adapter =new MyAdapter();
					mptrListView.setAdapter(adapter);
					mptrListView.onRefreshComplete();
					progressBar_flush.setVisibility(View.INVISIBLE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(SportNewsActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
				mptrListView.onRefreshComplete();
				
				}
			
		});
		
		
		queue.add(stringRequest);
	}

	
	
	
		
	



	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			
			return sportNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return sportNews.get(position);
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
			convertView=View.inflate(SportNewsActivity.this, R.layout.list_news_item, null);
			TextView tv_title =(TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_uptime =(TextView) convertView.findViewById(R.id.tv_uptime);
			TextView tv_where =(TextView) convertView.findViewById(R.id.tv_where);
			ImageView iv_pic =(ImageView) convertView.findViewById(R.id.imageView1_pic);
			SportNews list = sportNews.get(position);
			if(list==null){
				return null;
			}	
//			"description": "桑兰长微博：我没必要说谎 这一次不会再沉默...",
//			"picUrl": "http://d.ifengimg.com/w145_h103/y3.ifengimg.com/a/2015_45/61762f2dcdb120b.jpg",
//			"time": "2015-11-07 12:13",
//			"title": "桑兰长微博：我没必要说谎 这一次不会再沉默",
//			"url":
			
			tv_title.setText(list.title);
			tv_uptime.setText(list.time);
			//tv_where.setText(list.);
			
				if (list.picUrl!= null) {
					ImageListener iglistener = ImageLoader.getImageListener(iv_pic, R.drawable.share_photo_gallery,
							R.drawable.share_photo_gallery);
					imageLoader.get(list.picUrl,iglistener);
					 Log.i("url", list.picUrl);
				}else{
					iv_pic.setVisibility(View.GONE);
				}
				
			return convertView;
		}
		
	}
	
	class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		// ����ͼƬ�����
		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;

			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();

				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

	
	class HotNewsAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			 container =View.inflate(SportNewsActivity.this, R.layout.hot_news_item, null);
			//TextView tv_title = (TextView) container.findViewById(R.id.tv_hot_news_item);
			ImageView imageView =(ImageView) container.findViewById(R.id.iv_hot_news_item);
//			ImageListener iglistener = ImageLoader.getImageListener(imageView, R.drawable.share_photo_gallery,
//					R.drawable.share_photo_gallery);
//			imageLoader.get(hotNews.get(position).imageurls.get(0).url,iglistener);
			//tv_title.setText(hotNews.get(position).title);
			imageView.setImageResource(R.drawable.share_photo_gallery);
			//ImageView imageView =new ImageView(getActivity());

             
			return container;
		}
		
	}
	
	
	
	
	
}
