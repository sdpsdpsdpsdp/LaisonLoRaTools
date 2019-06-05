package com.laisontech.laisonloratools.location.task;

import com.google.android.gms.maps.model.LatLng;
import com.laisontech.laisonloratools.asynctask.BaseTask;
import com.laisontech.laisonloratools.callback.LoadDataFromDBListener;

import java.util.List;

/**
 * Created by SDP
 * on 2019/4/17
 * Des：
 */
public class PathAlgorithmTask extends BaseTask<Void, Void, List<LatLng>> {
    private List<LatLng> mTotalLatLng;

    public PathAlgorithmTask(List<LatLng> totalLatLng, LoadDataFromDBListener<List<LatLng>> listener) {
        super(listener);
        mTotalLatLng = totalLatLng;
    }

    @Override
    protected List<LatLng> doInBackground(Void... voids) {
        if (mTotalLatLng == null || mTotalLatLng.size() < 2) return null;
        List<LatLng> sortRoadPath = PathAlgorithm.buildAndSortRoadPath(mTotalLatLng);
        if (sortRoadPath == null || sortRoadPath.size() < 2) return null;
        return sortRoadPath;
    }
}
