package com.laisontech.laisonloratools.ui.main;

import android.support.annotation.StringRes;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.laisontech.mvp.mvp.BasePresenter;
import com.laisontech.mvp.mvp.BaseView;

/**
 * Created by SDP
 * on 2019/4/2
 * Des：
 */
public class MainContract {
    public interface View extends BaseView {
        GoogleMap getMap();

        void setMap(GoogleMap map);

        void showLastMapMode(int mapMode);//显示上次的地图选择类型

        int getMapModeVisible();//获取控件的显隐状态

        void showMapModeUIVisible(int gone);//显示控件

        void showSetMapMode(String msg);//设置地图类型成功的提示

        LatLng getLatLng();

        void showDialog();

        void hideDialog();

        void showError(@StringRes int resId);

        void showLocationMarker(Marker marker);
    }

    public interface Presenter extends BasePresenter<View> {
        void loadMapMode();

        void showMapModeVisible();

        void setMapMode(@StringRes int resId, int mapType);

        void executeLocation(boolean isFirstEnter, float zoom);
    }
}
