package com.example.l.location.bd.location;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.l.location.BaseActivity;
import com.example.l.location.R;
import com.example.l.location.bd.location.utils.PoiOverlay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class PoiSearchActivity extends BaseActivity {
	
	//声明POI搜索的核心执行类
	private PoiSearch poiSearch;
	
	//声明并初始化POI检索的事件监听类
	private OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		
		/**
		 * 当百度地图搜索到某条件下的兴趣点时回调此方法
		 * 通过解析遍历PoiResult对象获取所查询出的所有兴趣点
		 */
		@Override
		public void onGetPoiResult(PoiResult result) {
			Log.e("123", "onGetPoiResult");
			
			if(result == null && 
					result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
				return;
			}
			
			List<PoiInfo> pois = result.getAllPoi();
			
			Log.e("123", "pois size is " + pois.size());
			for(PoiInfo info : pois) {
				String name = info.name;
				String phoneNum = info.phoneNum;
				Log.e("123", "name is " + name + " phoneNum is " + phoneNum);
			}
			
			/*
			 * 以下代码将返回的result显示到MapView视图当中
			 */
			mBaiduMap.clear();  //将之前的查询结果清空
			//创建显示POI检索结果的OverLay对象
			PoiOverlay poiOverLay = new MyPoiOverLay(mBaiduMap);
			/*
			 * 通过setOnMarkerClickListener设置Overlay的点击事件
			 * 当点击某一个Marker时，onPoiClick方法会被回调
			 */
			mBaiduMap.setOnMarkerClickListener(poiOverLay);
			//设置Overlay的显示数据
			poiOverLay.setData(result);
			//将此覆盖物添加到MapView当中
			poiOverLay.addToMap();
			//设置缩放级别
			poiOverLay.zoomToSpan();
			
		}
		
		/**
		 * 查询获取兴趣点详细信息时，回调此方法
		 */
		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			Log.e("123", "onGetPoiDetailResult");	
			if(arg0.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(PoiSearchActivity.this, 
						"未搜索到结果", Toast.LENGTH_LONG).show();
			} else {
				String name = arg0.getName();
				String address = arg0.getAddress();
				
				Toast.makeText(PoiSearchActivity.this, 
						"name is " + name + " addr is " + address, 
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private EditText editCity;

	private AutoCompleteTextView searchKeyText;

	private MapView mMapView;

	private BaiduMap mBaiduMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_layout);
		
		initViews();
		
		initPoiSearch();
	}

	private void initViews() {
		mMapView = (MapView) findViewById(R.id.bmapView_POI);
		mBaiduMap = mMapView.getMap();
		
		editCity = (EditText) findViewById(R.id.editCity);
		searchKeyText = (AutoCompleteTextView) findViewById(R.id.searchKey);
	}

	private void initPoiSearch() {
		//第一步：初始化PoiSearch对象
		poiSearch = PoiSearch.newInstance();
		//第二步：设置PoiSearch的事件监听
		poiSearch.setOnGetPoiSearchResultListener(poiListener);
	}
	
	public void btnClicked(View view) {
		String city = editCity.getText().toString();
		String searchKey = searchKeyText.getText().toString();
		//第三步：初始化城市搜索的Optinos选项, 并将city和searchKey填充
		PoiCitySearchOption poiSearchOptions = new PoiCitySearchOption();
		poiSearchOptions.city(city);  //设置城市名称
		poiSearchOptions.keyword(searchKey);  //设置搜索关键字
		
		//第四步：调用PoiSearch.searchInCity()方法启动查询操作
		poiSearch.searchInCity(poiSearchOptions);
	}
	
	class MyPoiOverLay extends PoiOverlay {
		public MyPoiOverLay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		/**
		 * 当显示在MapView上的每一个Marker时，此方法被调用
		 */
		@Override
		public boolean onPoiClick(int index) {
			// TODO 点击某一个Marker时，显示详细信息
			PoiInfo info = getPoiResult().getAllPoi().get(index);
			//poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(info.uid));
			poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(info.uid));
			
			return true;
		}
	}
}








