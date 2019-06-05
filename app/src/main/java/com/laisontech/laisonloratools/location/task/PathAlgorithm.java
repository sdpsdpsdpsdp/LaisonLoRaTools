package com.laisontech.laisonloratools.location.task;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
 * Created by SDP on 2018/8/14.
 */
public class PathAlgorithm {
    //获取排序的10个点，最后一个座位终点
    private static final int maxPath = 14;
    //最左
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    private static final int BOTTOM = 3;

    public static String buildRoadPathStr(List<LatLng> sortList) {
        if (sortList == null || sortList.size() < 1) return "";
        //3、构建数据
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortList.size(); i++) {
            LatLng latLng = sortList.get(i);
            if (i != sortList.size() - 1) {
                sb.append(latLng.latitude).append(",").append(latLng.longitude)
                        .append("|");
            } else {
                sb.append(latLng.latitude).append(",").append(latLng.longitude);
            }
        }
        return sb.toString();
    }

    /**
     * @param allPaths 所有的路径的点 已经按照定位点拍好序的集合
     */
    public static List<LatLng> buildAndSortRoadPath(List<LatLng> allPaths) {
        if (allPaths == null || allPaths.size() < 1) return null;
        List<LatLng> needRoadPaths;
        //3、小于或者等于10个，可将数据全部放入路径规划中。
        if (allPaths.size() <= maxPath) {
            needRoadPaths = allPaths;
            //4、大于10个的时候，要根据算法，抽取一部分数据
        } else {
            needRoadPaths = buildPath(allPaths);
        }
        return sortPath(needRoadPaths);
    }

    //所得的值必须大于10个
    private static List<LatLng> buildPath(List<LatLng> allPaths) {
        if (allPaths == null || allPaths.size() <= maxPath) return null;
        List<LatLng> newLatLng = new ArrayList<>();

        LatLng leftLatLng = buildOrientation(LEFT, allPaths);
        if (leftLatLng != null) {
            newLatLng.add(leftLatLng);
            allPaths.remove(leftLatLng);
        }

        LatLng rightLatLng = buildOrientation(RIGHT, allPaths);
        if (rightLatLng != null) {
            newLatLng.add(rightLatLng);
            allPaths.remove(rightLatLng);
        }

        LatLng topLatLng = buildOrientation(TOP, allPaths);
        if (topLatLng != null) {
            newLatLng.add(topLatLng);
            allPaths.remove(topLatLng);
        }

        LatLng bottomLatLng = buildOrientation(BOTTOM, allPaths);
        if (bottomLatLng != null) {
            newLatLng.add(bottomLatLng);
            allPaths.remove(bottomLatLng);
        }

        List<LatLng> latLngList = buildResiduePath(newLatLng.size(), allPaths);
        if (latLngList != null) {
            newLatLng.addAll(latLngList);
        }
        return newLatLng;
    }

    //获取随机的6个点
    private static List<LatLng> buildResiduePath(int alreadyAndSize, List<LatLng> allPaths) {
        if (allPaths == null) return null;
        int residueCount = maxPath - alreadyAndSize;//剩余10个减去已经添加的数量==》集合中还可以添加几个经纬度坐标
        int size = allPaths.size();
        if (size <= residueCount) return allPaths;//如果获取剩余的尺寸小于6个，直接return出去
        Random random = new Random();
        //获取随机的6个下标
        List<Integer> integerList = new ArrayList<>();
        while (true) {
            if (integerList.size() >= residueCount) break;//只添加6个随机index
            int randomIndex = random.nextInt(size);//获取随机的index
            if (integerList.contains(randomIndex)) continue;//如果包含了这个index，则跳过
            integerList.add(randomIndex);//添加这个index
        }
        List<LatLng> randomList = new ArrayList<>();
        for (int i = 0; i < integerList.size(); i++) {
            int index = integerList.get(i);
            randomList.add(allPaths.get(index));
        }
        return randomList;
    }

    //构建8个坐标，涵盖了上下左右中，等表计
    private static LatLng buildOrientation(int type, List<LatLng> allPaths) {
        if (allPaths == null || allPaths.size() <= maxPath) return null;
        Collections.sort(allPaths, (latLng1, latLng2) -> {
            double v1, v2;
            switch (type) {
                case LEFT:
                    v1 = latLng1.latitude;
                    v2 = latLng2.latitude;
                    break;
                case RIGHT:
                    v1 = latLng2.latitude;
                    v2 = latLng1.latitude;
                    break;
                case TOP:
                    v1 = latLng1.longitude;
                    v2 = latLng2.longitude;
                    break;
                case BOTTOM:
                    v1 = latLng2.longitude;
                    v2 = latLng1.longitude;
                    break;
                default:
                    v1 = latLng1.latitude;
                    v2 = latLng2.latitude;
                    break;
            }
            if (v1 - v2 > 0) return 1;
            else if (v1 - v2 < 0) return -1;
            else return 0;
        });
        return allPaths.get(0);
    }

    //获取基本的需要构建的路径，规则：大致通过9个点，与当前定位，可以将整个路径覆盖进去。
    //规则:与当前位置最远的一个保留。中间一个，最左一个，
    public static List<LatLng> sortPath(Location location, List<LatLng> latLngList) {
        if (location == null || latLngList == null || latLngList.size() < 1) return null;
        LatLng latLngFrom = new LatLng(location.getLatitude(), location.getLongitude());
        Collections.sort(latLngList, (marker1, marker2) -> {
            double v1 = SphericalUtil.computeDistanceBetween(latLngFrom, marker1);
            double v2 = SphericalUtil.computeDistanceBetween(latLngFrom, marker2);
            if (v1 - v2 > 0) return 1;
            else if (v1 - v2 < 0) return -1;
            else return 0;
        });
        return latLngList;
    }

    //获取基本的需要构建的路径，规则：大致通过9个点，与当前定位，可以将整个路径覆盖进去。
    //规则:与当前位置最远的一个保留。中间一个，最左一个，
    public static List<LatLng> sortPath(List<LatLng> latLngList) {
        if (latLngList == null || latLngList.size() < 1) return null;
        Collections.sort(latLngList, (latLng1, latLng2) -> {
            double v1 = latLng1.latitude;
            double v2 = latLng2.latitude;
            if (v1 - v2 > 0) return -1;
            else if (v1 - v2 < 0) return 1;
            else return 0;
        });
        return latLngList;
    }
}
