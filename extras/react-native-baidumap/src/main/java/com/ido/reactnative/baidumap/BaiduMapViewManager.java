package com.ido.reactnative.baidumap;

import android.app.Activity;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.facebook.csslayout.CSSNode;
import com.facebook.csslayout.MeasureOutput;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;


/**
 * Created by gukeming on 2015/12/23.
 * Eamil:342802259@qq.com
 */
public class BaiduMapViewManager extends SimpleViewManager<MapView> {

    public static final String TAG = "RCTBaiduMap";
    public static final String REACT_CLASS = "RCTBaiduMap";

    private Activity activity;

    public BaiduMapViewManager(Activity activity) {
        this.activity = activity;
    }


    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public MapView createViewInstance(ThemedReactContext context) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(context.getApplicationContext());
        return new MapView(this.activity);
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

    @ReactProp(name="showZoomControls", defaultBoolean = false)
    public void showZoomControls(MapView mapView, Boolean visible) {
        Log.i(TAG, "showZoomControls:" + visible);
        mapView.showZoomControls(visible);
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
