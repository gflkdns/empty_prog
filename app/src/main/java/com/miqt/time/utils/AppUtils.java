package com.miqt.time.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtils {
    private static volatile AppUtils instance = null;
    Context context;

    private AppUtils(Context context) {
        this.context = context;
    }

    public static AppUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (AppUtils.class) {
                if (instance == null) {
                    instance = new AppUtils(context);
                }
            }
        }
        return instance;
    }

    public String getAppName(String pkg) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(pkg, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return (String) applicationInfo.loadLabel(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
