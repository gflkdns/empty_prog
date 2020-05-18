package com.miqt.time.utils;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;

public class TimeValueFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            if (value >= 7) {
                return "今天";
            } else if (value >= 6) {
                return "昨天";
            } else {
                //当前时间-n天
                long time = (long) (System.currentTimeMillis() - ((7 - value) * 24 * 60 * 60 * 1000));
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
                return dateFormat.format(time);
            }
        } else {
            int h = (int) (value / (60 * 60 * 1000));
            return h + "小时";
        }
    }

    @Override
    public String getPointLabel(Entry entry) {
        int h = (int) (entry.getY() / (60 * 60 * 1000));
        int m = (int) (entry.getY() / (60 * 1000)) / 60;

        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h).append("h");
        }
        builder.append(m).append("m");
        return builder.toString();
    }
}
