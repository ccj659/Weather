package com.example.apitestapp;

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
import com.example.entity.localnews.ContentList;
import com.example.entity.localnews.LocalNews;
import com.example.entity.localnews.NewsDetial;
import com.example.newspager.TabPageIndicator;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.BitmapUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsActivity extends FragmentActivity implements  OnPageChangeListener{
	
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
	private ViewPager hot_news_pager;
	private ViewPager viewPager;
	private int[] imgIdArray;
	private ImageView[] tips;
	private ImageView[] mImageViews;
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.local_news_layout);
	        
		Intent intent =getIntent();
		cityName=intent.getStringExtra("cityName");
		imageView1_back=(ImageView) findViewById(R.id.imageView1_back);
		//hot_news_pager=(ViewPager) findViewById(R.id.hot_news_pager);
		progressBar_flush=(ProgressBar) findViewById(R.id.progressBar_flush);
		mptrListView = (PullToRefreshListView) findViewById(R.id.ptr_lv);
		mptrListView.setMode(Mode.BOTH);
		//local_viewpager=(ViewPager) findViewById(R.id.local_viewpager);
		bitmapUtils=new BitmapUtils(this);
		queue=	Volley.newRequestQueue(NewsActivity.this);
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
				
				Intent intent =new Intent(NewsActivity.this,NewsDetail.class);
				intent.putExtra("link", contentlist.get(position-1).link);
				Log.i("arg0", position+"~~"+contentlist.get(position).link);
				startActivity(intent);
			}
			
		});
		
		mptrListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				//��ø�������ʱ��
				String time = DateUtils.formatDateTime(NewsActivity.this,
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
				page++;
				int allPage=Integer.parseInt(localNews.allPages);
				
				if (allPage==0) {
					Toast.makeText(NewsActivity.this, "没有内容~", Toast.LENGTH_SHORT).show();
					mptrListView.onRefreshComplete();
					return;
				}
				if(page>allPage){
					Toast.makeText(NewsActivity.this, "最后一页了少年", Toast.LENGTH_SHORT).show();
					mptrListView.onRefreshComplete();
					return;
				}
				volleyForNews();
				
				//new UpdateData().execute();
			}
		});
		
	}



	public void volleyForNews() {
		SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
		String str =format.format(new Date(System.currentTimeMillis()));
		String url="https://route.showapi.com/170-47?areaId=&areaName="+cityName+"&page="+page+"&showapi_appid=11768&showapi_timestamp="+str+"&title=&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";	
		StringRequest stringRequest =new StringRequest(url, new Listener<String>() {
		
			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject =new JSONObject(arg0);
					JSONObject	jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
					JSONObject	jsonObject2 = jsonObject1.getJSONObject("pagebean");
					Gson gson =new Gson();
					localNews = gson.fromJson(jsonObject2.toString(), LocalNews.class);
					Log.i("arg0", localNews.toString());
					contentlist  = localNews.contentlist;
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
				Toast.makeText(NewsActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
				mptrListView.onRefreshComplete();
				
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
			convertView=View.inflate(NewsActivity.this, R.layout.list_news_item, null);
			TextView tv_title =(TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_uptime =(TextView) convertView.findViewById(R.id.tv_uptime);
			TextView tv_where =(TextView) convertView.findViewById(R.id.tv_where);
			ImageView iv_pic =(ImageView) convertView.findViewById(R.id.imageView1_pic);
			ContentList list = contentlist.get(position);
			if(list==null){
				return null;
			}	
			tv_title.setText(list.title);
			tv_uptime.setText(list.pubDate);
			tv_where.setText(list.source);
			
				if (list.imageurls.size() > 0) {
					ImageListener iglistener = ImageLoader.getImageListener(iv_pic, R.drawable.share_photo_gallery,
							R.drawable.share_photo_gallery);
					imageLoader.get(list.imageurls.get(0).url,iglistener);
					 Log.i("url", list.imageurls.get(0).url);
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
			 container =View.inflate(NewsActivity.this, R.layout.hot_news_item, null);
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
	
	
	 public class MyAdapters extends PagerAdapter{  

		  

	        @Override  

	        public int getCount() {  

	            return mImageViews.length;  

	        }  

	  

	        @Override  

	        public boolean isViewFromObject(View arg0, Object arg1) {  

	            return arg0 == arg1;  

	        }  

	  

	        @Override  

	        public void destroyItem(View container, int position, Object object) {  

	            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);  

	        }  
	        /** 

	         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键 

	         */  
	        @Override  

	        public Object instantiateItem(View container, int position) {  

	            ((ViewPager)container).addView(mImageViews[position], 0);  
	            return mImageViews[position];  

	        }  


	    }  

	    @Override  

	    public void onPageSelected(int arg0) {  

	        setImageBackground(arg0);  

	    }  

	      

	    /** 

	     * 设置选中的tip的背景 

	     * @param selectItems 

	     */  

	    private void setImageBackground(int selectItems){  

	        for(int i=0; i<tips.length; i++){  

	            if(i == selectItems){  

	                tips[i].setBackgroundResource(R.drawable.ww1);  

	            }else{  

	                tips[i].setBackgroundResource(R.drawable.ww0);  

	            }  

	        }  

	    }

	   

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}  
	
	
	
	
	
}
	
	
	
	
	
	


