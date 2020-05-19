package com.miqt.time.presenter;

import android.app.Activity;
import android.app.usage.UsageStats;

import com.miqt.time.bean.UsageData;
import com.miqt.time.impl.USMImpl;
import com.miqt.time.utils.PriorityThreadPool;
import com.miqt.time.utils.USMUtils;
import com.miqt.time.view.MainView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPresenter implements IPresenter<MainView> {

    Activity activity;
    MainView mainView;

    public MainPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void start(final MainView view) {
        mainView =view;
        if (USMUtils.getUsageEvents(0, System.currentTimeMillis(), activity.getApplicationContext()) == null) {
            USMUtils.openUSMSetting(activity.getApplicationContext());
        }

        PriorityThreadPool.getInstance().asyn().execute(new Runnable() {
            @Override
            public void run() {
                //现在
                long endTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                //今天 0点0分0秒
                long startTime = calendar.getTimeInMillis();
                LinkedList<UsageData> usageData = new LinkedList<>();
                for (int i = 0; i < 7; i++) {
                    long totalTime = 0;
                    List<UsageStats> usageStats = USMUtils.getUsageStats(startTime, endTime, activity.getApplicationContext());
                    if (usageStats != null && !usageStats.get(i).getPackageName().contains("android")) {
                        for (int j = 0; j < usageStats.size(); j++) {
                            totalTime += usageStats.get(j).getTotalTimeInForeground();
                        }
                    }
                    //倒回一天
                    endTime = startTime;
                    startTime -= TimeUnit.DAYS.toMillis(1);
                    usageData.addFirst(new UsageData(usageStats, startTime, endTime, totalTime));
                }
                view.show7DayUsageData(usageData);
            }
        });
        PriorityThreadPool.getInstance().asyn().execute(new Runnable() {
            @Override
            public void run() {
                //现在
                long endTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                //今天 0点0分0秒
                long startTime = calendar.getTimeInMillis();
                view.showTodayUsageData(USMImpl.getUSMInfo(activity.getApplicationContext(), startTime, endTime));
            }
        });
    }


    public void on7DayValueSelected(UsageData usageData) {
        mainView.showDayUsageData(
                SimpleDateFormat.getTimeInstance(DateFormat.FULL)
                        .format(usageData.getEndTime())
                , usageData.getUsageStats());
    }
}
