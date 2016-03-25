package com.example.useractivity;

import com.example.apitestapp.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class UserInfoActivity extends Activity {
	EditText tv_user_nickname,tv_user_sginer,tv_user_where;
	Button btn_user_info_confirm;
	ImageView btn_userinfo_return;
	RadioGroup radioGroup;
	SharedPreferences preferences;
	Editor editor;
	protected boolean isCheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_info_layout);
		preferences=getSharedPreferences("user", MODE_PRIVATE);
		editor=preferences.edit();
		btn_userinfo_return = (ImageView) findViewById(R.id.btn_userinfo_return);
		tv_user_nickname=(EditText) findViewById(R.id.tv_user_nickname);
		tv_user_sginer=(EditText) findViewById(R.id.tv_user_sginer);
		tv_user_where=(EditText) findViewById(R.id.tv_user_where);
		btn_user_info_confirm=(Button) findViewById(R.id.btn_user_info_confirm);
		radioGroup=(RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new  OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio0://男
					editor.putInt("sex", 0);
					isCheck=true;
					break;
				case R.id.radio1://女
					editor.putInt("sex", 1);
					isCheck=true;
					break;
				default:
					break;
				}
			}
		});
		
		
		btn_user_info_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nickname = tv_user_nickname.getText().toString().trim();
				String sginer = tv_user_sginer.getText().toString().trim();
				String where = tv_user_where.getText().toString().trim();
				if(isCheck==false||nickname==null||sginer==null||where==null){
					Toast.makeText(UserInfoActivity.this, "请完善您的信息", Toast.LENGTH_SHORT).show();
					return;
				}
				editor.putString("nickname", nickname);
				editor.putString("sginer", sginer);
				editor.putString("where", where);
				editor.commit();
				Toast.makeText(UserInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
		btn_userinfo_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		
		
	}
	
	
	
}
