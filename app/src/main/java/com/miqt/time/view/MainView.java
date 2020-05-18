package com.miqt.time.view;

import com.miqt.time.bean.USMInfo;

import java.util.List;

public interface MainView extends IView {
    public void show7DayUsageData(long[] times);

    public void showTodayUsageData(List<USMInfo> usmInfos);
}
