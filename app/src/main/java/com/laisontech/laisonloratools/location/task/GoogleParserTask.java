package com.laisontech.laisonloratools.location.task;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.laisontech.laisonloratools.asynctask.BaseTask;
import com.laisontech.laisonloratools.callback.LoadDataFromDBListener;
import com.laisontech.laisonloratools.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SDP
 * on 2019/4/16
 * Des：
 */
public class GoogleParserTask extends BaseTask<String, Void, PolylineOptions> {
    public GoogleParserTask(LoadDataFromDBListener<PolylineOptions> listener) {
        super(listener);
    }

    @Override
    protected PolylineOptions doInBackground(String... strings) {
        if (strings == null || strings.length != 1) return null;
        String jsonPath = strings[0];
        try {
            JSONObject jObject = new JSONObject(jsonPath);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            List<List<HashMap<String, String>>> result = parser.parse(jObject);
            if (result == null || result.size() < 1) return null;
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = CommonUtils.getStringToDouble(point.get("lat"));
                    double lng = CommonUtils.getStringToDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
            }
            return lineOptions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
