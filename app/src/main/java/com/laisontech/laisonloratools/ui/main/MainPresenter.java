package com.laisontech.laisonloratools.ui.main;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.laisontech.laisonloratools.R;
import com.laisontech.laisonloratools.db.utils.OperateSPUtils;
import com.laisontech.laisonloratools.location.utils.GoogleMarkerUtils;
import com.laisontech.laisonloratools.utils.ViewUtils;
import com.laisontech.mvp.mvp.BasePresenterImpl;

/**
 * Created by SDP
 * on 2019/4/2
 * Des：
 */
public class MainPresenter extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter {
    /**
     * 获取选择的地图的类型
     */
    @Override
    public void loadMapMode() {
        if (mView != null) {
            mView.showLastMapMode(OperateSPUtils.queryLastSaveMapMode());
        }
    }

    /**
     * 获取对控件的显隐
     */
    @Override
    public void showMapModeVisible() {
        if (mView != null) {
            int mapModeVisible = mView.getMapModeVisible();
            if (mapModeVisible == View.VISIBLE) {
                mView.showMapModeUIVisible(View.GONE);
            } else {
                mView.showMapModeUIVisible(View.VISIBLE);
            }
        }
    }

    /**
     * 设置map mode
     */
    @Override
    public void setMapMode(int resId, int mapType) {
        if (mView != null) {
            OperateSPUtils.saveMapMode(mapType);
            mView.getMap().setMapType(mapType);
            mView.showSetMapMode(ViewUtils.getResStr(R.string.UseMode) + " " + ViewUtils.getResStr(resId) + " " + ViewUtils.getResStr(R.string.Map));
        }
    }

    @Override
    public void executeLocation(boolean isFirstEnter, float zoom) {
        if (mView != null) {
            boolean inExecution = false;
            LatLng latLng = mView.getLatLng();
            GoogleMap map = mView.getMap();
            mView.showLocationMarker(GoogleMarkerUtils.drawLocationMarker(map, isFirstEnter, inExecution, latLng, zoom));
        }
    }
}
