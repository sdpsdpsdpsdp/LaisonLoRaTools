package com.laisontech.laisonloratools.utils;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by SDP
 * on 2019/6/5
 * Des：
 */
public class CommonUtils {
    /*
  将String转换成double
   */
    public static double getStringToDouble(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        if (value.contains(",")) {
            value = value.replace(",", ".");
        }
        boolean isMatches = Pattern.matches("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$", value);
        if (isMatches) {
            return Double.parseDouble(value);
        } else {
            return 0;
        }
    }

    //将double转为为几位小数
    public static String getPointDouble(double val, int decimalPlace) {
        if (decimalPlace < 1) return String.valueOf(val);
        StringBuilder sb = new StringBuilder();
        sb.append("0.");
        for (int i = 0; i < decimalPlace; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);

    }
}
