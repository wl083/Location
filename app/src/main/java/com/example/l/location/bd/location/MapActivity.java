package com.example.l.location.bd.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.l.location.BaseActivity;
import com.example.l.location.R;

/**
 * Created by Administrator on 2017/2/16.
 * 根据中文地址反编译出改地址的经纬度，并进行定位
 */

public class MapActivity extends BaseActivity implements OnGetGeoCoderResultListener {


    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;

    EditText edt1, edt2;
    Button btnCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_geocoder);
        edt1 = (EditText) findViewById(R.id.edt1);
        edt2 = (EditText) findViewById(R.id.edt2);
        btnCommit = (Button) findViewById(R.id.btn);
        btnCommit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mSearch.geocode(new GeoCodeOption().city(edt1.getText().toString()).address(edt2.getText().toString()));
            }
        });

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        //根据地理位置定位，先城市，后地点名
        mSearch.geocode(new GeoCodeOption().city("背景").address("故宫"));
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        showToast(strInfo);

        //根据经纬度定位，并或者缩小，参数可调
        LatLng ll = new LatLng(result.getLocation().latitude,
                result.getLocation().longitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo((float) 15.5));
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

    }
}
