package com.laisontech.laisonloratools.ui.base;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.laisontech.laisonloratools.R;
import com.laisontech.laisonloratools.constants.Constants;
import com.laisontech.laisonloratools.location.service.DiffLocationService;
import com.laisontech.laisonloratools.location.event.LocationEvent;
import com.laisontech.mvp.mvp.BasePresenterImpl;
import com.laisontech.mvp.mvp.BaseView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;

/**
 * Created by SDP on 2018/4/25.
 * 基类的使用MVP获取定位和读取usb的Activity
 */

public abstract class BaseMapUsbMvpActivity<V extends BaseView, P extends BasePresenterImpl<V>> extends BaseCommonUsbActivity implements BaseView {
    protected P mPresenter;
    private boolean isNeedShowLocationDialog = false;
    private boolean isLocationDialogShow = false;
    //上次的定位
    private volatile LatLng mLastLatLng;

    @Override
    protected void setDependPresenter() {
        mPresenter = getInstance(this, 1);
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
        setsNeedCloseDialog(true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected void registerLocation() {
        registerLocation(true);
    }

    protected void registerLocation(boolean showLocationDialog) {
        isNeedShowLocationDialog = showLocationDialog;
        if (isNeedShowLocationDialog) {
            showWaitingDialog(getResStr(R.string.Loading), true);
            isLocationDialogShow = true;
        }
        DiffLocationService.startLocationService(this);
    }


    protected void unregisterLocation() {
        DiffLocationService.finishedLocationService(this);
    }

    //接收到定位信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveLocation(LocationEvent locationEvent) {
        if (locationEvent == null) return;
        if (isNeedShowLocationDialog) {
            if (isLocationDialogShow) {
                isLocationDialogShow = false;
                hideWaitingDialog();
            }
        }
        LatLng currentLatLng = buildLatLng(locationEvent.getLocation());
        onResumeLocation(currentLatLng);
        checkLocationChange(currentLatLng);
    }

    protected void onResumeLocation(LatLng latLng) {

    }

    protected void showLocationChange(boolean hasChange) {
    }

    //判断是否定位发生了改变
    private void checkLocationChange(LatLng currentLatLng) {
        if (currentLatLng == null) return;
        if (mLastLatLng == null) {
            mLastLatLng = currentLatLng;
        }
        double changeMeters = SphericalUtil.computeDistanceBetween(currentLatLng, mLastLatLng);
        if (changeMeters > Constants.LOCATION_CHANGE_MAX_DISTANCE) {
            showLocationChange(true);
            mLastLatLng = null;
        } else {
            showLocationChange(false);
        }
    }


    private LatLng buildLatLng(Location location) {
        if (location == null) return null;
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * 执行扫描后的回调
     *
     * @param result 扫描结果
     */
    protected void onScanCodeResult(String result) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
