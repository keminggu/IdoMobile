package com.ido.reactnative.baidumap;

import android.support.annotation.IntDef;

import com.baidu.mapapi.map.MapView;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import android.support.annotation.IntDef;

import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.image.ImageLoadEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.Nullable;

/**
 * Created by gukeming on 2015/12/24.
 * Eamil:342802259@qq.com
 */

public class BaiduMapEvent extends Event<BaiduMapEvent> {
    @IntDef({ON_ERROR, ON_LOADED, ON_CHANGE_START, ON_CHANGE, ON_CHANGE_END})
    @Retention(RetentionPolicy.SOURCE)
    @interface BaiduMapEventType {}

    // Currently ON_ERROR and ON_PROGRESS are not implemented, these can be added
    // easily once support exists in fresco.
    public static final int ON_ERROR = 1;
    public static final int ON_LOADED = 2;
    public static final int ON_CHANGE_START = 3;
    public static final int ON_CHANGE = 4;
    public static final int ON_CHANGE_END = 5;

    private final int mEventType;
    private @Nullable WritableMap mEvent;

    public BaiduMapEvent(int viewId, long timestampMs, @BaiduMapEventType int eventType) {
        super(viewId, timestampMs);
        mEventType = eventType;
    }

    public BaiduMapEvent(int viewId, long timestampMs, @BaiduMapEventType int eventType, @Nullable WritableMap event) {
        super(viewId, timestampMs);
        mEventType = eventType;
        mEvent = event;
    }

    public static String eventNameForType(@BaiduMapEventType int eventType) {
        switch(eventType) {
            case ON_ERROR:
                return "topError";
            case ON_LOADED:
                return "topLoaded";
            case ON_CHANGE_START:
                return "topChangeStart";
            case ON_CHANGE:
                return "topChange";
            case ON_CHANGE_END:
                return "topChangeEnd";
            default:
                throw new IllegalStateException("Invalid event: " + Integer.toString(eventType));
        }
    }

    @Override
    public String getEventName() {
        return BaiduMapEvent.eventNameForType(mEventType);
    }

    @Override
    public short getCoalescingKey() {
        // Intentionally casting mEventType because it is guaranteed to be small
        // enough to fit into short.
        return (short) mEventType;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), mEvent);
    }
}
