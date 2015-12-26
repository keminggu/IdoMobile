package com.ido.reactnative.baidumap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.uimanager.RootViewUtil;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;


/**
 * Created by gukeming on 2015/12/24.
 * Eamil:342802259@qq.com
 */
public class ReactMapView implements LifecycleEventListener {
    public static final String TAG = "ReactMapView";

    private Activity  activity = null;
    private ReactContext context = null;

    private MapView mapView = null;
    private BaiduMap baiduMap = null;

    private boolean isFirstLoc = true;
    private LocationClient locClient;

    private ImageView imageView = null;

    private EventDispatcher eventDispatcher = null;


    public ReactMapView(Activity activity,  ReactContext context) {
        this.activity = activity;
        this.context = context;

        eventDispatcher = this.context.getNativeModule(UIManagerModule.class).getEventDispatcher();

        this.mapView = new MapView(activity);
        this.baiduMap = this.mapView.getMap();
        
        this.baiduMap.setMyLocationEnabled(true);

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        this.baiduMap.setMapStatus(msu);
        
        // 定位初始化
        locClient = new LocationClient(this.mapView.getContext());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        // 打开gps
        option.setCoorType("bd09ll");   // 设置坐标类型
        option.setScanSpan(1000);
        locClient.setLocOption(option);
        locClient.start();

        locClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || ReactMapView.this.mapView == null) {
                    return;
                }

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                ReactMapView.this.baiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    ReactMapView.this.baiduMap.animateMapStatus(u);

//                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                        .location(ll));
                }
            }
        });

        this.baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                int width = ReactMapView.this.mapView.getWidth() / 2;
                int height = ReactMapView.this.mapView.getHeight() / 2;

                MapViewLayoutParams p = new MapViewLayoutParams.Builder().align(
                        MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL)
                        .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                        .point(new Point(width, height))
                        .build();


                imageView = new ImageView(ReactMapView.this.activity);
                imageView.setImageResource(R.drawable.icon_gcoding);
                ReactMapView.this.mapView.addView(imageView, p);

                eventDispatcher.dispatchEvent(
                        new BaiduMapEvent(ReactMapView.this.mapView.getId(), SystemClock.uptimeMillis(), BaiduMapEvent.ON_LOADED)
                );
            }
        });


        this.baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                LatLng mCenterLatLng = mapStatus.target;
                /**获取经纬度*/
                double lat = mCenterLatLng.latitude;
                double lng = mCenterLatLng.longitude;

                WritableMap event = Arguments.createMap();
                event.putDouble("latitude", lat);
                event.putDouble("longitude", lng);

                eventDispatcher.dispatchEvent(
                        new BaiduMapEvent(ReactMapView.this.mapView.getId(), SystemClock.uptimeMillis(), BaiduMapEvent.ON_CHANGE_START, event)
                );
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
//                eventDispatcher.dispatchEvent(
//                        new BaiduMapEvent(ReactMapView.this.mapView.getId(), SystemClock.uptimeMillis(), BaiduMapEvent.ON_CHANGE)
//                );
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng mCenterLatLng = mapStatus.target;
                /**获取经纬度*/
                double lat = mCenterLatLng.latitude;
                double lng = mCenterLatLng.longitude;

                WritableMap event = Arguments.createMap();
                event.putDouble("latitude", lat);
                event.putDouble("longitude", lng);
                eventDispatcher.dispatchEvent(
                        new BaiduMapEvent(ReactMapView.this.mapView.getId(), SystemClock.uptimeMillis(), BaiduMapEvent.ON_CHANGE_END, event)
                );
                //LatLng ptCenter = new LatLng(lat, lng);
                // 反Geo搜索
//                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                            .location(mCenterLatLng));
            }
        });
    }

    @Override
    public void onHostResume() {
        this.mapView.onResume();
    }

    @Override
    public void onHostPause() {
        this.mapView.onPause();
    }

    @Override
    public void onHostDestroy() {
        locClient.stop();
        locClient = null;
        // 关闭定位图层
        this.baiduMap.setMyLocationEnabled(false);
        this.mapView.onDestroy();
        this.mapView = null;
    }

    public MapView getMapView() {
        return this.mapView;
    }
}