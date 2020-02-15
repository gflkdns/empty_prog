package com.miqt.time.activity;

import android.app.usage.UsageStats;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqt.time.R;
import com.miqt.time.bean.USMInfo;
import com.miqt.time.impl.USMImpl;
import com.miqt.time.utils.AppUtils;
import com.miqt.time.utils.USMUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private long startTime;
    private long endTime;

    private Button btnNext;
    private Button btnPre;
    private TextView tv_oc_text;
    private TextView tv_curr_data;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (USMUtils.getUsageEvents(0, System.currentTimeMillis(), this.getApplicationContext()) == null) {
           // USMUtils.openUSMSetting(this.getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            currentDay = dateFormat.format(System.currentTimeMillis());

            startTime = dateFormat.parse(currentDay).getTime();
            endTime = startTime + TimeUnit.DAYS.toMillis(1);

            btnNext = findViewById(R.id.btn_next_day);
            btnPre = findViewById(R.id.btn_pre_day);
            tv_oc_text = findViewById(R.id.tv_oc_text);
            tv_curr_data = findViewById(R.id.tv_curr_data);

            btnNext.setOnClickListener(this);
            btnPre.setOnClickListener(this);

            update();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        long newStartTime = 0;
        long newEndTime = 0;
        switch (view.getId()) {
            case R.id.btn_next_day:
                newStartTime = startTime + TimeUnit.DAYS.toMillis(1);
                break;
            case R.id.btn_pre_day:
                newStartTime = startTime - TimeUnit.DAYS.toMillis(1);
                break;
            default: {
            }
        }

        newEndTime = newStartTime + TimeUnit.DAYS.toMillis(1);

        if (newEndTime - newStartTime > 0 && startTime > 0) {
            startTime = newStartTime;
            endTime = newEndTime;
            currentDay = dateFormat.format(startTime);

            update();
        }
    }

    private void update() {
        List<USMInfo> usmInfos = USMImpl.getUSMInfo(this.getApplicationContext(), startTime, endTime);
        List<UsageStats> usageStats = USMUtils.getUSM(startTime, endTime, this.getApplicationContext());
        Collections.sort(usageStats, new Comparator<UsageStats>() {
            @Override
            public int compare(UsageStats usageStats, UsageStats t1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return usageStats.getTotalTimeInForeground() < t1.getTotalTimeInForeground()?1:-1;
                }
                return 0;
            }
        });
        StringBuilder builder = new StringBuilder();

        if (usageStats != null) {
            for (int i = 0; i < usageStats.size(); i++) {
                UsageStats stats = usageStats.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && stats.getTotalTimeInForeground() > 1000 * 60) {
                    builder.append(AppUtils.getInstance(getApplicationContext()).getAppName(stats.getPackageName()))
                            .append(" 总共使用：")
                            .append(stats.getTotalTimeInForeground() / 1000f / (60))
                            .append("分钟\n\n");
                }
            }
        }

        if (usmInfos != null) {
            builder.append("----------具体使用-----------\n");
            for (int i = usmInfos.size() - 1; i >= 0; i--) {
                USMInfo info = usmInfos.get(i);
                //微信 几点到几点
                builder.append(timeFormat.format(info.getOpenTime()))
                        .append("-")
                        .append(timeFormat.format(info.getCloseTime()))
                        .append(" [")
                        .append(info.getAppName())
                        .append("]")
                        .append("\n\n");
            }
        }

        tv_curr_data.setText(currentDay);
        tv_oc_text.setText(builder.toString());
    }
}
