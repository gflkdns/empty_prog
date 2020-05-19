package com.miqt.time.view;

import android.app.usage.UsageStats;

import com.miqt.time.bean.USMInfo;
import com.miqt.time.bean.UsageData;

import java.util.LinkedList;
import java.util.List;

public interface MainView extends IView {
    public void show7DayUsageData( LinkedList<UsageData> usageData);
    public void showDayUsageData(String  time, List<UsageStats> usageStats);

    public void showTodayUsageData(List<USMInfo> usmInfos);
}
