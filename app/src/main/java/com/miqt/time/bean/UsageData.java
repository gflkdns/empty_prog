package com.miqt.time.bean;

import android.app.usage.UsageStats;

import java.util.List;

public class UsageData {
    List<UsageStats> usageStats;
    long startTime;
    long endTime;
    long totaltime;

    public UsageData(List<UsageStats> usageStats, long startTime, long endTime) {
        this.usageStats = usageStats;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public UsageData(List<UsageStats> usageStats, long startTime, long endTime, long totaltime) {
        this.usageStats = usageStats;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totaltime = totaltime;
    }

    public UsageData() {
     }

    public List<UsageStats> getUsageStats() {
        return usageStats;
    }

    public void setUsageStats(List<UsageStats> usageStats) {
        this.usageStats = usageStats;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(long totaltime) {
        this.totaltime = totaltime;
    }
}
