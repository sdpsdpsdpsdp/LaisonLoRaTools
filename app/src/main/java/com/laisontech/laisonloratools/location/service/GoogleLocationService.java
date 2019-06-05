package com.laisontech.laisonloratools.location.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.laisontech.laisonloratools.location.utils.LocationUtil;


/**
 * ..................................................................
 * .         The Buddha said: I guarantee you have no bug!          .
 * .                                                                .
 * .                            _ooOoo_                             .
 * .                           o8888888o                            .
 * .                           88" . "88                            .
 * .                           (| -_- |)                            .
 * .                            O\ = /O                             .
 * .                        ____/`---'\____                         .
 * .                      .   ' \\| |// `.                          .
 * .                       / \\||| : |||// \                        .
 * .                     / _||||| -:- |||||- \                      .
 * .                       | | \\\ - /// | |                        .
 * .                     | \_| ''\---/'' | |                        .
 * .                      \ .-\__ `-` ___/-. /                      .
 * .                   ___`. .' /--.--\ `. . __                     .
 * .                ."" '< `.___\_<|>_/___.' >'"".                  .
 * .               | | : `- \`.;`\ _ /`;.`/ - ` : | |               .
 * .                 \ \ `-. \_ __\ /__ _/ .-` / /                  .
 * .         ======`-.____`-.___\_____/___.-`____.-'======          .
 * .                            `=---='                             .
 * ..................................................................
 * Created by SDP on 2018/8/2.
 */
public class GoogleLocationService {
    private long refreshTime = 5000L;
    private float meterPosition = 0.0f;
    private ILocationListener mLocationListener;
    private LocationListener listener;
    private Context mContext;
    private LocationManager mLocationManager;
    private String mProvider;

    public GoogleLocationService(Context context) {
        mContext = context;
        listener = new MyLocationListener();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mProvider = LocationUtil.getBestProvider(mLocationManager);
    }

    /**
     * 自定义接口
     */
    public interface ILocationListener {
        void onSuccessLocation(Location location);
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public GoogleLocationService setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
        return this;
    }

    public float getMeterPosition() {
        return meterPosition;
    }

    public GoogleLocationService setMeterPosition(float meterPosition) {
        this.meterPosition = meterPosition;
        return this;
    }


    /**
     * GPS获取定位方式
     */
    public Location getGPSLocation() {
        if (mLocationManager == null) return null;
        Location location = null;
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//是否支持GPS定位
            //获取最后的GPS定位信息，如果是第一次打开，一般会拿不到定位信息，一般可以请求监听，在有效的时间范围可以获取定位信息
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * network获取定位方式
     */
    public Location getNetWorkLocation() {
        if (mLocationManager == null) return null;
        Location location = null;
        //高版本的权限检查
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//是否支持Network定位
            //获取最后的network定位信息
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }
    //获取定位
    public Location getGoogleLocation() {
        Location gpsLocation = getGPSLocation();
        if (gpsLocation != null) return gpsLocation;
        Location netWorkLocation = getNetWorkLocation();
        if (netWorkLocation != null) return netWorkLocation;
        return null;
    }
    /**
     * 注册定位监听事件
     */
    public void registerGoogleLocationListener(ILocationListener locationListener) {
        if (mLocationManager == null) return;
        if (locationListener != null) {
            mLocationListener = locationListener;
        }
        if (listener == null) {
            listener = new MyLocationListener();
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(mProvider, getRefreshTime(), getMeterPosition(), listener);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {//定位改变监听
            if (mLocationListener != null) {
                mLocationListener.onSuccessLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {//定位状态监听

        }

        @Override
        public void onProviderEnabled(String provider) {//定位状态可用监听

        }

        @Override
        public void onProviderDisabled(String provider) {//定位状态不可用监听

        }
    }

    /**
     * 取消定位监听
     */
    public void unRegisterGoogleLocationListener() {
        if (listener != null) {
            if (mLocationManager == null) return;
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //移除定位监听
            mLocationManager.removeUpdates(listener);
        }
    }
}
