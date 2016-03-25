package com.example.apitestapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.entity.weather.WeatherJH;
import com.example.fragment.FragmentCantainer;
import com.example.fragment.MySlidingMenuFragment;
import com.example.gaode_map.LocationModeSourceActivity;
import com.example.service.WeatherUpdateService;
import com.example.useractivity.UserActivity;
import com.example.utils.CityBIZ;
import com.example.utils.MatchWeatherIco;
import com.example.view.ArcMenu;
import com.example.view.ArcMenu.OnMenuItemClickListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity {
    private static final int LOACTION_OK = 0;
    private static final int ON_NEW_INTENT = 1;
    private static final int UPDATE_EXISTS_CITY = 2;
    private static final int GET_WEATHER_RESULT = 3;
    protected static final int UPDATE_BACK_GROUND = 4;
    public static final int ADD_CITY_IS_OK = 0X009;
    public static final int ADD_USER_IS_OK = 0X010;
    //onClick url
    private double y, x;
    private boolean flag;
    public View windmill;
    private FrameLayout main_layout;
    private ImageView localSet, iv_fineday_bird, iv_fineday_bird1, iv_fine_day_cloud2, iv_fine_day_cloud1;
    private ArrayList<Fragment> fragments;
    private ViewPager main_fragment_pager;
    private TextView tv_weather;
    private CityBIZ cityBIZ;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocClient;
    private ArrayList<WeatherJH> selectCityWeather;
    private MyFragmentAdapter adapter;
    private FragmentManager fragmentManager;
    //main~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int width, height;
    protected boolean isFirstPager;
    protected boolean reLocaled;
    public int id = 0;
    protected TreeMap<String, Integer> treeMapICo;
    protected int pagerIndex;
    public SharedPreferences preferences;
    private ImageView[] tips;
    private LinearLayout group;
    private SharedPreferences prefereUser;
    private boolean animatIsCheck = true;
    private int themeIndex;
    private SensorManager sm;
    private boolean sensorIsCheck = true;
    private boolean isSharePressed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        getDisplay();
        findView();
        initRightMenu();
        Intent intent = new Intent(MainActivity.this, WeatherUpdateService.class);
        startService(intent);
        weatherBackAnimator();
        getPreferenceData();
        cityBIZ = new CityBIZ(MainActivity.this);
        selectCityWeather = cityBIZ.getSelectCityWeather();
        setTips();//设置选择的城市的原点
        initAnimator();//初始化动画
        getLocal();//定位
        if (sensorIsCheck) {
            registerSensor();//加速度传感器
        }
        adapterFragment();
        if (selectCityWeather.size() == 0) {
            Toast.makeText(MainActivity.this, "请点击定位按钮或直接添加城市", Toast.LENGTH_SHORT).show();
        } else {
            tv_weather.setText(selectCityWeather.get(0).today.city);
        }

        fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentAdapter(fragmentManager);
        main_fragment_pager.setAdapter(adapter);
        main_fragment_pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                pagerIndex = arg0;
                selectCityWeather = cityBIZ.getSelectCityWeather();
                //}
                setImageBackground(arg0);
                String cityName = selectCityWeather.get(arg0).today.city;
                tv_weather.setText(cityName);
                Log.i("today.city", cityName);
                if (arg0 == 0) {
                    isFirstPager = true;
                    localSet.setVisibility(View.VISIBLE);
                } else {
                    localSet.setVisibility(View.GONE);
                    isFirstPager = false;
                }
                String weather = selectCityWeather.get(arg0).today.weather;
                int x = 0;
                int test = 1;

                try {
                    test = treeMapICo.get(weather);

                } catch (Exception e) {

                }
                themeSelect(themeIndex, test);
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

    private void getPreferenceData() {
        preferences = getSharedPreferences("city", MainActivity.MODE_PRIVATE);
        prefereUser = getSharedPreferences("user", WeatherUpdateService.MODE_PRIVATE);//保存默认城市
        animatIsCheck = prefereUser.getBoolean("animatIsCheck", true);
        sensorIsCheck = prefereUser.getBoolean("sensorIsCheck", true);
        themeIndex = prefereUser.getInt("theme", 0);
    }

    private void getDisplay() {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    public void registerSensor() { //传感器
        //创建一个SensorManager来获取系统的传感器服务
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //选取加速度感应器
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);

    }

    final SensorEventListener myAccelerometerListener = new SensorEventListener() {

        //复写onSensorChanged方法
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                //图解中已经解释三个值的含义
                float X_lateral = sensorEvent.values[0];
                float Y_longitudinal = sensorEvent.values[1];
                float Z_vertical = sensorEvent.values[2];
                Log.i("sensor", "x:" + X_lateral + "y:" + Y_longitudinal);
                if (X_lateral < -4) {//往右翻 为负数

                    if (pagerIndex < 0) {
                        pagerIndex = selectCityWeather.size() - 1;
                    }
                    main_fragment_pager.setCurrentItem(pagerIndex--);
                }
                if (X_lateral > 4) {//左反为正数


                    if (pagerIndex > selectCityWeather.size() - 1) {
                        pagerIndex = 0;
                    }
                    main_fragment_pager.setCurrentItem(pagerIndex++);
                }
            }
        }

        //复写onAccuracyChanged方法
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    public void setThemes() {
        prefereUser = getSharedPreferences("user", WeatherUpdateService.MODE_PRIVATE);//保存默认城市
        themeIndex = prefereUser.getInt("theme", 0);
        String weather = selectCityWeather.get(0).today.weather;
        Log.i("default", weather);
        int x = 0;
        int test = 1;

        try {
            test = treeMapICo.get(weather);
            Log.i("default", test + "测试");

        } catch (Exception e) {

        }
        final int tests = test;
        Log.i("default", x + "~~~");

        themeSelect(themeIndex, test);

    }


    private void initRightMenu() {

        Fragment leftMenuFragment = new MySlidingMenuFragment();
        setBehindContentView(R.layout.left_menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_left_menu_frame, leftMenuFragment).commit();
        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		menu.setBehindWidth()
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        // menu.setBehindScrollScale(1.0f);
//		menu.setSecondaryShadowDrawable(R.drawable.shadow);
//		menu.setSecondaryMenu(R.layout.left_menu_frame);
//		Fragment rightMenuFragment = new MySlidingMenuFragment();
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.id_left_menu_frame, rightMenuFragment).commit();


    }

    public void setTips() {
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setVisibility(View.GONE);
        }

        tips = new ImageView[selectCityWeather.size()];
        for (int i = 0; i < selectCityWeather.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(3, 3));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.splash_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.bus_stop_line_order);

            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(10,

                    10));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);

        }


    }


    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.splash_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.bus_stop_line_order);
            }

        }

    }


    private void initDestopText(View childView) {

        //直接通过Activity获取的 WindowManager，在act退出时，桌面组件也将退出。
//       WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);   
        //一定要通过getApplicationContext()获取WindowManager,这种情况下，当Application终止后，悬浮控件才会被退出
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//       params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;   //如果设置为  
        params.type = WindowManager.LayoutParams.TYPE_PHONE; //
        //params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wm.addView(childView, params);
    }

    private void initAnimator() {
        if (selectCityWeather == null || selectCityWeather.size() == 0) {
            return;
        }
        String weather = selectCityWeather.get(0).today.weather;

        Log.i("default", weather);
        int x = 0;
        int test = 1;
        try {
            test = treeMapICo.get(weather);
        } catch (Exception e) {

        }
        themeSelect(themeIndex, test);

    }

    public void adapterFragment() {
        fragments = new ArrayList<Fragment>();
        if (selectCityWeather.size() == 0) {
            Bundle args = new Bundle();
            args.putString("cityName", "北京");
            args.putInt("isFirst", 0);
            Fragment fragment = new FragmentCantainer();
            //fragment.set
            fragment.setArguments(args);//根据数据库添加fragment
            fragments.add(fragment);
        }
        for (int i = 0; i < selectCityWeather.size(); i++) {
            Bundle args = new Bundle();
            args.putString("cityName", selectCityWeather.get(i).today.city);
            args.putInt("isFirst", i);
            Log.i("MaincityName", selectCityWeather.get(i).today.city);
            Fragment fragment = new FragmentCantainer();
            //fragment.set
            fragment.setArguments(args);//根据数据库添加fragment
            fragments.add(fragment);
        }
    }

    public void setlocalName(String cityName) {
        reLocaled = true;
        updateCityInfoDB();
        adapter.notifyDataSetChanged();
        tv_weather.setText(cityName);

    }

    public boolean isFirstPage() {

        return isFirstPager;
    }


    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return fragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.size();
        }


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_city_btn_pressed:
                Intent intent = new Intent("android.intent.action.CityContainer");
                startActivityForResult(intent, ADD_CITY_IS_OK);
                int enterAnim = R.animator.into;
                int exitAnim = R.animator.into;
                overridePendingTransition(enterAnim, exitAnim);
                break;
            case R.id.im_ic_actionbar_share_normal:

                Toast.makeText(MainActivity.this, "share_normal", Toast.LENGTH_LONG).show();
                isSharePressed=true;
                Intent intents = new Intent(Intent.ACTION_SEND);
                intents.setType("text/plain"); //"image"
                intents.putExtra(Intent.EXTRA_SUBJECT, "今天天气");
                ArrayList<WeatherJH> cityWeathers = cityBIZ.getSelectCityWeather();
                WeatherJH weatherJH = cityWeathers.get(pagerIndex);
                if (weatherJH == null) {
                    Toast.makeText(MainActivity.this, "请先添加城市", Toast.LENGTH_LONG).show();
                    return;
                }
                intents.putExtra(Intent.EXTRA_TEXT, weatherJH.today.city + "今天天气:" + weatherJH.today.weather + "," +
                                weatherJH.today.temperature + ",风向:" + weatherJH.sk.wind_direction + ",风力" +
                                weatherJH.sk.wind_strength + "," + weatherJH.today.dressing_advice
                );

                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intents, "选择分享类型"));

            case R.id.im_lacal:
                if (isSharePressed){
                    isSharePressed=false;
                    return;
                }

                Toast.makeText(MainActivity.this, "开启定位", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent("reciver");
                Log.i("lon", x + ":" + y);
                intent1.putExtra("lon", x + "");
                intent1.putExtra("lat", y + "");
                sendBroadcast(intent1);
                break;
            default:
                break;
        }

    }


    public void findView() {
        main_layout = (FrameLayout) findViewById(R.id.main_layout_main);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        localSet = (ImageView) findViewById(R.id.im_lacal);
        group = (LinearLayout) findViewById(R.id.lin_group);
        main_fragment_pager = (ViewPager) findViewById(R.id.main_fragment_pager);
        //tv_sliper=(TextView) findViewById(R.id.tv_sliper);
        windmill = findViewById(R.id.im_sunshine);
        iv_fineday_bird = (ImageView) findViewById(R.id.iv_fineday_bird);
        iv_fineday_bird1 = (ImageView) findViewById(R.id.iv_fineday_bird1);
        iv_fine_day_cloud2 = (ImageView) findViewById(R.id.im_fine_day_cloud2);
        iv_fine_day_cloud1 = (ImageView) findViewById(R.id.iv_fine_day_cloud1);
    }


    public void getLocal() {
        flag = false;
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CITY_IS_OK) {
            cityBIZ = new CityBIZ(getApplicationContext());
            updateCityInfoDB();
            setTips();
            fragments = new ArrayList<Fragment>();
            adapterFragment();
            adapter.notifyDataSetChanged();
            String cityName = null;
            try {
                cityName = data.getStringExtra("cityName");
                if (cityName == null) {
                    return;
                }
                int index = -1;
                if ((index = checkCityName(cityName)) != -1) {
                    //��ת��ViewPagerָ����ҳ��
                    main_fragment_pager.setCurrentItem(index);
                    return;
                }
            } catch (Exception e) {
            }
        }

    }

    public void updateCityInfoDB() {
        selectCityWeather = cityBIZ.getSelectCityWeather();

        for (WeatherJH iterable : selectCityWeather) {
            String city = iterable.today.city;
            Log.i("city", city);
        }
    }

    private int checkCityName(String cityName) {
        for (int i = 0; i < selectCityWeather.size(); i++) {
            if (selectCityWeather.get(i).today.city.equals(cityName)) {
                return i;
            }
        }
        return -1;
    }

    public class MyLocationListenner implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }

            if (flag == false) {
                y = location.getLatitude();
                x = location.getLongitude();
                // Log.i("lon", x)

                //Log.i("addresss", location.getAddrStr());
                Log.i("location", location.getLatitude() + "~~" + location.getLongitude());
                flag = true;

                mLocClient.stop();
            }
        }

    }

    public void stopClient() {
        mLocClient.stop();

    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mLocClient.stop();
        Log.i("destory", "destory");
        super.onDestroy();
    }



    public void themeSelect(int themeIndex, int id) {
        if (animatIsCheck == false) {
            return;
        }
        //im_city_btn_pressed,tv_weather,im_lacal,tv_sliper,im_ic_actionbar_share_normal
        //main_fragment_pager framelayout linearl_main
        main_layout = (FrameLayout) findViewById(R.id.main_layout_main);
        int size = main_layout.getChildCount();
        for (int i = 0; i < size; i++) {
            main_layout.getChildAt(i).clearAnimation();
            int id2 = main_layout.getChildAt(i).getId();
            if (id2 == R.id.relativeLayout1 || id2 == R.id.tv_weather) {
                continue;
            }
            if (id2 == R.id.im_lacal || id2 == R.id.lin_group) {
                continue;
            }
            if (id2 == R.id.im_ic_actionbar_share_normal || id2 == R.id.main_fragment_pager) {
                continue;
            }

            if (id2 == R.id.framelayout || id2 == R.id.linearl_main) {
                continue;
            }
            main_layout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        switch (themeIndex) {
            case 0:
                backWeather(id);//系统
                break;
            case 1:
                backWeather1(id);//宇宙
                break;
            case 2:
                backWeather2(id);//nba
                break;
            case 3:
                backWeather3(id);//城市
                break;
            case 4:
                backWeather4(id);//lol
                break;
            case 5:
                backWeather5(id);//cat
                break;
            default:
                break;
        }


    }
    public void cloudyDayAnimation() {
        main_layout.setBackgroundResource(R.drawable.bg_cloudy_day);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.cloudy_bottom);
        imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        main_layout.addView(imageView);
        TranslateAnimation aa2 = new TranslateAnimation(0, 0, height / 4, -height / 8);
        aa2.setRepeatCount(-1);//--重复次数
        aa2.setRepeatMode(Animation.REVERSE);
        aa2.setDuration(80 * 1000);
        imageView.startAnimation(aa2);
    }

    public void darkDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_overcast);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);

    }

    public void fogDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_fog_and_haze);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);

    }

    public void rainDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_heavy_rain_night);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
        Random random = new Random();
        int[] imageViewRes = {R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m};
        for (int i = 0; i < imageViewRes.length; i++) {
            ImageView imageViews = new ImageView(this);
            imageViews.setImageResource(imageViewRes[i]);
            imageViews.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            main_layout.addView(imageViews);
            imageViews.setVisibility(View.INVISIBLE);
            int drop = random.nextInt(1000);
            TranslateAnimation animation = new TranslateAnimation(drop, drop, 0, height);

            animation.setDuration(random.nextInt(1000) + 1000);
            animation.setRepeatCount(-1);
            imageViews.startAnimation(animation);
            imageViews.setVisibility(View.VISIBLE);

        }


    }

    public void heavyRainDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_heavy_rain_night);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
//		 ImageView 	imageView=new ImageView(this);//云
//		 imageView.setImageResource(R.drawable.moderate_rain_gray_cloud);
//		 imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		 main_layout.addView(imageView);
//		 TranslateAnimation animat =new TranslateAnimation(0, width/4, 0, 0);
//		 animat.setDuration(7000);
//		 animat.setRepeatCount(-1);
//		 animat.setRepeatMode(TranslateAnimation.REVERSE);
//		 imageView.startAnimation(animat);
        Random random = new Random();
        int[] imageViewRes = {R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m,
                R.drawable.raindrop_xl, R.drawable.raindrop_l, R.drawable.raindrop_s, R.drawable.raindrop_m};
        for (int i = 0; i < imageViewRes.length; i++) {
            for (int j = 0; j < imageViewRes.length; j++) {
                ImageView imageViews = new ImageView(this);
                imageViews.setImageResource(imageViewRes[i]);
                imageViews.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                main_layout.addView(imageViews);
                imageViews.setVisibility(View.INVISIBLE);
                int drop = random.nextInt(1000);
                TranslateAnimation animation = new TranslateAnimation(drop, drop, 0, height);

                animation.setDuration(random.nextInt(500) + 1000);
                animation.setRepeatCount(-1);
                imageViews.startAnimation(animation);
                imageViews.setVisibility(View.VISIBLE);
            }

        }


    }

    public void smallSnowDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_snow);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
        Random random = new Random();
        int[] imageViewRes = {R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl};
        for (int i = 0; i < imageViewRes.length; i++) {
            ImageView imageViews = new ImageView(this);
            imageViews.setImageResource(imageViewRes[i]);
            imageViews.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            main_layout.addView(imageViews);
            imageViews.setVisibility(View.INVISIBLE);
            int drop = random.nextInt(1000);
            TranslateAnimation animation = new TranslateAnimation(drop, drop, 0, height);

            animation.setDuration(random.nextInt(5000) + 1000);
            animation.setRepeatCount(-1);
            imageViews.startAnimation(animation);
            imageViews.setVisibility(View.VISIBLE);

        }


    }

    public void heavySnowDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_snow_night);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
        Random random = new Random();
        int[] imageViewRes = {R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl};
        for (int i = 0; i < imageViewRes.length; i++) {
            for (int j = 0; j < imageViewRes.length; j++) {
                ImageView imageViews = new ImageView(this);
                imageViews.setImageResource(imageViewRes[j]);
                imageViews.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                main_layout.addView(imageViews);
                imageViews.setVisibility(View.INVISIBLE);
                int drop = random.nextInt(1000);
                TranslateAnimation animation = new TranslateAnimation(drop, drop, 0, height);

                animation.setDuration(random.nextInt(3000) + 1000);
                animation.setRepeatCount(-1);
                imageViews.startAnimation(animation);
                imageViews.setVisibility(View.VISIBLE);
            }
        }


    }


    public void cloudDayAnimat() {
        main_layout.setBackgroundResource(R.drawable.bg_snow_night);
        iv_fineday_bird.setVisibility(View.INVISIBLE);
        iv_fineday_bird1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud1.setVisibility(View.INVISIBLE);
        iv_fine_day_cloud2.setVisibility(View.INVISIBLE);
        windmill.setVisibility(View.INVISIBLE);
        Random random = new Random();
        int[] imageViewRes = {R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl,
                R.drawable.snowflake_xxl, R.drawable.snowflake_l, R.drawable.snowflake_m, R.drawable.snowflake_xl};
        for (int i = 0; i < imageViewRes.length; i++) {
            for (int j = 0; j < imageViewRes.length; j++) {
                ImageView imageViews = new ImageView(this);
                imageViews.setImageResource(imageViewRes[j]);
                imageViews.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                main_layout.addView(imageViews);
                imageViews.setVisibility(View.INVISIBLE);
                int drop = random.nextInt(1000);
                TranslateAnimation animation = new TranslateAnimation(drop, drop, 0, height);

                animation.setDuration(random.nextInt(3000) + 1000);
                animation.setRepeatCount(-1);
                imageViews.startAnimation(animation);
                imageViews.setVisibility(View.VISIBLE);
            }
        }


    }
    public void fineDayAnimation() {
        main_layout.setBackgroundResource(R.drawable.bg0_fine_day);
        AnimationDrawable anim = (AnimationDrawable) iv_fineday_bird.getBackground();
        anim.start();
        AnimationDrawable anim1 = (AnimationDrawable) iv_fineday_bird1.getBackground();
        anim1.start();
        TranslateAnimation aa = new TranslateAnimation(0, width, 0, 0);
        aa.setRepeatCount(-1);//--重复次数
        aa.setRepeatMode(Animation.RESTART);
        aa.setDuration(10 * 1000);
        iv_fineday_bird.setAnimation(aa);
        iv_fineday_bird1.setAnimation(aa);
        aa.start();
        TranslateAnimation aa2 = new TranslateAnimation(0, width, 0, 0);
        aa2.setRepeatCount(-1);//--重复次数
        aa2.setRepeatMode(Animation.RESTART);
        aa2.setDuration(45 * 1000);
        iv_fine_day_cloud1.setAnimation(aa2);
        iv_fine_day_cloud2.setAnimation(aa2);
        aa2.start();
        iv_fineday_bird.setVisibility(View.VISIBLE);
        iv_fineday_bird1.setVisibility(View.VISIBLE);
        iv_fine_day_cloud1.setVisibility(View.VISIBLE);
        iv_fine_day_cloud2.setVisibility(View.VISIBLE);
        windmill.setVisibility(View.VISIBLE);
        Animation operatingAnim = AnimationUtils.loadAnimation(this,
                R.animator.rotate);

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            windmill.startAnimation(operatingAnim);
        }
    }

    public void backWeather(int id) {
        Log.i("weather", id + "");


        switch (id) {

            case 0://晴天

                fineDayAnimation();
                break;
            case 1://多云
                cloudyDayAnimation();
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                rainDayAnimat();
                break;
            case 3://小雨
                rainDayAnimat();
                break;
            case 4://大雨,暴雨
                heavyRainDayAnimat();
                break;
            case 5://阵雪
                smallSnowDayAnimat();
                break;
            case 6://雾,霾,扬尘
                fogDayAnimat();
                break;

            case 7://大雪
                heavySnowDayAnimat();
                break;
            case 8://阴
                darkDayAnimat();
                break;

            default:
                break;
        }

    }

    public void backWeather1(int id) {
        switch (id) {
            case 0://晴天
                main_layout.setBackgroundResource(R.drawable.bg_y1);
                break;
            case 1://多云
                main_layout.setBackgroundResource(R.drawable.bg_y2);
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                main_layout.setBackgroundResource(R.drawable.bg_y3);
                break;
            case 3://小雨
                main_layout.setBackgroundResource(R.drawable.bg_y4);
                break;
            case 4://大雨,暴雨
                main_layout.setBackgroundResource(R.drawable.bg_y4);
                break;
            case 5://阵雪
                main_layout.setBackgroundResource(R.drawable.bg_y5);
                break;
            case 6://雾,霾,扬尘
                main_layout.setBackgroundResource(R.drawable.bg_y6);
                break;

            case 7://大雪
                main_layout.setBackgroundResource(R.drawable.bg_y7);
                break;
            case 8://阴
                main_layout.setBackgroundResource(R.drawable.bg_y2);
                break;

            default:
                break;
        }

    }


    public void backWeather2(int id) {//nba壁纸
        switch (id) {
            case 0://晴天
                main_layout.setBackgroundResource(R.drawable.bg_nba1);
                break;
            case 1://多云
                main_layout.setBackgroundResource(R.drawable.bg_nba2);
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                main_layout.setBackgroundResource(R.drawable.bg_nba3);
                break;
            case 3://小雨
                main_layout.setBackgroundResource(R.drawable.bg_nba4);
                break;
            case 4://大雨,暴雨
                main_layout.setBackgroundResource(R.drawable.bg_nba5);
                break;
            case 5://阵雪
                main_layout.setBackgroundResource(R.drawable.bg_nba6);
                break;
            case 6://雾,霾,扬尘
                main_layout.setBackgroundResource(R.drawable.bg_nba7);
                break;

            case 7://大雪
                main_layout.setBackgroundResource(R.drawable.bg_nba7);
                break;
            case 8://阴
                main_layout.setBackgroundResource(R.drawable.bg_nba2);
                break;

            default:
                break;
        }

    }

    public void backWeather3(int id) {//city
        switch (id) {
            case 0://晴天
                main_layout.setBackgroundResource(R.drawable.bg_city1);
                break;
            case 1://多云
                main_layout.setBackgroundResource(R.drawable.bg_city2);
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                main_layout.setBackgroundResource(R.drawable.bg_city3);
                break;
            case 3://小雨
                main_layout.setBackgroundResource(R.drawable.bg_city4);
                break;
            case 4://大雨,暴雨
                main_layout.setBackgroundResource(R.drawable.bg_city5);
                break;
            case 5://阵雪
                main_layout.setBackgroundResource(R.drawable.bg_city6);
                break;
            case 6://雾,霾,扬尘
                main_layout.setBackgroundResource(R.drawable.bg_city5);
                break;

            case 7://大雪
                main_layout.setBackgroundResource(R.drawable.bg_city7);
                break;
            case 8://阴
                main_layout.setBackgroundResource(R.drawable.bg_city8);
                break;

            default:
                break;
        }

    }

    public void backWeather4(int id) {//lol
        switch (id) {
            case 0://晴天
                main_layout.setBackgroundResource(R.drawable.bg_lol1);
                break;
            case 1://多云
                main_layout.setBackgroundResource(R.drawable.bg_lol2);
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                main_layout.setBackgroundResource(R.drawable.bg_lol3);
                break;
            case 3://小雨
                main_layout.setBackgroundResource(R.drawable.bg_lol4);
                break;
            case 4://大雨,暴雨
                main_layout.setBackgroundResource(R.drawable.bg_lol5);
                break;
            case 5://阵雪
                main_layout.setBackgroundResource(R.drawable.bg_lol6);
                break;
            case 6://雾,霾,扬尘
                main_layout.setBackgroundResource(R.drawable.bg_lol7);
                break;

            case 7://大雪
                main_layout.setBackgroundResource(R.drawable.bg_lol8);
                break;
            case 8://阴
                main_layout.setBackgroundResource(R.drawable.bg_lol9);
                break;

            default:
                break;
        }

    }

    public void backWeather5(int id) {//cat
        switch (id) {
            case 0://晴天
                main_layout.setBackgroundResource(R.drawable.bg_cat1);
                break;
            case 1://多云
                main_layout.setBackgroundResource(R.drawable.bg_cat2);
                break;
            case 2://阵雨,雷阵雨,雨夹雪
                main_layout.setBackgroundResource(R.drawable.bg_cat3);
                break;
            case 3://小雨
                main_layout.setBackgroundResource(R.drawable.bg_cat4);
                break;
            case 4://大雨,暴雨
                main_layout.setBackgroundResource(R.drawable.bg_cat5);
                break;
            case 5://阵雪
                main_layout.setBackgroundResource(R.drawable.bg_cat6);
                break;
            case 6://雾,霾,扬尘
                main_layout.setBackgroundResource(R.drawable.bg_cat7);
                break;

            case 7://大雪
                main_layout.setBackgroundResource(R.drawable.bg_cat6);
                break;
            case 8://阴
                main_layout.setBackgroundResource(R.drawable.bg_cat2);
                break;

            default:
                break;
        }

    }


    public void weatherBackAnimator() {
        treeMapICo = new TreeMap<String, Integer>();
        treeMapICo.put("晴", 0);
        treeMapICo.put("多云", 1);
        treeMapICo.put("阴", 8);
        treeMapICo.put("阵雨", 2);
        treeMapICo.put("雷阵雨", 2);
        treeMapICo.put("雷阵雨伴有冰雹", 2);
        treeMapICo.put("雨夹雪", 5);
        treeMapICo.put("小雨", 3);
        treeMapICo.put("中雨", 4);
        treeMapICo.put("大雨", 4);
        treeMapICo.put("暴雨", 4);
        treeMapICo.put("大暴雨", 4);
        treeMapICo.put("特大暴雨", 4);
        treeMapICo.put("阵雪", 5);
        treeMapICo.put("小雪", 5);
        treeMapICo.put("中雪", 7);
        treeMapICo.put("大雪", 7);
        treeMapICo.put("暴雪", 7);
        treeMapICo.put("雾", 6);
        treeMapICo.put("冻雨", 3);
        treeMapICo.put("沙尘暴", 6);
        treeMapICo.put("小雨转中雨", 4);
        treeMapICo.put("中雨转小雨", 4);
        treeMapICo.put("中雨转大雨", 4);
        treeMapICo.put("大雨转暴雨", 4);
        treeMapICo.put("浮尘", 6);
        treeMapICo.put("扬沙", 6);
        treeMapICo.put("强沙尘暴", 6);
        treeMapICo.put("霾", 6);
        treeMapICo.put("多云转阴", 8);
        treeMapICo.put("晴转多云", 1);
        treeMapICo.put("阴转小雨", 3);
        treeMapICo.put("小雨转多云", 3);
        treeMapICo.put("小雨转雨夹雪", 2);
        treeMapICo.put("晴转霾", 8);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        JPushInterface.onResume(this);
        if (sm != null) {
            int sensorType = Sensor.TYPE_ACCELEROMETER;
            sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);//停止
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //edit.putString("", value)
        if (sm != null) {
            sm.unregisterListener(myAccelerometerListener);//停止
        }

        JPushInterface.onPause(this);
    }

    public void startSensor() {
        if (sm == null) {
            registerSensor();
        }
        if (sm != null) {
            int sensorType = Sensor.TYPE_ACCELEROMETER;
            sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_NORMAL);//停止
        }
    }

    public void StopSenser() {
        if (sm == null) {
        }
        if (sm != null) {
            sm.unregisterListener(myAccelerometerListener);//停止

        }
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                //让Toast的显示时间和等待时间相同
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
