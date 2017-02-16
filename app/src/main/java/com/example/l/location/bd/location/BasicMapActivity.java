package com.example.l.location.bd.location;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.example.l.location.BaseActivity;
import com.example.l.location.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class BasicMapActivity extends BaseActivity {
	
	private LocationClient locationClient;
	
	private MyListener listener;

	private MapView mMapView;  //地图视图

	private BaiduMap mBaiduMap; //地图的控制类，可以更新地图的状态等

	private LatLng latLng;
	
	private List<LatLng> points = new ArrayList<LatLng>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_layout);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		//第一步：创建定位的执行类LocationClient
		locationClient = new LocationClient(getApplicationContext());

		//第三步设置LocationClient的事件监听
		listener = new MyListener();
		//调用registerLocationListener注册事件监听
		locationClient.registerLocationListener(listener);
	}

	//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.basic_layout);
//
//		mMapView = (MapView) findViewById(R.id.bmapView);
//		mBaiduMap = mMapView.getMap();
//
//		//第一步：创建定位的执行类LocationClient
//		locationClient = new LocationClient(getApplicationContext());
//
//		//第三步设置LocationClient的事件监听
//		listener = new MyListener();
//		//调用registerLocationListener注册事件监听
//		locationClient.registerLocationListener(listener);
//	}
//
	public void btnClickd(View view) {
		switch (view.getId()) {
		case R.id.btn_Loacation:
			//第五步：调用start方法启动定位功能
			locationClient.start();
			break;
		case R.id.btn_MapType:
			//通过BaiduMap.setMapType方法可以设置地图的显示类型
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.btn_MarkPostion:
			//构建Marker所需要显示的icon图标
			BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(
					R.drawable.icon_marka);
			//用来做标记的选项
			OverlayOptions markOptions = new MarkerOptions()
			.position(latLng)  //设置覆盖物所显示位置
			.icon(icon)  //覆盖物的图标
			.zIndex(9)   //覆盖物的z轴坐标
			.draggable(true);  //设置覆盖物可拖动
			mBaiduMap.addOverlay(markOptions);
			mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
				
				@Override
				public void onMarkerDragStart(Marker arg0) {
					Log.e("123", "onMarkerDragStart");
				}
				
				@Override
				public void onMarkerDragEnd(Marker marker) {
					// TODO Auto-generated method stub
					Log.e("123", "onMarkerDragEnd");
					LatLng ll = new LatLng(marker.getPosition().latitude, 
							marker.getPosition().longitude);
					
					points.add(ll);
					
					if(points.size() >= 3) {
						//构建多边形的Options对象
						OverlayOptions polygonOptions = new PolygonOptions()
								.points(points)
								.stroke(new Stroke(5, 0xAA00FF00))  
							    .fillColor(0xAAFFFF00);
						
						mBaiduMap.addOverlay(polygonOptions);
					}
					
				}
				
				@Override
				public void onMarkerDrag(Marker arg0) {
					// TODO Auto-generated method stub
					Log.e("123", "onMarkerDrag");
					
				}
			});
		
			break;

		default:
			break;
		}
	}
	
	//第二步：创建LocationClient的回调接口类
	class MyListener implements BDLocationListener {

		/**
		 * 当百度地图定位到用户位置时，会回调此接口方法
		 * @param location 封装了用户的经纬度信息，以及定位的类型
		 */
		@Override
		public void onReceiveLocation(BDLocation location) {
			//通过location对象可以判断出定位的类型
			int type = location.getLocType(); //返回定位的类型
			if(type == BDLocation.TypeGpsLocation) { //说明GPS定位成功
				showToast("GPS定位成功");
				showLocation(location);
			} else if(type == BDLocation.TypeNetWorkLocation) {  //网络定位成功
				showToast( "网络定位成功");
				showLocation(location);
			} else if(type == BDLocation.TypeOffLineLocation) { //离线定位成功
				showToast("离线定位成功");
				showLocation(location);
			}
		}
		
	}

	/**
	 * 根据百度地图返回给我们的BDLocation对象，获取经纬度，滑动到当前位置
	 * @param location
	 */
	public void showLocation(BDLocation location) {
		Log.e("123", "lat is " + location.getLatitude() 
			+ " lng is " + location.getLongitude());
		latLng = new LatLng(
				location.getLatitude(), location.getLongitude());
		//创建最新地图状态的工厂类Builder
		MapStatus.Builder builder = new MapStatus.Builder();
		//设置地图的中心点为地图定位返回位置，并设置地图缩放级别
		builder.target(latLng).zoom(18.0f);
		//通过动画的方式将地图的中心点设置为地图返回给用户的位置经纬度
		mBaiduMap.animateMapStatus(
				MapStatusUpdateFactory.newMapStatus(builder.build()));
	}
}








