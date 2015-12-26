package com.ido.reactnative.baidumap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.facebook.csslayout.CSSNode;
import com.facebook.csslayout.MeasureOutput;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.image.ImageLoadEvent;
import com.facebook.react.views.image.ReactImageView;

import java.util.Map;

import javax.annotation.Nullable;


/**
 * Created by gukeming on 2015/12/23.
 * Eamil:342802259@qq.com
 */
public class BaiduMapViewManager extends SimpleViewManager<MapView> {

    public static final String TAG = "RCTBaiduMap";
    public static final String REACT_CLASS = "RCTBaiduMap";

    private Activity activity;
    private ReactMapView reactMapView = null;

    public BaiduMapViewManager(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        ReactMapView reactMapView = new ReactMapView(this.activity, context);
        context.addLifecycleEventListener(reactMapView);
        return reactMapView.getMapView();
    }

    /**
     * 地图模式
     *
     * @param mapView
     * @param type
     *  1. 普通
     *  2. 卫星
     */
    @ReactProp(name="mode", defaultInt = 1)
    public void setMode(MapView mapView, int type) {
        Log.i(TAG, "mode:" + type);
        mapView.getMap().setMapType(type);
    }

    /**
     * 是否显示放大缩小操作杆
     * @param mapView
     * @param visible
     */
    @ReactProp(name="showZoomControls", defaultBoolean = false)
    public void showZoomControls(MapView mapView, Boolean visible) {
        Log.i(TAG, "showZoomControls:" + visible);
        mapView.showZoomControls(visible);
    }

    /**
     *放缩大小
     * @param mapView
     * @param zoom
     */
//    @ReactProp(name="zoomTo", defaultFloat = 14.0f)
//    public void setZoomTo(MapView mapView, float zoom) {
//        Log.i(TAG, "SetZoomTo:" + zoom);
//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(zoom);
//        mapView.getMap().setMapStatus(msu);
//    }

    /**
     * 是否开启定位
     * @param mapView
     * @param visible
     */
    @ReactProp(name="locationEnable", defaultBoolean = false)
    public void setLocationEnable(MapView mapView, Boolean visible) {
        Log.i(TAG, "locationEnable:" + visible);
        // 开启定位图层

        //mapView.getMap().setMyLocationEnabled(visible);

    }

    @Override
    public @Nullable Map getExportedCustomDirectEventTypeConstants() {
        return  MapBuilder.of(
                BaiduMapEvent.eventNameForType(BaiduMapEvent.ON_LOADED),
                MapBuilder.of("registrationName", "onLoaded"),
                BaiduMapEvent.eventNameForType(BaiduMapEvent.ON_CHANGE_START),
                MapBuilder.of("registrationName", "onChangeStart"),
                BaiduMapEvent.eventNameForType(BaiduMapEvent.ON_CHANGE),
                MapBuilder.of("registrationName", "onChange"),
                BaiduMapEvent.eventNameForType(BaiduMapEvent.ON_CHANGE_END),
                MapBuilder.of("registrationName", "onChangeEnd")
        );
    }

    private final class BaiduMapShadowNode extends LayoutShadowNode
            implements CSSNode.MeasureFunction {

        public BaiduMapShadowNode() {
            setMeasureFunction(this);
        }

        @Override
        public void measure(CSSNode node, float width, MeasureOutput measureOutput) {
            measureOutput.width = width;
        }
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new BaiduMapShadowNode();
    }

}
