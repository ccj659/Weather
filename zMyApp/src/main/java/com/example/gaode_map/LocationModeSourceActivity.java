package com.example.gaode_map;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnPOIClickListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.apitestapp.R;
import com.example.gaode_map.navi.BasicNavActivity;
import com.example.gaode_map.route.RouteSearchPoiDialog;
import com.example.gaode_map.util.AMapUtil;
import com.example.gaode_map.util.ToastUtil;
import com.example.view.ArcMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * AMapV2地图中介绍定位三种模式的使用，包括定位，追随，旋转
 */
public class LocationModeSourceActivity extends Activity implements LocationSource,
        AMapLocationListener, OnCheckedChangeListener, OnPOIClickListener,
        OnMarkerClickListener, TextWatcher, PoiSearch.OnPoiSearchListener, RouteSearch.OnRouteSearchListener, View.OnClickListener, AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private TextView mLocationErrText;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private Marker regeoMarker;
    private String addressName;

    private ArcMenu mArcMenu;
    private String shareMassage;
    private View lay_poisearch;
    private View lay_route_overley;
    private MapView mapView;
    private Button drivingButton;
    private Button busButton;
    private Button walkButton;
    private RelativeLayout relativeLayout_roadsearch_top;
    private LinearLayout ll_point_search;
    private ImageButton startImageButton;
    private ImageButton endImageButton;
    private ImageButton routeSearchImagebtn;

    private EditText startTextView;
    private EditText endTextView;
    private int busMode = RouteSearch.BusDefault;// 公交默认模式
    private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
    private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
    private BusRouteResult busRouteResult;// 公交模式查询结果
    private DriveRouteResult driveRouteResult;// 驾车模式查询结果
    private WalkRouteResult walkRouteResult;// 步行模式查询结果
    private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式
    private String strStart;
    private String strEnd;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private PoiSearch.Query startSearchQuery;
    private PoiSearch.Query endSearchQuery;

    private boolean isClickStart = false;
    private boolean isClickTarget = false;
    private Marker startMk, targetMk;
    private RouteSearch routeSearch;
    public ArrayAdapter<String> aAdapter;
    private TextView imagebtn_roadsearch_guide;
    private boolean isSearch = false;


    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private EditText editCity;// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private boolean isRouteSearchs = false;
    private boolean isPointSearchs = false;
    private double localLongitude, localLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.locationmodesource_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        initView();

        initArcMenu();

        initLocal();
        initSearch();
        initRoute();


    }

    private void initView() {
        if (ll_point_search == null || relativeLayout_roadsearch_top == null) {
            ll_point_search = (LinearLayout) findViewById(R.id.ll_point_search);
            relativeLayout_roadsearch_top = (RelativeLayout) findViewById(R.id.relativeLayout_roadsearch_top);
        }
        ll_point_search.setVisibility(View.INVISIBLE);
        relativeLayout_roadsearch_top.setVisibility(View.INVISIBLE);


    }

    private void initLocal() {


        //mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        //mGPSModeGroup.setOnCheckedChangeListener(this);
        mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
        aMap.setOnPOIClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    private void initSearch() {
        Button searButton = (Button) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        editCity = (EditText) findViewById(R.id.city);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }


    private void initRoute() {
        if (aMap == null) {
            aMap = mapView.getMap();
            registerListener();
        }
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
        endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
        busButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
        busButton.setOnClickListener(this);
        drivingButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
        drivingButton.setOnClickListener(this);
        walkButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);
        walkButton.setOnClickListener(this);
        startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
        startImageButton.setOnClickListener(this);
        endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
        endImageButton.setOnClickListener(this);
        routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
        routeSearchImagebtn.setOnClickListener(this);
        imagebtn_roadsearch_guide = (TextView) findViewById(R.id.imagebtn_roadsearch_guide);
        imagebtn_roadsearch_guide.setOnClickListener(this);
    }


    /**
     * 设置一些amap的属性
     */

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        //showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
//		case R.id.gps_locate_button:
//			// 设置定位的类型为定位模式
//			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//			break;
//		case R.id.gps_follow_button:
//			// 设置定位的类型为 跟随模式
//			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
//			break;
//		case R.id.gps_rotate_button:
//			// 设置定位的类型为根据地图面向方向旋转
//			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
//			break;
        }

    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                //Toast.makeText(getBaseContext(), amapLocation.getAddress(), Toast.LENGTH_SHORT).show();
                localLongitude = amapLocation.getLongitude();
                localLatitude = amapLocation.getLatitude();

                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Log.e("amapLocation", amapLocation.getLocationDetail() + "~" + amapLocation.getDistrict());

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //底图poi点击回调
    @Override
    public void onPOIClick(Poi poi) {
        aMap.clear();

        MarkerOptions markOptiopns = new MarkerOptions();
        markOptiopns.position(poi.getCoordinate());
        TextView textView = new TextView(getApplicationContext());
        textView.setText(poi.getName());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.custom_info_bubble);
        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
//View infoWindow = getLayoutInflater().inflate(
//			R.layout.custom_info_window, null);
//			AlertDialog.Builder builder =new AlertDialog.Builder(LocationModeSourceActivity.this);
//			builder.setView(infoWindow);
//			ImageView imageView = (ImageView) infoWindow.findViewById(R.id.iv_sound);
//			imageView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Toast.makeText(getBaseContext(), "声音", Toast.LENGTH_SHORT).show();
//				}
//			});
//			builder.show();

        aMap.addMarker(markOptiopns);
        setUpMap();
    }

//		@Override
//		public boolean onMarkerClick(Marker marker) {
//
//			Toast.makeText(getBaseContext(), "点击我了", Toast.LENGTH_SHORT).show();
//
////			// 构造导航参数
////			NaviPara naviPara = new NaviPara();
////			// 设置终点位置
////			naviPara.setTargetPoint(marker.getPosition());
////			// 设置导航策略，这里是避免拥堵
////			naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
////			try {
////				// 调起高德地图导航
////				AMapUtils.openAMapNavi(naviPara, getApplicationContext());
////			} catch (com.amap.api.maps.AMapException e) {
////				// 如果没安装会进入异常，调起下载页面
////				AMapUtils.getLatestAMapApp(getApplicationContext());
////			}
//			//aMap.clear();
//			//setUpMap();
//			return false;
//		}

    /**
     * 选择公交模式
     */
    private void busRoute() {
        routeType = 1;// 标识为公交模式
        busMode = RouteSearch.BusDefault;
        drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
        busButton.setBackgroundResource(R.drawable.mode_transit_on);
        walkButton.setBackgroundResource(R.drawable.mode_walk_off);

    }

    /**
     * 选择驾车模式
     */
    private void drivingRoute() {
        routeType = 2;// 标识为驾车模式
        drivingButton.setBackgroundResource(R.drawable.mode_driving_on);
        busButton.setBackgroundResource(R.drawable.mode_transit_off);
        walkButton.setBackgroundResource(R.drawable.mode_walk_off);
    }

    /**
     * 选择步行模式
     */
    private void walkRoute() {
        routeType = 3;// 标识为步行模式
        walkMode = RouteSearch.WalkMultipath;
        drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
        busButton.setBackgroundResource(R.drawable.mode_transit_off);
        walkButton.setBackgroundResource(R.drawable.mode_walk_on);
    }

    /**
     * 在地图上选取起点
     */
    private void startImagePoint() {
        ToastUtil.show(LocationModeSourceActivity.this, "在地图上点击您的起点");
        isClickStart = true;
        isClickTarget = false;
        registerListener();
    }

    /**
     * 在地图上选取终点
     */
    private void endImagePoint() {
        ToastUtil.show(LocationModeSourceActivity.this, "在地图上点击您的终点");
        isClickTarget = true;
        isClickStart = false;
        registerListener();
    }

    /**
     * 点击搜索按钮开始Route搜索
     */
    public void searchRoute() {
        strStart = startTextView.getText().toString().trim();
        strEnd = endTextView.getText().toString().trim();
        if (strStart == null || strStart.length() == 0) {
            ToastUtil.show(LocationModeSourceActivity.this, "请选择起点");
            return;
        }
        if (strEnd == null || strEnd.length() == 0) {
            ToastUtil.show(LocationModeSourceActivity.this, "请选择终点");
            return;
        }

        if (strStart.equals(strEnd)) {
            ToastUtil.show(LocationModeSourceActivity.this, "起点与终点距离很近，您可以步行前往");
            return;
        }
        isSearch = true;
        startSearchResult();// 开始搜终点
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        isClickStart = false;
        isClickTarget = false;
        if (marker.equals(startMk)) {
            startTextView.setText("地图上的起点");
            startPoint = AMapUtil.convertToLatLonPoint(startMk.getPosition());
            startMk.hideInfoWindow();
            startMk.remove();
        } else if (marker.equals(targetMk)) {
            endTextView.setText("地图上的终点");
            endPoint = AMapUtil.convertToLatLonPoint(targetMk.getPosition());
            targetMk.hideInfoWindow();
            targetMk.remove();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latng) {
        if (isClickStart) {
            startMk = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.point)).position(latng)
                    .title("点击选择为起点"));
            startMk.showInfoWindow();
        } else if (isClickTarget) {
            targetMk = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.point)).position(latng)
                    .title("点击选择为目的地"));
            targetMk.showInfoWindow();
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(LocationModeSourceActivity.this);
        aMap.setOnMarkerClickListener(LocationModeSourceActivity.this);
        aMap.setOnInfoWindowClickListener(LocationModeSourceActivity.this);
        aMap.setInfoWindowAdapter(LocationModeSourceActivity.this);
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 查询路径规划起点
     */
    public void startSearchResult() {
        strStart = startTextView.getText().toString().trim();
        if (startPoint != null && strStart.equals("地图上的起点")) {
            endSearchResult();
        } else {
            showProgressDialog();
            startSearchQuery = new PoiSearch.Query(strStart, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
            PoiSearch poiSearch = new PoiSearch(LocationModeSourceActivity.this,
                    startSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();// 异步poi查询
        }
    }

    /**
     * 查询路径规划终点
     */
    public void endSearchResult() {
        strEnd = endTextView.getText().toString().trim();
        if (endPoint != null && strEnd.equals("地图上的终点")) {
            searchRouteResult(startPoint, endPoint);
        } else {
            showProgressDialog();
            endSearchQuery = new PoiSearch.Query(strEnd, "", "010"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
            endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
            endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

            PoiSearch poiSearch = new PoiSearch(LocationModeSourceActivity.this,
                    endSearchQuery);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn(); // 异步poi查询
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        if (routeType == 1) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, busMode, "北京", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == 2) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, drivingMode,
                    null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == 3) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, walkMode);
            routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }


    /**
     * POI搜索结果回调
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();
        if (isRouteSearchs) {
            routeSearchs(result, rCode);
        } else if (isPointSearchs) {
            PointSearchs(result, rCode);
        }

    }

    private void PointSearchs(PoiResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                    } else {
                        ToastUtil.show(LocationModeSourceActivity.this, "无结果");
                    }
                } else if (rCode == 27) {
                    ToastUtil.show(LocationModeSourceActivity.this, "网络错误");
                } else if (rCode == 32) {
                    ToastUtil.show(LocationModeSourceActivity.this, "错误");
                } else {
                    ToastUtil.show(LocationModeSourceActivity.this, "其它错误");
                }
            }
        }
    }

    private void routeSearchs(PoiResult result, int rCode) {
        if (rCode == 0) {// 返回成功
            if (result != null && result.getQuery() != null
                    && result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
                if (result.getQuery().equals(startSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
                            LocationModeSourceActivity.this, poiItems);
                    dialog.setTitle("您要找的起点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new RouteSearchPoiDialog.OnListItemClick() {
                        @Override
                        public void onListItemClick(
                                RouteSearchPoiDialog dialog,
                                PoiItem startpoiItem) {
                            startPoint = startpoiItem.getLatLonPoint();
                            strStart = startpoiItem.getTitle();
                            startTextView.setText(strStart);
                            endSearchResult();// 开始搜终点
                        }

                    });
                } else if (result.getQuery().equals(endSearchQuery)) {
                    List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
                    RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
                            LocationModeSourceActivity.this, poiItems);
                    dialog.setTitle("您要找的终点是:");
                    dialog.show();
                    dialog.setOnListClickListener(new RouteSearchPoiDialog.OnListItemClick() {
                        @Override
                        public void onListItemClick(
                                RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
                            endPoint = endpoiItem.getLatLonPoint();
                            strEnd = endpoiItem.getTitle();
                            endTextView.setText(strEnd);
                            searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
                        }

                    });
                }
            } else {
                ToastUtil.show(LocationModeSourceActivity.this, "无结果");
            }
        } else if (rCode == 27) {
            ToastUtil.show(LocationModeSourceActivity.this, "网络错误");
        } else if (rCode == 32) {
            ToastUtil.show(LocationModeSourceActivity.this, "错误");
        } else {
            ToastUtil.show(LocationModeSourceActivity.this, "其它错误");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 公交路线查询回调
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                busRouteResult = result;
                BusPath busPath = busRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
                        busPath, busRouteResult.getStartPos(),
                        busRouteResult.getTargetPos());
                routeOverlay.removeFromMap();
                routeOverlay.addToMap();
                routeOverlay.zoomToSpan();
            } else {
                ToastUtil.show(LocationModeSourceActivity.this, "无结果");
            }
        } else if (rCode == 27) {
            ToastUtil.show(LocationModeSourceActivity.this, "网络错误");
        } else if (rCode == 32) {
            ToastUtil.show(LocationModeSourceActivity.this, "错误");
        } else {
            ToastUtil.show(LocationModeSourceActivity.this, "其它错误");
        }
    }

    /**
     * 驾车结果回调
     */
    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                driveRouteResult = result;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this, aMap, drivePath, driveRouteResult.getStartPos(),
                        driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                ToastUtil.show(LocationModeSourceActivity.this, "无结果");
            }
        } else if (rCode == 27) {
            ToastUtil.show(LocationModeSourceActivity.this, "网络错误");
        } else if (rCode == 32) {
            ToastUtil.show(LocationModeSourceActivity.this, "错误");
        } else {
            ToastUtil.show(LocationModeSourceActivity.this, "其它错误");
        }
    }

    /**
     * 步行路线结果回调
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        dissmissProgressDialog();
        if (rCode == 0) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                walkRouteResult = result;
                WalkPath walkPath = walkRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
                        aMap, walkPath, walkRouteResult.getStartPos(),
                        walkRouteResult.getTargetPos());
                walkRouteOverlay.removeFromMap();
                walkRouteOverlay.addToMap();
                walkRouteOverlay.zoomToSpan();
            } else {
                ToastUtil.show(LocationModeSourceActivity.this, "无结果");
            }
        } else if (rCode == 27) {
            ToastUtil.show(LocationModeSourceActivity.this, "网络错误");
        } else if (rCode == 32) {
            ToastUtil.show(LocationModeSourceActivity.this, "错误");
        } else {
            ToastUtil.show(LocationModeSourceActivity.this, "其它错误");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebtn_roadsearch_startoption:
                startImagePoint();
                break;
            case R.id.imagebtn_roadsearch_endoption:
                endImagePoint();
                break;
            case R.id.imagebtn_roadsearch_tab_transit:
                busRoute();
                break;
            case R.id.imagebtn_roadsearch_tab_driving:
                drivingRoute();
                break;
            case R.id.imagebtn_roadsearch_tab_walk:
                walkRoute();
                break;
            case R.id.imagebtn_roadsearch_search:

                searchRoute();

                break;
            case R.id.imagebtn_roadsearch_guide:
                if (!isSearch) {
                    ToastUtil.show(this, "请先规划路径");
                    return;
                }

                Intent intent = new Intent(LocationModeSourceActivity.this, BasicNavActivity.class);
                ArrayList<String> strings = new ArrayList<>();
                strings.add(startPoint.getLatitude() + "");
                strings.add(startPoint.getLongitude() + "");
                strings.add(endPoint.getLatitude() + "");
                strings.add(endPoint.getLongitude() + "");
//				NaviLatLng[] naviLatLngs =new NaviLatLng[2];
//				naviLatLngs[0]=new NaviLatLng(startPoint.getLatitude(),startPoint.getLongitude());
//				naviLatLngs[1]=new NaviLatLng(endPoint.getLatitude(), endPoint.getLongitude());
                Log.e("strings", startPoint.getLatitude() + "~" + endPoint.getLatitude());
                intent.putStringArrayListExtra("strings", strings);
                startActivity(intent);

                break;
            /**
             * 点击搜索按钮
             */
            case R.id.searchButton:
                searchButton();
                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                nextButton();
                break;
            default:
                break;
        }
    }


    int i = 0;

    private void initArcMenu() {
        mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                //Toast.makeText(LocationDemo.this, pos+":"+view.getTag(), Toast.LENGTH_SHORT).show();
                //view.getId();
                initView();
                switch (pos) {
                    case 1:
                        isRouteSearchs = false;
                        isPointSearchs = true;
                        if (ll_point_search.getVisibility() == View.VISIBLE) {
                            ll_point_search.setVisibility(View.INVISIBLE);
                        } else {
                            ll_point_search.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(LocationModeSourceActivity.this, "周边搜索", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        isRouteSearchs = true;
                        isPointSearchs = false;
                        Toast.makeText(LocationModeSourceActivity.this, "路线导航", Toast.LENGTH_SHORT).show();

                        if (relativeLayout_roadsearch_top.getVisibility() == View.VISIBLE) {
                            relativeLayout_roadsearch_top.setVisibility(View.INVISIBLE);
                        } else {
                            relativeLayout_roadsearch_top.setVisibility(View.VISIBLE);
                        }
//						lay_poisearch.setVisibility(View.GONE);
//						lay_route_overley.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        isRouteSearchs = false;
                        isPointSearchs = false;
                        if (i == 0) {
                            // 设置定位的类型为定位模式
                            i++;
                            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                        } else if (i == 1) {
                            // 设置定位的类型为 跟随模式
                            i++;
                            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                        } else if (i == 2) {
                            // 设置定位的类型为根据地图面向方向旋转
                            i = 0;
                            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                        }


                        break;
                    case 4:
                        if (shareMassage == null) {
                            Toast.makeText(LocationModeSourceActivity.this, "请点击周边热点后点击", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intents = new Intent(Intent.ACTION_SEND);
                        intents.setType("text/plain"); //"image*//*"
                        intents.putExtra(Intent.EXTRA_SUBJECT, "炫彩天气提醒您");

                        intents.putExtra(Intent.EXTRA_TEXT, shareMassage
                        );

                        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intents, "选择分享类型"));

                        break;
                    case 5:
                        Toast.makeText(LocationModeSourceActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LocationModeSourceActivity.this);
                        builder.setTitle("确定退出?");
                        builder.setPositiveButton("再见", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }

                        });
                        builder.setNegativeButton("再玩一会", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }

                        });

                        builder.show();
                        break;
                    default:
                        break;
                }


            }
        });
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, editCity.getText().toString());
        Inputtips inputTips = new Inputtips(LocationModeSourceActivity.this, inputquery);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                if (i == 0) {// 正确返回
                    List<String> listString = new ArrayList<String>();
                    for (int r = 0; r < list.size(); r++) {
                        listString.add(list.get(r).getName());
                    }
                    ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.route_inputs, listString);
                    searchText.setAdapter(aAdapter);
                    aAdapter.notifyDataSetChanged();
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = AMapUtil.checkEditText(searchText);
        if ("".equals(keyWord)) {
            ToastUtil.show(LocationModeSourceActivity.this, "请输入搜索关键字");
            return;
        } else {
            doSearchQuery();
        }
    }

    /**
     * 点击下一页按钮
     */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                ToastUtil.show(this,
                        "无结果");
            }
        }
    }

    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationModeSourceActivity.this, BasicNavActivity.class);
                ArrayList<String> strings = new ArrayList<>();
                strings.add(localLatitude + "");
                strings.add(localLongitude + "");
                strings.add(marker.getPosition().latitude + "");
                strings.add(marker.getPosition().longitude + "");
                intent.putStringArrayListExtra("strings", strings);
                startActivity(intent);

            }
        });
        return view;
    }

}
