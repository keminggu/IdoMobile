package com.ido.reactnative.baidumap;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gukeming on 2015/12/23.
 * Eamil:342802259@qq.com
 */
public class BaiduMapModule extends ReactContextBaseJavaModule {

    public static final String CLASS_NAME = "BaiduMap";

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public BaiduMapModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

}
