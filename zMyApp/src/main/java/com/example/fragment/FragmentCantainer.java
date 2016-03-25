package com.example.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.example.apitestapp.MainActivity;
import com.example.apitestapp.NewsDetail;
import com.example.apitestapp.R;
import com.example.base.Constants;
import com.example.entity.localnews.ContentList;
import com.example.entity.localnews.LocalNews;
import com.example.entity.weather.MyCityInfo;
import com.example.entity.weather.WeatherFutureJH;
import com.example.entity.weather.WeatherJH;
import com.example.entity.weather.WeatherNow;
import com.example.entity.weather.WeatherTodayJH;
import com.example.gaode_map.LocationModeSourceActivity;
import com.example.news.LookPointActivity;
import com.example.news.SportNewsActivity;
import com.example.utils.CityBIZ;
import com.example.utils.MatchWeatherIco;
import com.example.utils.URLEncoderUtils;
import com.example.view.MyChartItem;
import com.example.view.MyChartView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TreeMap;

public class FragmentCantainer extends Fragment {
    public static final int LOACTION_OK = 0;
    public static final int HOT_NEWS_IS_OK = 1;
    protected static final int UI_LOG_TO_VIEW = 0;
    private SpeechSynthesizer speechSynthesizer;
    private EditText inputTextView;
    private Button startButton;
    public TreeMap<String, Integer> treeMapICo;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ArrayList<Fragment> fragments;
    private ListView list_fragment;
    private SoundPool mSoundPool;
    private RequestQueue mQueue;
    private RequestQueue queue;
    private String URL_PM;
    public MatchWeatherIco weatherIco;
    View view;
    View backLay;
    WeatherJH mWeatherJH;
    WeatherTodayJH weatherTodayJH;
    WeatherNow weatherNow;
    WeatherFutureJH weatherFutureJH;
    ImageView imageView4_sound, bt3_aqi_icon_green, imageView2_tommorrow_wea,
            imageView1_today_wea;
    TextView tv_wind, textView3_aqi, tv_shidu, textView4_temper, tv_weathers,
            tv_today_temp_weather, tv_today_temper, tv_tomorrow_temper,
            tv_today_weather_tommorrow;
    Button ic_lifeindex_calendar, ic_lifeindex_sport, ic_lifeindex_coldl,
            ic_lifeindex_dress, ic_lifeindex_tour,
            ic_lifeindex_ultravioletrays;

    // city temperature weather wind }}} temp wind_direction wind_strength
    // humidity time
    public String city;// today
    public String date_y;
    public String temperature;
    public String weather;
    public String wind;
    public String dressing_index;
    public String dressing_advice;
    public String uv_index;
    public String wash_index;
    public String travel_index;
    public String exercise_index;
    public String drying_index;
    public String temp;// now
    public String wind_direction;
    public String wind_strength;
    public String humidity;
    public String time;
    ArrayList<WeatherJH> selectCityWeather;
    MyAdapter myAdapter;
    private String URL_ByCity;
    private String lon;
    private String lat;
    CityBIZ cityBIZ;
    public boolean flag;
    public double x;
    public double y;

    private MyReciver receiver;
    private IntentFilter filter;
    public int isFirst;

    private PullToRefreshListView mptrListView;

    protected LocalNews localNews;

    protected ArrayList<ContentList> contentlist;

    // private BitmapCache bitmapCache;

    private ImageLoader imageLoader;

    protected ArrayList<ContentList> hotNews;

    private ImageView[] tips;

    private int[] imgIdArray;

    private ViewPager viewPager;

    private ImageView[] mImageViews;
    StringBuffer buffer = null;
    private ViewGroup group;
    public int positions;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HOT_NEWS_IS_OK:
                    myAdapters.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

        }

        ;

    };
    private MyAdapters myAdapters;
    private BitmapCache bitmapCache;
    private TextView tv_hot_news_pic;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container, container, false);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        backLay = getActivity().findViewById(R.id.main_layout);
        mptrListView = (PullToRefreshListView) view
                .findViewById(R.id.pull_list_fragment);
        cityBIZ = new CityBIZ(getActivity());
        Bundle cityName = getArguments();
        city = cityName.getString("cityName");
        isFirst = cityName.getInt("isFirst");
        Log.e("lon", isFirst + "sd");
        Log.e("city", city);
        mQueue = Volley.newRequestQueue(getActivity());
        bitmapCache = new BitmapCache(); // 为滑动新闻
        imageLoader = new ImageLoader(mQueue, bitmapCache);
        myAdapters = new MyAdapters();
        if (localNews == null) {
            volleyForNews();// 热门新闻 网络请求
        } else {
            myAdapters.notifyDataSetChanged();
        }

        buffer = new StringBuffer();
        //city="上海";

        treeMapICo = new TreeMap<String, Integer>();
        weatherIco = new MatchWeatherIco();
        getBigIco();
        selectCityWeather = cityBIZ.getSelectCityWeather();
        URL_ByCity = "http://v.juhe.cn/weather/index?format=2&cityname=" + URLEncoderUtils.encode(city) + "&key=" + Constants.WEATHEAR_KEY;
        URL_PM = "http://v.juhe.cn/weather/geo?format=2&key=" + Constants.WEATHEAR_KEY + "&lon="
                + lon + "&lat=" + lat;
        registerBroadCast();// 注册广播
        Log.e("URL_ByCity", URL_ByCity);
        if (mWeatherJH == null) {
            volleyRequest(URL_ByCity);// 网络请求
        } else {
            mptrListView.setAdapter(myAdapter);
        }

        mptrListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // ��ø�������ʱ��
                String time = DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE);
                mptrListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mptrListView.getLoadingLayoutProxy().setPullLabel("刷新成功");
                mptrListView.getLoadingLayoutProxy().setReleaseLabel("下拉刷新");
                mptrListView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "上次刷新时间" + time);

                volleyRequest(URL_ByCity);
                volleyForNews();// 热门新闻 网络请求

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // �������������ı�
                String time = DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE);
                mptrListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mptrListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                mptrListView.getLoadingLayoutProxy().setReleaseLabel("刷新成功");
                mptrListView.getLoadingLayoutProxy().setLastUpdatedLabel(
                        "上次刷新时间" + time);
                // mptrListView.setBackgroundResource(resid);
                // volleyRequest(URL_ByCity);//刷新
                volleyRequest(URL_ByCity);
                volleyForNews();// 热门新闻 网络请求
                myAdapters.notifyDataSetChanged();
            }
        });
    }

    public void registerBroadCast() {
        receiver = new MyReciver();
        filter = new IntentFilter("reciver");
        getActivity().registerReceiver(receiver, filter);
    }

    class MyReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            MainActivity activity = (MainActivity) getActivity();
            if (intent.getAction().equals("reciver")) {
                MainActivity main = (MainActivity) getActivity();

                Log.e("lon", isFirst + "~~~~~~~~~");
                if (!(isFirst == 0)) {
                    return;
                }

                flag = true;
                lon = intent.getStringExtra("lon");
                lat = intent.getStringExtra("lat");
                Log.e("lon", lon + "~~~~~~~~~~~~~~~~" + lat);
                // 新数据
                URL_PM = "http://v.juhe.cn/weather/geo?format=2&key=" + Constants.WEATHEAR_KEY + "&lon="
                        + lon + "&lat=" + lat;
                volleyRequest(URL_PM);
                activity.stopClient();
            }

        }

    }

    public String volleyRequest(String URL) {
        // 新数据
        queue = Volley.newRequestQueue(getActivity());
        Log.e("URL", URL);
        StringRequest stringRequest = new StringRequest(URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("URL", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            ArrayList<WeatherJH> selectCityWeather2 = cityBIZ
                                    .getSelectCityWeather();
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i = 0; i < selectCityWeather2.size(); i++) {
                                list.add(selectCityWeather2.get(i).today.city);
                            }
                            Log.e("WeatherJH", response.toString());
                            if (response.toString().length() < 100) { // 判断网络是否错误
                                Log.e("WeatherJH", city);

                                for (int i = 0; i < list.size(); i++) {
                                    Log.e("WeatherJH", city);
                                    if (list.get(i).equals(city)) {
                                        mWeatherJH = selectCityWeather2.get(i);
                                        Log.e("WeatherJH",
                                                mWeatherJH.toString());
                                    }
                                    // Log.e("WeatherJH",
                                    // mWeatherJH.future.get(1).toString());
                                }
                            } else {
                                JSONObject result = object
                                        .getJSONObject("result");
                                Log.e("result", result.toString());
                                Gson gson = new Gson();
                                mWeatherJH = gson.fromJson(result.toString(),
                                        WeatherJH.class);
                                // Log.e("WeatherJH",
                                // mWeatherJH.future.get(1).toString());

                            }

                            if (list.contains(city)) { // 判断 城市列表是否存在该城市
                                cityBIZ.updateCityInfo(city, mWeatherJH);
                                Toast.makeText(getActivity(), "正在更新城市",
                                        Toast.LENGTH_SHORT).show();
                            } else if (isFirst == 0 && flag == true) {
                                cityBIZ.updateCityInfo(city, mWeatherJH);
                                Toast.makeText(getActivity(), "定位成功",
                                        Toast.LENGTH_SHORT).show();
                                ((MainActivity) getActivity())
                                        .setlocalName(mWeatherJH.today.city);
                            } else {
                                cityBIZ.addCityInfo(mWeatherJH);
                                TextView main_city = (TextView) getActivity()
                                        .findViewById(R.id.tv_weather);
                                main_city.setText(city);
                            }
                            pullDate();// 解析出数据给别人
                            myAdapter = new MyAdapter();
                            mptrListView.setAdapter(myAdapter);
                            // mptrListView.setItemsCanFocus(true);
                            Log.e("myAdapter", myAdapter.getCount() + "");
                            mptrListView.onRefreshComplete();
                            Toast.makeText(getActivity(), "更新成功",
                                    Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "请检查网络连接",
                        Toast.LENGTH_LONG).show();
                Log.e("WeatherJH", "错误");
                ArrayList<WeatherJH> selectCityWeather2 = cityBIZ
                        .getSelectCityWeather();
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < selectCityWeather2.size(); i++) {
                    list.add(selectCityWeather2.get(i).today.city);
                }
                for (int i = 0; i < list.size(); i++) {
                    Log.e("WeatherJH", city);
                    if (list.get(i).equals(city)) {
                        mWeatherJH = selectCityWeather2.get(i);
                        Log.e("WeatherJH",
                                "future" + mWeatherJH.future.toString());
                    }
                }

                pullDate();
                myAdapter = new MyAdapter();
                mptrListView.setAdapter(myAdapter);
                // mptrListView.setItemsCanFocus(true);
                Log.e("myAdapter", myAdapter.getCount() + "");

                mptrListView.onRefreshComplete();

            }

        });
        queue.add(stringRequest);

        return null;

    }

    public void pullDate() {
        if (mWeatherJH == null) {
            return;
        }
        weatherTodayJH = mWeatherJH.today;
        String localCity = weatherTodayJH.city;// today
        if (!localCity.equals(city)) {
            cityBIZ.updateCityInfo(city, mWeatherJH);
            ((MainActivity) getActivity()).setlocalName(localCity);
            Log.e("VolleyCityname", localCity);
            // city=weatherTodayJH.city;
        }
        Log.e("VolleyCityname", city);

        date_y = weatherTodayJH.date_y;
        temperature = weatherTodayJH.temperature;
        weather = weatherTodayJH.weather;
        wind = weatherTodayJH.wind;
        dressing_index = weatherTodayJH.dressing_index;
        dressing_advice = weatherTodayJH.dressing_advice;
        uv_index = weatherTodayJH.uv_index;
        wash_index = weatherTodayJH.wash_index;
        travel_index = weatherTodayJH.travel_index;
        exercise_index = weatherTodayJH.exercise_index;
        drying_index = weatherTodayJH.drying_index;
        weatherNow = mWeatherJH.sk;

        temp = weatherNow.temp;// now
        wind_direction = weatherNow.wind_direction;
        wind_strength = weatherNow.wind_strength;
        humidity = weatherNow.humidity;
        time = weatherNow.time;
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter() {

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 5;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            switch (position) {
                case 0:

                    convertView = View.inflate(getActivity(),
                            R.layout.weather_frame_layout, null);
                    bt3_aqi_icon_green = (ImageView) convertView
                            .findViewById(R.id.bt3);
                    imageView4_sound = (ImageView) convertView
                            .findViewById(R.id.imageView4);
                    TextView tv_update_time = (TextView) convertView
                            .findViewById(R.id.tv_update_time);
                    TextView tv_now_time = (TextView) convertView
                            .findViewById(R.id.tv_now_time);

                    // tv_wind,textView3_aqi,tv_shidu,textView4_temper,tv_weathers
                    tv_wind = (TextView) convertView.findViewById(R.id.tv_wind);
                    textView3_aqi = (TextView) convertView
                            .findViewById(R.id.textView3);
                    tv_shidu = (TextView) convertView.findViewById(R.id.tv_shidu);
                    textView4_temper = (TextView) convertView
                            .findViewById(R.id.textView4);
                    tv_weathers = (TextView) convertView
                            .findViewById(R.id.tv_weathers);

                    // city temperature weather wind }}} temp wind_direction
                    // wind_strength humidity time
                    textView4_temper.setText(temp + "℃");
                    if (time == null) {

                        tv_update_time.setText("更新时间:" + "5分钟之前");
                    }
                    tv_update_time.setText("更新时间:" + time);
                    Log.e("time", time + "");
                    tv_now_time.setText(date_y);
                    tv_wind.setText(wind_strength);
                    tv_shidu.setText(humidity);
                    tv_weathers.setText(mWeatherJH.today.weather);
                    Log.e("weather", mWeatherJH.today.weather);

                    textView3_aqi.setText((new Random().nextInt(70) + 30) + "");
                    MyspeechSynthesizer myspeechSynthesizer = new MyspeechSynthesizer();
                    speechSynthesizer = new SpeechSynthesizer(getActivity(),
                            "holder", myspeechSynthesizer);
                    // 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
                    speechSynthesizer.setApiKey("6K5xFGycnEvQ6dxCpiZHHo2t",
                            "8ec82689bafa773fcdf4d7028fefa575");
                    speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

                    final AlphaAnimation aa = new AlphaAnimation(1f, 0.1f);
                    aa.setRepeatCount(3);// --重复次数
                    aa.setRepeatMode(Animation.RESTART);
                    aa.setDuration(2 * 1000);
                    imageView4_sound.setAnimation(aa);

                    imageView4_sound.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Toast.makeText(getActivity(), "语音播报",
                                    Toast.LENGTH_SHORT).show();

                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    aa.start();
                                    setParams();
                                    int ret = speechSynthesizer
                                            .speak("今天天气         "
                                                    + mWeatherJH.today.weather
                                                    + "   当前温度" + temp
                                                    + "度         风力 "
                                                    + wind_strength + "  风向"
                                                    + wind_direction

                                                    + "   祝您  今天有个好心情");
                                    if (ret != 0) {
                                        Toast.makeText(getActivity(), "错误",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).start();

                        }
                    });
                    // convertView

                    break;
                case 1:

                    convertView = View.inflate(getActivity(),
                            R.layout.fragment2_layout, null);
                    TextView tv_today_weath = (TextView) convertView
                            .findViewById(R.id.tv_today_temp);
                    TextView tv_today_temper = (TextView) convertView
                            .findViewById(R.id.tv_today_temper);
                    TextView tv_tommrrow_weather = (TextView) convertView
                            .findViewById(R.id.tv_today_weather);
                    tv_tomorrow_temper = (TextView) convertView
                            .findViewById(R.id.tv_tomorrow_temper);
                    ImageView imageView_today_weath = (ImageView) convertView
                            .findViewById(R.id.imageView_today_weath);
                    ImageView imageView_tommorrow_weath = (ImageView) convertView
                            .findViewById(R.id.imageView_tommorrow_weath);
                    tv_today_weath.setText(mWeatherJH.future.get(0).weather);
                    tv_today_temper.setText(temperature);
                    WeatherFutureJH futureJH = null;
                    try {
                        futureJH = mWeatherJH.future.get(1);
                        tv_tommrrow_weather.setText(futureJH.weather);
                        tv_tomorrow_temper.setText(futureJH.temperature);
                    } catch (Exception e) {

                    }
                    try {
                        // 今天

                        imageView_today_weath
                                .setImageResource(setSmallIco(weather));
                        // Log.e("default1", weather);
                        // Log.e("default1", treeMapICo.get(weather)+"");

                        imageView_tommorrow_weath
                                .setImageResource(setSmallIco(futureJH.weather));
                        //
                    } catch (Exception e) {

                    }
                    Log.e("weatherIco", futureJH.weather);

                    break;
                case 2:

                    convertView = View.inflate(getActivity(),
                            R.layout.fragment3_layout, null);
                    ArrayList<MyChartItem> topList = new ArrayList<MyChartItem>();
                    ArrayList<Integer> icos = new ArrayList<Integer>();

                    ArrayList<Integer> icosNight = new ArrayList<Integer>();
                    ArrayList<Integer> lowList = new ArrayList<Integer>();
                    ArrayList<String> weathers = new ArrayList<String>();
                    if (mWeatherJH.future == null) {
                        break;
                    }

                    for (int i = 0; i < mWeatherJH.future.size(); i++) {
                        WeatherFutureJH weatherFutureJH2 = mWeatherJH.future.get(i);
                        String str = weatherFutureJH2.temperature;
                        Log.e("temp", str + "");
                        ArrayList<String> arrayList = new ArrayList<String>();
                        String string = str.replaceAll("℃", "");
                        int indexOf = string.indexOf("~");
                        String c = string.substring(0, indexOf);
                        String c1 = string.substring(indexOf + 1, string.length());

                        System.out.println(arrayList.size() + "SSS");
                        int h = Integer.valueOf(c1);
                        int l = Integer.valueOf(c);
                        Log.e("temp", str + "");
                        String week = weatherFutureJH2.week;
                        String week1 = week.replaceAll("星期", "周");
                        topList.add(new MyChartItem(week1, h, l));
                        String weather2 = weatherFutureJH2.weather;
                        if (weather2.length() > 3) {
                            weather2 = weather2.substring(0, 2);
                        }
                        weathers.add(weather2);
                        try {
                            int x = weatherIco.getSmallIco().get(
                                    weatherFutureJH2.weather);
                            int setSmallIco = setSmallIco(weatherFutureJH2.weather);

                        } catch (Exception e) {
                            icos.add(R.drawable.ww1);
                            icosNight.add(R.drawable.org3_ww1);
                            continue;
                        }
                        icosNight.add(setSmallIco(weatherFutureJH2.weather));
                        Log.e("drawText", weatherFutureJH2.weather + "ss");
                        icos.add(weatherIco.getSmallIco().get(
                                weatherFutureJH2.weather));
                        // icosNight.add(setSmallIcoNight(weatherFutureJH2.weather));
                        Log.e("weatherIco", weatherFutureJH2.weather + "ss");
                        Log.e("weatherIco",
                                ""
                                        + weatherIco.getSmallIco().get(
                                        weatherFutureJH2.weather));

                    }

                    MyChartView tu = (MyChartView) convertView
                            .findViewById(R.id.myChartView1);

                    tu.SetTuView(topList, "", icos, icosNight, weathers);

                    break;
                case 3:
                    convertView = View.inflate(getActivity(),
                            R.layout.hot_news_layout, null);
                    case3Load(convertView);

                    break;

                case 4:
                    convertView = View.inflate(getActivity(),
                            R.layout.fragment4_layout, null);
                    ic_lifeindex_calendar = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_calendar);
                    ic_lifeindex_sport = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_sport);
                    ic_lifeindex_coldl = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_coldl);
                    ic_lifeindex_dress = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_dress);
                    ic_lifeindex_tour = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_tour);
                    ic_lifeindex_ultravioletrays = (Button) convertView
                            .findViewById(R.id.ic_lifeindex_ultravioletrays);
                    ic_lifeindex_calendar.setText(date_y);
                    ic_lifeindex_sport.setText(exercise_index);
                    // ic_lifeindex_coldl.setText(dressing_index);
                    ic_lifeindex_dress.setText(dressing_index);
                    // if (travel_index.length()!=0) {
                    // ic_lifeindex_tour.setText(travel_index);
                    // }

                    // ic_lifeindex_ultravioletrays.setText(uv_index);

                    ic_lifeindex_calendar.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent();
                            intent.setComponent((new ComponentName(
                                    "com.android.calendar",
                                    "com.android.calendar.LaunchActivity")));
                            getActivity().startActivity(intent);
                        }
                    });

                    ic_lifeindex_sport.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intentToNew = new Intent(getActivity(),
                                    SportNewsActivity.class);
                            startActivity(intentToNew);

                        }
                    });

                    ic_lifeindex_dress.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intentToNew = new Intent(
                                    "android.intent.action.NewsActivity");
                            Toast.makeText(getActivity(), dressing_advice,
                                    Toast.LENGTH_SHORT).show();
                            MyCityInfo checkCity = cityBIZ.checkCity(city);
                            ArrayList<String> strings = new ArrayList<String>();
                            if (checkCity == null || checkCity.province == null) {
                                return;
                            }
                            intentToNew.putExtra("cityName", checkCity.province);
                            startActivity(intentToNew);
                        }
                    });

                    ic_lifeindex_ultravioletrays
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent intent11 = new Intent(getActivity(), LocationModeSourceActivity.class);
                                    startActivity(intent11);

                                }
                            });
                    ic_lifeindex_tour.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(getActivity(),
                                    LookPointActivity.class);
                            MyCityInfo checkCity = cityBIZ.checkCity(city);
                            ArrayList<String> strings = new ArrayList<String>();

                            intent.putExtra("cityName", checkCity.city);
                            startActivity(intent);

                        }
                    });
                    ic_lifeindex_coldl.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(
                                    "com.intent.example.news.HealthNewsActivity");
                            startActivity(intent);
                        }
                    });

                    break;
                default:
                    break;
            }

            return convertView;
        }

    }

    public void case3Load(View convertView) {
        group = (ViewGroup) convertView.findViewById(R.id.viewGroup1);
        tv_hot_news_pic = (TextView) convertView
                .findViewById(R.id.tv_hot_news_pic);

        viewPager = (ViewPager) convertView.findViewById(R.id.viewPager1);
        imgIdArray = new int[]{R.drawable.bg0_fine_day,
                R.drawable.bg_cloudy_day, R.drawable.bg_fog_and_haze};

        if (hotNews != null) {
            tips = new ImageView[hotNews.size()];
            for (int i = 0; i < hotNews.size(); i++) {
                tv_hot_news_pic.setText(hotNews.get(i).title);
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new LayoutParams(10, 10));
                tips[i] = imageView;
                if (i == 0) {
                    tips[i].setBackgroundResource(R.drawable.ww1);
                } else {
                    tips[i].setBackgroundResource(R.drawable.ww0);

                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,

                                LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 5;
                layoutParams.rightMargin = 5;
                group.addView(imageView, layoutParams);

            }
            mImageViews = new ImageView[hotNews.size()];
            for (int i = 0; i < hotNews.size(); i++) {
                ImageView imageView = new ImageView(getActivity());

                ImageListener iglistener = ImageLoader.getImageListener(
                        imageView, R.drawable.share_photo_gallery,
                        R.drawable.share_photo_gallery);
                imageLoader
                        .get(hotNews.get(i).imageurls.get(0).url, iglistener);
                mImageViews[i] = imageView;
                imageView.setBackgroundResource(imgIdArray[1]);
            }

        } else {
            tips = new ImageView[imgIdArray.length];
            for (int i = 0; i < tips.length; i++) {

                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new LayoutParams(10, 10));
                tips[i] = imageView;
                if (i == 0) {
                    tips[i].setBackgroundResource(R.drawable.ww1);
                } else {
                    tips[i].setBackgroundResource(R.drawable.ww0);

                }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,

                                LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 5;
                layoutParams.rightMargin = 5;
                group.addView(imageView, layoutParams);
            }

            mImageViews = new ImageView[imgIdArray.length];
            for (int i = 0; i < mImageViews.length; i++) {
                ImageView imageView = new ImageView(getActivity());
                mImageViews[i] = imageView;

                imageView.setBackgroundResource(imgIdArray[i]);

            }

        }

        // 设置Adapter
        myAdapters = new MyAdapters();
        viewPager.setAdapter(myAdapters);

        // 设置监听，主要是设置点点的背景

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                setImageBackground(arg0);
                if (hotNews == null || hotNews.size() == 0
                        || hotNews.get(arg0).title == null) {
                    return;
                }
                tv_hot_news_pic.setText(hotNews.get(arg0).title);
                positions = arg0;
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

        viewPager.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("viewPager", "onclick");
            }
        });

        // 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        // viewPager.setCurrentItem((mImageViews.length));
    }

    public class MyAdapters extends PagerAdapter {

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

            ((ViewPager) container).removeView(mImageViews[position
                    % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {

            ((ViewPager) container).addView(mImageViews[position], 0);
            mImageViews[position]
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Log.e("onclick", "onclick");
                            Intent intent = new Intent(getActivity(),
                                    NewsDetail.class);
                            intent.putExtra("link", hotNews.get(positions).link);
                            Log.e("arg0",
                                    positions + "~~"
                                            + hotNews.get(positions).link);
                            startActivity(intent);
                        }

                    });

            return mImageViews[position];

        }

    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.ww1);
            } else {
                tips[i].setBackgroundResource(R.drawable.ww0);
            }

        }

    }

    private void setParams() {
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,
                SpeechSynthesizer.AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,
                SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE,
        // SpeechSynthesizer.LANGUAGE_ZH);
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }

    class MyspeechSynthesizer implements SpeechSynthesizerListener {

        @Override
        public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onCancel(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1,
                                    boolean arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechFinish(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechPause(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechResume(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechStart(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartWorking(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSynthesizeFinish(SpeechSynthesizer arg0) {
            // TODO Auto-generated method stub

        }

    }

    // public class MyLocationListenner implements BDLocationListener {
    //
    //
    // @Override
    // public void onReceiveLocation(BDLocation location) {
    // // map view 销毁后不在处理新接收的位置
    // if (location == null ) {
    // return;
    // }
    //
    // if(flag==false){
    // x=location.getLatitude();
    // y=location.getLongitude();
    //
    // //Log.e("addresss", location.getAddrStr());
    // Log.e("location", location.getLatitude()+"~~"+location.getLongitude());
    // flag=true;
    // mHandler.sendEmptyMessage(LOACTION_OK);
    //
    // }
    // }
    //
    // }
    //

    public void getBigIco() {

        treeMapICo.put("晴", R.drawable.org3_ww0);
        treeMapICo.put("多云", R.drawable.org3_ww1);
        treeMapICo.put("阴", R.drawable.org3_ww2);
        treeMapICo.put("阵雨", R.drawable.org3_ww3);
        treeMapICo.put("雷阵雨", R.drawable.org3_ww4);
        treeMapICo.put("雷阵雨伴有冰雹", R.drawable.org3_ww5);
        treeMapICo.put("雨夹雪", R.drawable.org3_ww6);
        treeMapICo.put("小雨", R.drawable.org3_ww7);
        treeMapICo.put("中雨", R.drawable.org3_ww8);
        treeMapICo.put("大雨", R.drawable.org3_ww9);
        treeMapICo.put("暴雨", R.drawable.org3_ww10);
        treeMapICo.put("大暴雨", R.drawable.org3_ww10);
        treeMapICo.put("特大暴雨", R.drawable.org3_ww10);
        treeMapICo.put("阵雪", R.drawable.org3_ww13);
        treeMapICo.put("小雪", R.drawable.org3_ww14);
        treeMapICo.put("中雪", R.drawable.org3_ww15);
        treeMapICo.put("大雪", R.drawable.org3_ww16);
        treeMapICo.put("暴雪", R.drawable.org3_ww17);
        treeMapICo.put("雾", R.drawable.org3_ww18);
        treeMapICo.put("冻雨", R.drawable.org3_ww19);
        treeMapICo.put("沙尘暴", R.drawable.org3_ww20);
        treeMapICo.put("小雨转中雨", R.drawable.org3_ww8);
        treeMapICo.put("中雨转小雨", R.drawable.org3_ww8);
        treeMapICo.put("中雨转大雨", R.drawable.org3_ww8);
        treeMapICo.put("大雨转暴雨", R.drawable.org3_ww9);
        treeMapICo.put("暴雨-大暴雨", R.drawable.org3_ww10);
        treeMapICo.put("浮尘", R.drawable.org3_ww20);
        treeMapICo.put("扬沙", R.drawable.org3_ww20);
        treeMapICo.put("强沙尘暴", R.drawable.org3_ww20);
        treeMapICo.put("霾", R.drawable.org3_ww20);
        treeMapICo.put("多云转阴", R.drawable.org3_ww1);
        treeMapICo.put("阴转小雨", R.drawable.org3_ww7);
        treeMapICo.put("小雨转多云", R.drawable.org3_ww3);

    }

    public int setSmallIcoNight(String weather) {

        if (weather.contains("大雪")) {
            Log.e("setSmallIco", "大雪");
            return R.drawable.ww34;
        } else if (weather.contains("雪")) {
            Log.e("setSmallIco", "大雪");
            return R.drawable.ww34;
        } else if (weather.contains("大雨")) {
            Log.e("setSmallIco", "大雨");
            return R.drawable.ww33;
        } else if (weather.contains("阵雨")) {
            return R.drawable.ww33;
        } else if (weather.contains("中雨")) {
            return R.drawable.ww33;
        } else if (weather.contains("雨")) {
            return R.drawable.ww33;
        } else if (weather.contains("多云")) {
            Log.e("setSmallIco", "多云");
            return R.drawable.ww31;
        } else if (weather.contains("阴")) {
            Log.e("setSmallIco", "阴");
            return R.drawable.ww31;
        } else if (weather.contains("晴")) {
            Log.e("setSmallIco", "请");
            return R.drawable.ww30;
        } else {
            return R.drawable.ww31;
        }

    }

    public int setSmallIco(String weather) {

        if (weather.contains("大雪")) {
            Log.e("setSmallIco", "大雪");
            return R.drawable.org3_ww16;
        } else if (weather.contains("雪")) {
            Log.e("setSmallIco", "大雪");
            return R.drawable.org3_ww15;
        } else if (weather.contains("大雨")) {
            Log.e("setSmallIco", "大雨");
            return R.drawable.org3_ww9;
        } else if (weather.contains("阵雨")) {
            return R.drawable.org3_ww9;
        } else if (weather.contains("中雨")) {
            return R.drawable.org3_ww9;
        } else if (weather.contains("雨")) {
            return R.drawable.org3_ww7;
        } else if (weather.contains("多云")) {
            Log.e("setSmallIco", "多云");
            return R.drawable.org3_ww1;
        } else if (weather.contains("阴")) {
            Log.e("setSmallIco", "阴");
            return R.drawable.org3_ww2;
        } else if (weather.contains("晴")) {
            Log.e("setSmallIco", "请");
            return R.drawable.org3_ww0;
        } else {
            return R.drawable.org3_ww20;
        }

    }

    public void volleyForNews() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = format.format(new Date(System.currentTimeMillis()));
        MyCityInfo checkCity = cityBIZ.checkCity(city);
        ArrayList<String> strings = new ArrayList<String>();
        Log.e("arg0", "checkCity" + checkCity);
        if (checkCity == null) {
            return;
        }

        String url = "https://route.showapi.com/170-47?areaId=&areaName="
                + URLEncoderUtils.encode(checkCity.province)
                + "&page=1&showapi_appid=11768&showapi_timestamp=" + str
                + "&title=&showapi_sign=2ccc2fc74f924717aabdd30027c05a8c";
        StringRequest stringRequest1 = new StringRequest(url,
                new Listener<String>() {

                    public void onResponse(String arg0) {
                        Log.e("arg0", "onResponse" + arg0);
                        try {
                            JSONObject jsonObject = new JSONObject(arg0);
                            JSONObject jsonObject1 = jsonObject
                                    .getJSONObject("showapi_res_body");
                            JSONObject jsonObject2 = jsonObject1
                                    .getJSONObject("pagebean");
                            Gson gson = new Gson();
                            localNews = gson.fromJson(jsonObject2.toString(),
                                    LocalNews.class);
                            Log.e("arg0", localNews.toString());
                            contentlist = localNews.contentlist;
                            hotNews = new ArrayList<ContentList>();
                            for (int i = 0; i < contentlist.size(); i++) {
                                if (null != contentlist.get(i).imageurls
                                        && contentlist.get(i).imageurls.size() > 0) {
                                    hotNews.add(contentlist.get(i));
                                }
                            }
                            handler.sendEmptyMessage(HOT_NEWS_IS_OK);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "请检查网络",
                        Toast.LENGTH_SHORT).show();
                mptrListView.onRefreshComplete();

            }

        });

        mQueue.add(stringRequest1);
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


}
