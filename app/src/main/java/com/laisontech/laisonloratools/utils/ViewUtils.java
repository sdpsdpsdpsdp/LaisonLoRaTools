package com.laisontech.laisonloratools.utils;

import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;

import com.laisontech.laisonloratools.app.LoRaApp;

import java.lang.reflect.Method;

/**
 * Created by SDP
 * on 2019/6/5
 * Des：
 */
public class ViewUtils {
    public static void setActionBarParams(AppCompatActivity activity, @StringRes int resId) {
        if (activity == null) return;
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(false);
            supportActionBar.setTitle(resId);
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setTitle(resId);
            }
        }
    }

    /**
     * 获取本地资源
     */
    public static String getResStr(@StringRes int resId) {
        return LoRaApp.mLocalContext.getResources().getString(resId);
    }

    /**
     * 使用反射将menu的icon显示出来
     */
    public static void showMenuIcon(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
