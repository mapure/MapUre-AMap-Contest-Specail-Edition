package com.mapure.amap.contest.ui.fagment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.*;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.overlay.PoiOverlay;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.PlaceDetailActivity;
import com.mapure.amap.contest.util.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

public class WHUMapFragment extends SupportMapFragment implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private AMap aMap;

    private Marker whuDoorMarker;
    private Marker sakuraRoadMarker;
    private Marker historyDeptMarker;
    private Marker songQingSportCenterMarker;
    private Marker sixOnePavilionMarker;
    private Marker newLibraryMarker;

    private static final LatLng marker1 = new LatLng(30.533061, 114.357839);
    private static final LatLng marker2 = new LatLng(30.539362, 114.364114);
    private static final LatLng marker3 = new LatLng(30.539029, 114.367433);
    private static final LatLng marker4 = new LatLng(30.536938, 114.361374);
    private static final LatLng marker5 = new LatLng(30.538275, 114.361855);
    private static final LatLng marker6 = new LatLng(30.535836, 114.362252);

    private static final LatLng boundary1 = new LatLng(30.525369, 114.360351);
    private static final LatLng boundary2 = new LatLng(30.553849, 114.355979);
    private static final LatLng boundary3 = new LatLng(30.538814, 114.358055);
    private static final LatLng boundary4 = new LatLng(30.535009, 114.377147);

    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;

    @Override
    public void onResume() {
        super.onResume();

        aMap = getMap();

        if (aMap != null) {
            mAMapLocationManager = LocationManagerProxy
                    .getInstance(getActivity());

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));
            myLocationStyle.strokeWidth(0);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.getUiSettings().setCompassEnabled(false);

            // 设置定位资源。如果不设置此定位资源则定位按钮不可点击。
            aMap.setLocationSource(this);
            // 设置定位层是否显示。
            aMap.setMyLocationEnabled(true);

            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            addSpotMarkers();// 往地图上添加marker

            deactivate();
        }

        ArrayAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.map_overlay_type,
                android.R.layout.simple_spinner_dropdown_item);

        mSpinnerAdapter.setDropDownViewResource(R.layout.action_bar_spinner_item);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                if (itemPosition == 0) {
                    aMap.clear();

                    addSpotMarkers();

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(marker1).include(marker2).include(marker3)
                            .include(marker4).include(marker5).include(marker6).build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                } else if (itemPosition == 1) {
                    aMap.clear();

                    addDeptMarkers();

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(boundary1).include(boundary2).include(boundary3)
                            .include(boundary4).build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                } else {
                    aMap.clear();

                    addServiceMarkers();

                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(boundary1).include(boundary2).include(boundary3)
                            .include(boundary4).build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                }

                return true;
            }
        });
    }

    private void addSpotMarkers() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(marker1);
        markerOption.title("武大牌坊");
        markerOption.snippet("你是我的眼");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        whuDoorMarker = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(marker2);
        markerOption.title("樱花大道");
        markerOption.snippet("站在这里,你也成为了历史");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.flower));
        sakuraRoadMarker = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(marker3);
        markerOption.title("历史学院人文馆");
        markerOption.snippet("在这里,读懂历史");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        historyDeptMarker = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(marker4);
        markerOption.title("六一亭");
        markerOption.snippet("在这里,见证武大历史");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        sixOnePavilionMarker = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(marker5);
        markerOption.title("宋卿体育馆");
        markerOption.snippet("在百年历史的体育馆中感受历史,强健体魄");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.group));
        songQingSportCenterMarker = aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(marker6);
        markerOption.title("武汉大学新图书馆");
        markerOption.snippet("这里,全是武大历史");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.group));
        newLibraryMarker = aMap.addMarker(markerOption);
    }

    private void addDeptMarkers() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.53917, 114.3674413));
        markerOption.title("历史学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536925, 114.366612));
        markerOption.title("哲学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536692, 114.366573));
        markerOption.title("国学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.539408, 114.371035));
        markerOption.title("外国语言文学学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.539884, 114.367768));
        markerOption.title("新闻与传播学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.535009, 114.377147));
        markerOption.title("艺术学系");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.538832, 114.373453));
        markerOption.title("经济与管理学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.53867, 114.37105));
        markerOption.title("法学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.53807, 114.367729));
        markerOption.title("政治与公共管理学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.538735, 114.360333));
        markerOption.title("WTO学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.539988, 114.364685));
        markerOption.title("数学与统计学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.534155, 114.362271));
        markerOption.title("物理科学与技术学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536458, 114.359842));
        markerOption.title("化学与分子科学学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.533467, 114.360352));
        markerOption.title("生命科学学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.525369, 114.360351));
        markerOption.title("资源与环境科学学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.542068, 114.362616));
        markerOption.title("水利水电学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.542068, 114.362616));
        markerOption.title("电气工程学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.541901, 114.36548));
        markerOption.title("动力与机械学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.541506, 114.364804));
        markerOption.title("城市设计学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.544382, 114.361358));
        markerOption.title("土木建筑工程学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.538814, 114.358055));
        markerOption.title("计算机学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.525889, 114.360324));
        markerOption.title("遥感信息工程学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.537895, 114.358297));
        markerOption.title("电子信息学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.525948, 114.362154));
        markerOption.title("测绘学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.52599, 114.360349));
        markerOption.title("印刷与包装系");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.553849, 114.355979));
        markerOption.title("医学部");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.553216, 114.355563));
        markerOption.title("HOPE护理学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.53285, 114.361434));
        markerOption.title("药学院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.building));
        aMap.addMarker(markerOption);
    }

    private void addServiceMarkers() {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.527013, 114.358529));
        markerOption.title("信息学部二食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.528323, 114.359319));
        markerOption.title("信息学部一食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.530408, 114.360705));
        markerOption.title("星湖园食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.538499, 114.35936));
        markerOption.title("桂圆食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.535712, 114.364792));
        markerOption.title("梅园食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.543274, 114.366179));
        markerOption.title("工学部四食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.541459, 114.370303));
        markerOption.title("湖滨食堂");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.540117, 114.357513));
        markerOption.title("自强超市");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.530593, 114.360274));
        markerOption.title("星湖园自强超市");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.535617,114.365299));
        markerOption.title("后勤服务集团");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536847,114.364074));
        markerOption.title("武汉大学妇女与性别研究中心");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.533438,114.364104));
        markerOption.title("武汉大学医院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.535485,114.366883));
        markerOption.title("武汉大学董事会");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536489,114.362612));
        markerOption.title("武汉大学万林艺术博物馆");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536326,114.366608));
        markerOption.title("武汉大学考试中心");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.537612,114.366521));
        markerOption.title("行政楼");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.5399,114.363256));
        markerOption.title("武汉大学质量发展战略研究院");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.539749,114.362618));
        markerOption.title("武汉大学学生俱乐部");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.54484,114.360858));
        markerOption.title("武汉大学工程检测中心");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.543843,114.36147));
        markerOption.title("武汉大学图书馆工学分馆");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.544154,114.360851));
        markerOption.title("武汉大学教育培训中心");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(30.536293,114.361053));
        markerOption.title("武汉大学教育技术与教室管理中心");
        markerOption.perspective(true);
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.service));
        aMap.addMarker(markerOption);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        int SEARCH_POI_REQUEST_CODE = 1;

        if (requestCode == SEARCH_POI_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            deactivate();

            Bundle b = data.getExtras();

            String id = b.getString("id");
            double latitude = b.getDouble("latitude");
            double longitude = b.getDouble("longitude");
            String title = b.getString("title");
            String snippet = b.getString("snippet");

            PoiItem poiItem = new PoiItem(id, new LatLonPoint(latitude, longitude), title, snippet);
            List<PoiItem> poiItems = new ArrayList<>();
            poiItems.add(poiItem);

            aMap.clear();// 清理之前的图标
            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan();

            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(latitude, longitude), 17, 0, 0)), null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        deactivate();

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        getActivity().invalidateOptionsMenu();

        getActivity().getActionBar().setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
    }

    @Override
    public void onDestroy() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(AMapLocation aLocation) {

        if (mListener != null) {
            mListener.onLocationChanged(aLocation);
        }

        deactivate();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
        }

        // Location API采用GPS和网络混合定位方式。
        mAMapLocationManager.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 5000, 10, this);
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destory();
        }
        mAMapLocationManager = null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.equals(whuDoorMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_WHU_DOOR);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);

        } else if (marker.equals(sakuraRoadMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_SAKURA_ROAD);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);
        } else if (marker.equals(historyDeptMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_HISTORY_DEPT_CENTER);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);
        } else if (marker.equals(songQingSportCenterMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_SQ_SPORT_CENTER);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);
        } else if (marker.equals(sixOnePavilionMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_SIX_ONE_PAVILION);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);
        } else if (marker.equals(newLibraryMarker)) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("type", ConstantUtils.TYPE_NEW_LIBRARY);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in,
                    R.anim.scale_out);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }

        return true;
    }

}
