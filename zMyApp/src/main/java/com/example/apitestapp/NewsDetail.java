package com.example.apitestapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
import com.example.apitestapp.NewsActivity.BitmapCache;
import com.example.entity.localnews.NewsDetial;
import com.example.entity.weather.WeatherJH;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetail extends Activity {
	private RequestQueue queue;
	TextView tv_title_detail,tv_where_detail,tv_details,tv_where_detail_title;
	ImageView iv_pic_detail,imageView1_back_detail,iv_share_news;
	ProgressBar progressBar ;
	protected ImageLoader imageLoader;
	private BitmapCache bitmapCache;
	private RequestQueue mQueue;
	protected boolean  isRun;
	protected NewsDetial localNews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);
		progressBar=(ProgressBar) findViewById(R.id.progressBar_flush_detail);
		tv_details=(TextView) findViewById(R.id.tv_details);
		tv_title_detail=(TextView) findViewById(R.id.tv_title_detail);
		tv_where_detail=(TextView) findViewById(R.id.tv_where_detail);
		iv_pic_detail=(ImageView) findViewById(R.id.iv_pic_detail);
		tv_where_detail_title=(TextView) findViewById(R.id.tv_where_detail_title);
		imageView1_back_detail=(ImageView) findViewById(R.id.imageView1_back_detail);
		iv_share_news=(ImageView) findViewById(R.id.iv_share_news);
		iv_pic_detail.setVisibility(View.INVISIBLE);
		mQueue = Volley.newRequestQueue(this);
		bitmapCache = new BitmapCache();
		imageLoader = new ImageLoader(mQueue, bitmapCache);
		Intent intent =getIntent();
		String  link =intent.getStringExtra("link");
		if (link==null) {
			Toast.makeText(NewsDetail.this, "链接不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.i("arg0", link);
		queue=	Volley.newRequestQueue(this);
		VolleyRequestForResult(link);
		
		
		
		imageView1_back_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		iv_share_news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(NewsDetail.this, "分享", Toast.LENGTH_SHORT).show();
				Intent intents=new Intent(Intent.ACTION_SEND); 
				intents.setType("text/plain"); //"image/*"
				if (localNews==null) {
					Toast.makeText(NewsDetail.this, "内容还未加载完成,请稍后", Toast.LENGTH_SHORT).show();

				}
				intents.putExtra(Intent.EXTRA_SUBJECT,"炫彩天气提醒您:"+localNews.title); 
				intents.putExtra(Intent.EXTRA_TEXT,"炫彩天气提醒您:"+localNews.title);
				intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(Intent.createChooser(intents, "选择分享类型"));
			}
		});
		
		
		
		
	}
	
	 public void  onClick(View v){
		 if(v.getId()==R.id.imageView1_back){
			 finish();
		 }
	 }
	
	public void VolleyRequestForResult(String link) {
		SimpleDateFormat format =new SimpleDateFormat("yyyyMMddHHmmss");
		String str =format.format(new Date(System.currentTimeMillis()));
	//	String url="https://route.showapi.com/170-47?areaId=&areaName="+cityName+"&page="+page+"&showapi_appid=11768&showapi_timestamp="+str+"&title=&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";	

		String URl="https://route.showapi.com/644-1?showapi_appid=11768&showapi_timestamp="+str+"&url="+link+"&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";	
		Log.i("arg0", URl);
		StringRequest stringRequest =new StringRequest(URl, new Listener<String>() {

			public void onResponse(String arg0) {
				try {
					JSONObject jsonObject =new JSONObject(arg0);
					JSONObject	jsonObject1 = jsonObject.getJSONObject("showapi_res_body");
					//JSONObject	jsonObject2 = jsonObject1.getJSONObject("pagebean");
					Gson gson =new Gson();
					localNews = gson.fromJson(jsonObject1.toString(), NewsDetial.class);
					Log.i("arg0","arg0"+ localNews.toString());
					tv_title_detail.setText(localNews.title);
					tv_where_detail.setText(localNews.laiyuan+"   "+ localNews.shijian );
					//iv_pic_detail.set;
					tv_where_detail_title.setText(localNews.laiyuan);
					//for
					final String pic_url=localNews.pic;
					final String pic=localNews.title;
					try {
						iv_pic_detail.setVisibility(View.VISIBLE);
						ImageListener iglistener = ImageLoader.getImageListener(iv_pic_detail, R.drawable.share_photo_gallery,
								R.drawable.share_photo_gallery);
						imageLoader.get(localNews.pic,iglistener);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						iv_pic_detail.setVisibility(View.GONE);
					}
					iv_pic_detail.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							AlertDialog.Builder builder =new AlertDialog.Builder(NewsDetail.this);
							builder.setTitle("是否保存图片");
							
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
								

									if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
							        {	
										isRun=true;
										saveBitmap(pic_url);
							        }
							        else
							        {
							            Toast.makeText(NewsDetail.this, "没有SD卡", Toast.LENGTH_LONG).show();
							        }
								}
							});
							builder.setNegativeButton("不要了", null);
							
							
							builder.show();
							
							
							return false;
						}

						
					});
					
					String neirong = localNews.neirong;
					///<p><img src/a-z 
							String s=neirong.replaceAll("/a-z", " ");
							String s1=neirong.replaceAll("<br>", "\n");
							String ss =s1.replaceAll("<b>", "\r\n");
							String sss =ss.replaceAll("</b>", "\r\n");
							
					tv_details.setText(sss);
					progressBar.setVisibility(View.INVISIBLE);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(NewsDetail.this, "请检查网络", Toast.LENGTH_SHORT).show();
				
				
				}
			
		});
		
	
		queue.add(stringRequest);
	
	
	
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
	public void saveBitmap(final String pic_url) {
		
		//final File file =new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"pic.png");
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(NewsDetail.this, "内存卡没有准备好", Toast.LENGTH_SHORT).show();
			return;
		}
		
		File file=new File(Environment.getExternalStorageDirectory()+File.separator+"zuixuan");
		if (!file.exists()) {
			file.mkdir();
		}
		int i =new Random().nextInt(10000)+10;
		final File file2 =new File(file,"logo"+i+".png");
		
		FileInputStream fileInputStream=null;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while ( isRun) {
					FileOutputStream outputStream =null;
					try {
						URL url =new URL(pic_url);
						InputStream openStream = url.openStream();
						outputStream=new FileOutputStream(file2);
						Bitmap bitmap =BitmapFactory.decodeStream(openStream);
						bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
						outputStream.flush();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						
						try {
							outputStream.close();
							isRun=false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
					}
					
				}
				
			}
		}).start();

	}

	
	
}
