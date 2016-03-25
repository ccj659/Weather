package com.example.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apitestapp.MainActivity;
import com.example.apitestapp.R;
import com.example.useractivity.ThemeActivity;
import com.example.useractivity.UserInfoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;

public class MySlidingMenuFragment extends Fragment implements OnClickListener ,Comparator<String>{
	
	public interface  A {
		int i=1;
		
	}
	

	private static final int PICTURE_IS_OK = 0x0001;
	private static final int LOCAL_IS_OK = 0x0015;
	private View mView;
	private TextView tv_userName, tv_user_sginers;
	private ImageView iv_user_logo, iv_user_login, iv_user_sex;
	private RelativeLayout layout_appwiget_set, layout_sent_set,
			layout_notifcation_set, layout_back_animation_set,
			layout_share_app;
	private ImageView iv_send_key, iv_back_animat_key, iv_notification_key;
	private SharedPreferences preferences;
	private Editor editor;
	private String nickName;
	private int sex;
	boolean isRun = false;
	private String sginers;
	private Boolean sendIsCheck = true;
	private boolean animatIsCheck = true;
	private boolean notifyIsCheck = true;
	private boolean sensorIsCheck = true;
	private ImageView iv_sensor_key;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mView = inflater.inflate(R.layout.user_layout, container, false);
		return mView;
	}
	Comparator<String> s; 
	
	// tv_share_myapp
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		preferences = getActivity().getSharedPreferences("user",
				getActivity().MODE_PRIVATE);
		tv_userName = (TextView) view.findViewById(R.id.tv_user_name);
		iv_user_logo = (ImageView) view.findViewById(R.id.iv_user_logo);
		iv_user_login = (ImageView) view.findViewById(R.id.iv_user_login);
		layout_appwiget_set = (RelativeLayout) view
				.findViewById(R.id.layout_appwiget_set);
		
		layout_sent_set = (RelativeLayout) view
				.findViewById(R.id.layout_sent_set);
		layout_notifcation_set = (RelativeLayout) view
				.findViewById(R.id.layout_notifcation_set);
		layout_back_animation_set = (RelativeLayout) view
				.findViewById(R.id.layout_back_animation_set);
		
		iv_sensor_key = (ImageView) view.findViewById(R.id.iv_sensor_key);
		iv_user_sex = (ImageView) view.findViewById(R.id.iv_user_sex);
		tv_user_sginers = (TextView) view.findViewById(R.id.tv_user_sginers);
		iv_send_key = (ImageView) view.findViewById(R.id.iv_send_key);
		iv_back_animat_key = (ImageView) view
				.findViewById(R.id.iv_back_animat_key);
		iv_notification_key = (ImageView) view
				.findViewById(R.id.iv_notification_key);
		layout_appwiget_set = (RelativeLayout) view
				.findViewById(R.id.layout_appwiget_set);
		/*
		 * editor.putString("nickname", nickname); editor.putString("sginer",
		 * sginer); editor.putString("where", where);
		 */
		iv_user_sex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//iv_user_sex.setOnClickListener((View view)->{});
		editor = preferences.edit();

		checkButtonSet();// 查看button的设置

		nickName = preferences.getString("nickname", "请填写信息");
		sginers = preferences.getString("sginer", "天空不留痕迹");
		Log.i("preferences", sginers + "ss");
		tv_user_sginers.setText(sginers);
		sex = preferences.getInt("sex", 0);
		getLogo();
		tv_userName.setText(nickName);
		if (sex == 1) {
			iv_user_sex.setImageResource(R.drawable.female_checked);
		} else {
			iv_user_sex.setImageResource(R.drawable.male_checked);
		}
		iv_user_login.setOnClickListener(this);
		iv_user_logo.setOnClickListener(this);
		iv_send_key.setOnClickListener(this);
		iv_back_animat_key.setOnClickListener(this);
		iv_notification_key.setOnClickListener(this);
		iv_sensor_key.setOnClickListener(this);
		layout_appwiget_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ThemeActivity.class);
				// startActivityForResult(intent, 0x0011);
				startActivityForResult(intent, 0x0025);
			}
		});

	}
	//

	public void checkButtonSet() {//设置个人主页的几个
		sendIsCheck = preferences.getBoolean("sendIsCheck", true);
		animatIsCheck = preferences.getBoolean("animatIsCheck", true);
		notifyIsCheck = preferences.getBoolean("notifyIsCheck", true);
		sensorIsCheck = preferences.getBoolean("sensorIsCheck", true);
		if (sendIsCheck == false) {
			iv_send_key.setImageResource(R.drawable.toggle_btn_unchecked);
		} else {
			iv_send_key.setImageResource(R.drawable.toggle_btn_checked);
		}

		if (animatIsCheck == false) {
			iv_back_animat_key
					.setImageResource(R.drawable.toggle_btn_unchecked);
		} else {
			iv_back_animat_key.setImageResource(R.drawable.toggle_btn_checked);
		}

		if (notifyIsCheck == false) {
			iv_notification_key
					.setImageResource(R.drawable.toggle_btn_unchecked);
		} else {
			iv_notification_key.setImageResource(R.drawable.toggle_btn_checked);
		}
		if (sensorIsCheck == false) {
			iv_sensor_key.setImageResource(R.drawable.toggle_btn_unchecked);
		} else {
			iv_sensor_key.setImageResource(R.drawable.toggle_btn_checked);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_user_login:
			Intent intent = new Intent(getActivity(), UserInfoActivity.class);
			// setResult(0x0012, data)
			startActivityForResult(intent, LOCAL_IS_OK);
			break;
		case R.id.iv_user_logo:
			Intent intent1 = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent1.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent1.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
			startActivityForResult(intent1, PICTURE_IS_OK);
			break;
		case R.id.iv_send_key:
			sendIsCheck = !sendIsCheck;

			if (sendIsCheck == false) {
				iv_send_key.setImageResource(R.drawable.toggle_btn_unchecked);
			} else {
				iv_send_key.setImageResource(R.drawable.toggle_btn_checked);
			}
			editor.putBoolean("sendIsCheck", sendIsCheck);
			Toast.makeText(getActivity(), "设置成功,下次启动炫彩完成设置", Toast.LENGTH_SHORT)
					.show();
			editor.commit();
			break;
		case R.id.iv_back_animat_key:
			animatIsCheck = !animatIsCheck;
			if (animatIsCheck == false) {
				iv_back_animat_key
						.setImageResource(R.drawable.toggle_btn_unchecked);
			} else {
				iv_back_animat_key
						.setImageResource(R.drawable.toggle_btn_checked);
			}
			editor.putBoolean("animatIsCheck", animatIsCheck);
			Toast.makeText(getActivity(), "设置成功,下次启动炫彩完成设置", Toast.LENGTH_SHORT)
					.show();
			editor.commit();
			break;
		case R.id.iv_notification_key:
			notifyIsCheck = !notifyIsCheck;
			if (notifyIsCheck == false) {
				iv_notification_key
						.setImageResource(R.drawable.toggle_btn_unchecked);
			} else {
				iv_notification_key
						.setImageResource(R.drawable.toggle_btn_checked);
			}
			editor.putBoolean("notifyIsCheck", notifyIsCheck);
			Toast.makeText(getActivity(), "设置成功,下次启动炫彩完成设置", Toast.LENGTH_SHORT)
					.show();
			editor.commit();

			break;
		case R.id.iv_sensor_key:
			MainActivity activity = (MainActivity) getActivity();

			sensorIsCheck = !sensorIsCheck;
			if (sensorIsCheck == false) {
				activity.StopSenser();
				iv_sensor_key.setImageResource(R.drawable.toggle_btn_unchecked);
			} else {
				iv_sensor_key.setImageResource(R.drawable.toggle_btn_checked);
				activity.startSensor();
			}
			editor.putBoolean("sensorIsCheck", sensorIsCheck);
			editor.commit();

			break;
		default:
			break;
		}
	}

	public void getLogo() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getActivity(), "内存卡没有准备好", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "zuixuan");
		File file2 = new File(file, "logo.png");
		Log.i("file2", file2.toString());
		Bitmap bitmap = BitmapFactory.decodeFile(file2.toString());
		if (bitmap == null) {
			Toast.makeText(getActivity(), "请重新选择照片", Toast.LENGTH_SHORT).show();

		} else {
			iv_user_logo.setImageBitmap(bitmap);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOCAL_IS_OK) {
			preferences = getActivity().getSharedPreferences("user",
					getActivity().MODE_PRIVATE);
			nickName = preferences.getString("nickname", "请填写信息");
			sex = preferences.getInt("sex", 0);
			sginers = preferences.getString("sginer", "请填写信息");
			tv_userName.setText(nickName);
			if (sex == 1) {
				iv_user_sex.setImageResource(R.drawable.female_checked);
			} else {
				iv_user_sex.setImageResource(R.drawable.male_checked);
			}

		}

		if (0x0025 == requestCode) {
			FragmentActivity activity = (MainActivity) getActivity();
			((MainActivity) activity).setThemes();
		}

		if (requestCode == 0x0001) {
			Uri uri = null;
			try {
				uri = data.getData();
			} catch (Exception e) {
				return;
			}

			ContentResolver resolver = getActivity().getContentResolver();

			try {

				final Bitmap bitmap = BitmapFactory.decodeStream(resolver
						.openInputStream(uri));
				iv_user_logo.setImageBitmap(bitmap);
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getActivity(), "内存卡没有准备好",
							Toast.LENGTH_SHORT).show();
					return;
				}

				final File file = new File(
						Environment.getExternalStorageDirectory()
								+ File.separator + "zuixuan");
				if (!file.exists()) {
					file.mkdir();
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						while (!isRun) {
							// TODO Auto-generated method stub
							File file2 = new File(file, "logo.png");
							FileOutputStream stream = null;
							try {
								stream = new FileOutputStream(file2);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bitmap.compress(CompressFormat.PNG, 50, stream);
							try {
								stream.flush();
								stream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.i("Bitmap", "Bitmap");
							isRun = true;
						}
					}
				}).start();

				Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT)
						.show();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public int compare(String lhs, String rhs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
