package com.example.useractivity;

import java.util.ArrayList;

import com.example.apitestapp.R;
import com.example.apitestapp.R.drawable;
import com.example.apitestapp.R.id;
import com.example.apitestapp.R.layout;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ThemeActivity extends Activity {
	GridView gridView;
	ArrayList<Integer>  themes;
	private MyAdapter adapter;
	SharedPreferences preferences;
	Editor editor;
	Boolean isChecked=false;
	private int selectedIndex=0;
	protected int positions=-1;
	ImageView iv_theme_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.theme_layout);
		gridView=(GridView) findViewById(R.id.gv_theme);
		iv_theme_back=(ImageView) findViewById(R.id.iv_theme_backs);
		preferences=getSharedPreferences("user", ThemeActivity.MODE_PRIVATE);
		selectedIndex = preferences.getInt("theme", 0);
		editor=preferences.edit();
		themes=new ArrayList<Integer>();
		themes.add(R.drawable.bg0_fine_day);
		themes.add(R.drawable.bg_y1);
		themes.add(R.drawable.bg_nba2);
		themes.add(R.drawable.bg_city5);
		
		iv_theme_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (positions==-1) {
					positions=0;
				}
				Intent intent = new Intent();
				intent.putExtra("id", positions);
				setResult(0x0022,intent);
				finish();
			}
		});
		
		
		 adapter =new MyAdapter();
		gridView.setAdapter(adapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("onItemClick", position+"S");
				//adapter.notifyDataSetChanged();
				//parent.notifySubtreeAccessibilityStateChanged(child, source, changeType)
				final ImageView iv_theme_slelected = (ImageView) view.findViewById(R.id.iv_theme_slelected);
				
				if (iv_theme_slelected.getVisibility()==0) {
					isChecked=false;
					iv_theme_slelected.setVisibility(View.INVISIBLE);
					
					//保存
				}else if(isChecked==true){
					Toast.makeText(ThemeActivity.this,"选择之前,请取消之前的选择 ", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else {
					isChecked=true;
					iv_theme_slelected.setVisibility(View.VISIBLE);
					editor.putInt("theme", position);
					positions=position;
					editor.commit();
					Toast.makeText(ThemeActivity.this,"选择成功,您选择的是"+(position+1)+"号主题", Toast.LENGTH_SHORT).show();
					
					//保存
				}
			
			}
		});
		
		
		
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (positions==-1) {
				positions=0;
			}
			Intent intent = new Intent();
			intent.putExtra("id", positions);
			setResult(0x0022,intent);
			finish();
		}
		
		
		
		
		return super.onKeyDown(keyCode, event);
		
		
	}
	
	
	
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return themes.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return themes.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView=View.inflate(ThemeActivity.this, R.layout.theme_item_layout, null);
			ImageView iv_theme_pic =(ImageView) convertView.findViewById(R.id.iv_theme_pic);
			
			final ImageView iv_theme_slelected=(ImageView) convertView.findViewById(R.id.iv_theme_slelected);
			iv_theme_slelected.setVisibility(View.INVISIBLE);
			iv_theme_pic.setImageResource(themes.get(position));
			if (selectedIndex==position) {
				positions=position;
				iv_theme_slelected.setVisibility(View.VISIBLE);
				isChecked=true;
			}
			
			
			return convertView;
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
