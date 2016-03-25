package com.example.useractivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apitestapp.R;

import java.io.FileNotFoundException;

public class UserActivity extends Activity {
	TextView tv_userName;
	ImageView iv_user_logo,iv_user_login,iv_user_sex;
	RelativeLayout layout_appwiget_set,layout_sent_set,layout_notifcation_set
	,layout_back_animation_set,layout_share_app;
	SharedPreferences preferences;
	private String nickName;
	private int sex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_layout);
		preferences=getSharedPreferences("user", MODE_PRIVATE);
		tv_userName=(TextView) findViewById(R.id.tv_user_name);
		iv_user_logo=(ImageView) findViewById(R.id.iv_user_logo);
		iv_user_login=(ImageView) findViewById(R.id.iv_user_login);
		layout_appwiget_set=(RelativeLayout) findViewById(R.id.layout_appwiget_set);
		layout_sent_set=(RelativeLayout) findViewById(R.id.layout_sent_set);
		layout_notifcation_set=(RelativeLayout) findViewById(R.id.layout_notifcation_set);
		layout_back_animation_set=(RelativeLayout) findViewById(R.id.layout_back_animation_set);
		layout_share_app=(RelativeLayout) findViewById(R.id.iv_sensor_key);
		iv_user_sex=(ImageView) findViewById(R.id.iv_user_sex);

		 nickName = preferences.getString("nickname", "请填写信息");
		 sex = preferences.getInt("sex", 0);
		
		 tv_userName.setText(nickName);
		 if (sex==1) {
			 iv_user_sex.setImageResource(R.drawable.female_checked);
		}else{
			 iv_user_sex.setImageResource(R.drawable.male_checked);
		}
		
		
		
		
		
		
		
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_user_login:
			Intent intent =new Intent(UserActivity.this,UserInfoActivity.class);
			//setResult(0x0012, data)
			startActivityForResult(intent, 0x0015);
			
			
			break;
		case R.id.iv_user_logo:
			Intent intent1 = new Intent();    
            /* 开启Pictures画面Type设定为image */    
            intent1.setType("image/*");    
            /* 使用Intent.ACTION_GET_CONTENT这个Action */    
            intent1.setAction(Intent.ACTION_GET_CONTENT);     
            /* 取得相片后返回本画面 */    
            startActivityForResult(intent1, 0x0001);    
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0x0015){
			preferences=getSharedPreferences("user", MODE_PRIVATE);
			nickName = preferences.getString("nickname", "请填写信息");
			sex = preferences.getInt("sex", 0);
			
			 tv_userName.setText(nickName);
			 if (sex==1) {
				 iv_user_sex.setImageResource(R.drawable.female_checked);
			}else{
				 iv_user_sex.setImageResource(R.drawable.male_checked);
			}
		
			
		}
		
		if (requestCode==0x0001) {
			Uri uri =data.getData();
			ContentResolver resolver =this.getContentResolver();
			
			try {
				Bitmap bitmap =BitmapFactory.decodeStream(resolver.openInputStream(uri));
				iv_user_logo.setImageBitmap(bitmap);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
			
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

