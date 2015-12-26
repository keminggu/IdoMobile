package com.ido.reactnative.baidumap;

import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.platform.comapi.map.B;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by gukeming on 2015/12/23.
 * Eamil:342802259@qq.com
 */
public class BaiduMapModule extends ReactContextBaseJavaModule {

    public static final String CLASS_NAME = "BaiduMapModule";
    private ReactContext reactContext = null;

    private static GeoCoder search= null;

    private void sendEvent(String eventName, WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public BaiduMapModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    public void initialize() {
        super.initialize();

        // 初始化搜索模块
        search = GeoCoder.newInstance();
        search.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                LatLng location = geoCodeResult.getLocation();
                WritableMap params = Arguments.createMap();
                params.putDouble("latitude", location.latitude);
                params.putDouble("longitude", location.longitude);
                sendEvent("onGeoCode", params);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                WritableMap params = Arguments.createMap();
                params.putString("address", reverseGeoCodeResult.getAddress());
                sendEvent("onReverseGeoCode", params);
            }
        });
    }

    @ReactMethod
    public void reverseGeoCode(double latitude, double longitude) {
        /**获取经纬度*/
        LatLng ll = new LatLng(latitude, longitude);
        // 反Geo搜索
        search.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
    }

    @ReactMethod
    public void geoCode(final String addr) {
        if(addr == null) {
            return;
        }
        search.geocode(new GeoCodeOption().address(addr));
    }


}
