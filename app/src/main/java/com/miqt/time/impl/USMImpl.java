package com.miqt.time.impl;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.miqt.time.bean.USMInfo;
import com.miqt.time.utils.ClazzUtils;
import com.miqt.time.utils.USMUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class USMImpl {
    public static List<USMInfo> getUSMInfo(Context context, long start, long end) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return null;
            }
            if (end - start <= 0) {
                return null;
            }

            PackageManager packageManager = context.getPackageManager();
            Object usageStats = USMUtils.getUsageEvents(start, end, context);
            if (usageStats != null) {
                List<USMInfo> infos = new ArrayList<>();
                USMInfo openEvent = null;
                Object lastEvent = null;
                while (true) {

                    boolean b = (boolean) ClazzUtils.invokeObjectMethod(usageStats, "hasNextEvent");
                    if (!b) {
                        break;
                    }
                    UsageEvents.Event event = (UsageEvents.Event) ClazzUtils.newInstance("android.app.usage.UsageEvents$Event");


                    ClazzUtils.invokeObjectMethod(usageStats, "getNextEvent", new String[]{"android.app.usage.UsageEvents$Event"}
                            , new Object[]{event});


                    if (getEventType(event) != 1 && getEventType(event) != 2) {
                        continue;
                    }

                    if (getPackageName(event).equals(context.getPackageName())||getPackageName(event).contains("miui")) {
                        continue;
                    }

                    if (!hasLaunchIntentForPackage(packageManager, getPackageName(event))) {
                        continue;
                    }
                    if (openEvent == null) {

                        if (getEventType(event) == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                            openEvent = openUsm(context, packageManager, event);
                        }
                    } else {
                        if (!openEvent.getPkgName().equals(getPackageName(event))) {
                            openEvent.setCloseTime(getTimeStamp(lastEvent));

                            //大于3秒的才算做oc,一闪而过的不算
                            if (openEvent.getCloseTime() - openEvent.getOpenTime() >= 3000) {
                                infos.add(openEvent);
                            }

                            if (getEventType(event) == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                                openEvent = openUsm(context, packageManager, event);
                            }
                        }
                    }
                    lastEvent = event;
                }
                return infos;
            }
        } catch (Throwable e) {
        }
        return null;
    }


    private static HashSet<String> catchPackage = new HashSet<>();

    /**
     * getLaunchIntentForPackage 这个方法某些设备比较耗时 引起波动, 在这里缓存一下
     *
     * @param manager
     * @param packageName
     * @return
     */
    public static boolean hasLaunchIntentForPackage(PackageManager manager, String packageName) {
        try {
            if (manager == null || packageName == null) {
                return false;
            }
            if (catchPackage.contains(packageName)) {
                return true;
            }
            if (manager.getLaunchIntentForPackage(packageName) != null) {
                catchPackage.add(packageName);
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }

    public static String getPackageName(Object object) {
        return (String) ClazzUtils.invokeObjectMethod(object, "getPackageName");
    }

    public static long getTimeStamp(Object object) {
        return (long) ClazzUtils.invokeObjectMethod(object, "getTimeStamp");
    }

    public static int getEventType(Object object) {
        return (int) ClazzUtils.invokeObjectMethod(object, "getEventType");
    }

    @SuppressLint("NewApi")
    private static USMInfo openUsm(Context context, PackageManager packageManager, Object event) {
        try {
            USMInfo openEvent = new USMInfo(getTimeStamp(event), getPackageName(event));
            openEvent.setCollectionType("5");
            openEvent.setSwitchType("1");
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(event), 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            openEvent.setAppName((String) applicationInfo.loadLabel(packageManager));
            openEvent.setVersionCode(packageInfo.versionName + "|" + packageInfo.versionCode);
            return openEvent;
        } catch (Throwable e) {
        }
        return null;
    }
}
