package com.example.apitestapp;

import cn.jpush.android.api.JPushInterface;

import com.example.fragment.FragmentCantainer.MyAdapters;
import com.example.service.WeatherUpdateService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingActivity extends FragmentActivity {
	
	
	public ImageView[] mImageViews;
	public ImageView[] tips;
	public int[] imgIdArray={R.drawable.loading1,R.drawable.loading2,R.drawable.loading3};
	private ViewGroup group;
	private TextView tv_hot_news_pic;
	public ViewPager viewPager;
	public MyAdapters myAdapters;
	public int selectItems;
	ImageButton btn_loading_start;
	private int height;
	private int width;
	SharedPreferences prefereUser;
	private Editor edit;
	private int isfirst;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefereUser=getSharedPreferences("user", LoadingActivity.MODE_PRIVATE);//保存默认城市
		isfirst = prefereUser.getInt("isfirst", 0);
		Log.i("isfirst", isfirst+"");

		if (isfirst!=0) {
			Log.i("isfirst", isfirst+";;0000");

			Intent intent1 =new Intent(LoadingActivity.this,MainActivity.class);
			startActivity(intent1);
			finish();
		}
		
		edit = prefereUser.edit();
		edit.putInt("isfirst", 1);
		edit.commit();
		
		setContentView(R.layout.loading_layout);
		group = (ViewGroup)findViewById(R.id.viewGroup1);  
		mImageViews=new ImageView[imgIdArray.length];
		viewPager = (ViewPager)findViewById(R.id.viewPager1);  
		btn_loading_start=(ImageButton) findViewById(R.id.btn_loading_start);
		btn_loading_start.setVisibility(View.INVISIBLE);
		
		height = getWindowManager().getDefaultDisplay().getHeight();
		width = getWindowManager().getDefaultDisplay().getWidth();
		
		
		loadingImage();
		//mImageViews[0]=BitmapFactory.decodeResource(getResources(), R.drawable.loading1);
		btn_loading_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(LoadingActivity.this,MainActivity.class);
				startActivity(intent);
				int enterAnim;
				int exitAnim=R.animator.alpha;
				finish();
			}
		});
	
	
	}
	
	public class MyAdapters extends PagerAdapter{  

        @Override  

        public int getCount() {  

            return imgIdArray.length;  

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

	
	
    
    public void loadingImage(){
    	tips = new ImageView[imgIdArray.length];  
		  for(int i=0; i<tips.length; i++){
	        	
		        
	            ImageView imageView = new ImageView(LoadingActivity.this);  
	            imageView.setLayoutParams(new LayoutParams(10,10));  
	            tips[i] = imageView;  
	            if(i == 0){  
	                tips[i].setBackgroundResource(R.drawable.ww1);  
	            }else{  
	                tips[i].setBackgroundResource(R.drawable.ww0);  

	            }  
	            
	            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,    

	                    LayoutParams.WRAP_CONTENT));  
	            layoutParams.leftMargin = 5;  
	            layoutParams.rightMargin = 5;  
	            group.addView(imageView, layoutParams);  
	        }     
	        
	        mImageViews = new ImageView[imgIdArray.length];  
	        for(int i=0; i<mImageViews.length; i++){  
	            ImageView imageView = new ImageView(LoadingActivity.this);  
	            mImageViews[i] = imageView;  
	            imageView.setBackgroundResource(imgIdArray[i]);  
	        }  
	        myAdapters = new MyAdapters();
	        viewPager.setAdapter(myAdapters);
	        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				private int positions;

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					setImageBackground(arg0);
					if (arg0==imgIdArray.length-1) {
						btn_loading_start.setVisibility(View.VISIBLE);
						TranslateAnimation animation =new TranslateAnimation(0, 50, -height, 50);
						animation.setDuration(1000);
						
						animation.setStartOffset(500);
						animation.setFillAfter(true);
						btn_loading_start.startAnimation(animation);
						
					}else{
						btn_loading_start.setVisibility(View.INVISIBLE);
					}
					
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					 
				}
			});  
	        


    }
    
    
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		JPushInterface.onPause(this);
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	
}
