package com.laisontech.laisonloratools.ui.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.laisontech.laisonloratools.utils.ViewUtils;
import com.laisontech.serialportlibrary.serialport.config.SerialPortConfig;
import com.laisontech.serialportlibrary.ui.BaseUsbActivity;
import com.laisontech.serialportlibrary.usbmanager.UsbSerialManager;

import java.io.Serializable;

/**
 * Created by SDP
 * on 2019/3/11
 * Des：common usb
 */
public abstract class BaseCommonUsbActivity extends BaseUsbActivity {
    private boolean justLoadOnceData = false;

    @Override
    protected void onResume() {
        super.onResume();
        UsbSerialManager.getManager().setLaisonFrameType(SerialPortConfig.PARER_TYPE_DEFAULT);
        if (!justLoadOnceData) {
            justLoadOnceData = true;
            justLoadOnce();
        }
    }

    //传入Bundle的方式打开activity
    protected void openActivity(Bundle bundle, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String keyName, String valueName, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(keyName, valueName);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String parcelableName, Parcelable parcelable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(parcelableName, parcelable);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String serializableName, Serializable serializable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(serializableName, serializable);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    //startActivityForResult
    protected void openActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    //startActivityForResult 传入Bundle
    protected void openActivityForResult(Bundle bundle, Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    private void closeInputWindow() {
        View view = this.getWindow().peekDecorView();
        if (view != null) {
            @SuppressLint("WrongConstant") InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService("input_method");

            assert inputMethodManager != null;

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    protected String getResStr(@StringRes int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("config", "onConfigurationChanged: " + newConfig);
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * onResume的时候只加载一次数据
     */
    protected void justLoadOnce() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setActionBarParams(@StringRes int resId) {
        ViewUtils.setActionBarParams(this, resId);
    }
}
