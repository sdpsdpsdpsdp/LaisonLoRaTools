package com.laisontech.laisonloratools.ui.main;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.laisontech.laisonloratools.R;
import com.laisontech.laisonloratools.ui.base.BaseMapUsbMvpActivity;
import com.laisontech.laisonloratools.utils.ViewUtils;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新项目：
 * 用于LoRa无线工具的演示Demo
 * 功能：1、支持金钰铭 利尔达
 * 2、支持添加点到地图上 移除点等功能
 * 3、支持对选择的点进行自动抄表数据演示等
 * 4、根据插入的无线设备不同进行显示不同的演示项目
 */
public class MainActivity extends BaseMapUsbMvpActivity<MainContract.View, MainPresenter>
        implements MainContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public GoogleMap getMap() {
        return null;
    }

    @Override
    public void setMap(GoogleMap map) {

    }

    @Override
    public void showLastMapMode(int mapMode) {

    }

    @Override
    public int getMapModeVisible() {
        return 0;
    }

    @Override
    public void showMapModeUIVisible(int gone) {

    }

    @Override
    public void showSetMapMode(String msg) {

    }

    @Override
    public LatLng getLatLng() {
        return null;
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(int resId) {

    }

    @Override
    public void showLocationMarker(Marker marker) {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ViewUtils.showMenuIcon(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick(R.id.fab)
    public void onClick() {
    }
}
