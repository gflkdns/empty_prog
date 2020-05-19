package com.miqt.time.utils;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.miqt.time.bean.USMInfo;

import java.text.SimpleDateFormat;

public class AppNameValueFormatter extends ValueFormatter {
    Context context;

    public AppNameValueFormatter(Context context) {
        this.context = context;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return super.getAxisLabel(value, axis);
    }

    @Override
    public String getPointLabel(Entry entry) {
        USMInfo info = (USMInfo) entry.getData();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String text =
                "[" + AppUtils.getInstance(context).getAppName(info.getPkgName()) + "][" + dateFormat.format(info.getOpenTime()) +
                        "-" +
                        dateFormat.format(info.getCloseTime()) + "]";
        return text;
    }

    @Override
    public String getFormattedValue(float value) {

        return (int) (value / (1000 * 60)) + "分" + (int) (value / (1000)) % 60 + "秒";
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        USMInfo info = (USMInfo) entry.getData();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String text =
                "[" + AppUtils.getInstance(context).getAppName(info.getPkgName()) + "][" + dateFormat.format(info.getOpenTime()) +
                        "-" +
                        dateFormat.format(info.getCloseTime()) + "]";
        return text;
    }
}
