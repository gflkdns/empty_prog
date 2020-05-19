package com.miqt.time.activity;

import android.app.usage.UsageStats;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.miqt.time.R;
import com.miqt.time.bean.USMInfo;
import com.miqt.time.bean.UsageData;
import com.miqt.time.presenter.MainPresenter;
import com.miqt.time.utils.AppUtils;
import com.miqt.time.utils.ImageUtils;
import com.miqt.time.utils.TimeValueFormatter;
import com.miqt.time.view.MainView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    MainPresenter mainPresenter;
    LineChart lineChart;
    private ArrayList<PieEntry> pieVals;
    private PieChart preChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLineChart();
        HorizontalBarChart();
        initPieChart();
        mainPresenter = new MainPresenter(this);
        mainPresenter.start(this);
    }

    private void initPieChart() {
        preChart = findViewById(R.id.preChart);
        preChart.setUsePercentValues(true);
        preChart.getDescription().setEnabled(false);
        preChart.setExtraOffsets(5, 10, 5, 5);

        preChart.setDragDecelerationFrictionCoef(0.95f);

        preChart.setCenterTextTypeface(Typeface.DEFAULT);


        preChart.setDrawHoleEnabled(true);
        preChart.setHoleColor(Color.WHITE);

        preChart.setTransparentCircleColor(Color.WHITE);
        preChart.setTransparentCircleAlpha(110);

        preChart.setHoleRadius(58f);
        preChart.setTransparentCircleRadius(61f);

        preChart.setDrawCenterText(true);

        preChart.setRotationAngle(0);
        preChart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = preChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // entry label styling
        preChart.setEntryLabelColor(Color.WHITE);
        preChart.setEntryLabelTextSize(12f);
        // enable rotation of the lineChart by touch
        preChart.setRotationEnabled(true);
        preChart.setHighlightPerTapEnabled(true);

        PieData data = new PieData();
        pieVals = new ArrayList<>();

        PieDataSet dataSet = new PieDataSet(pieVals, "单日用量统计");


        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        data.addDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter(preChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        preChart.highlightValues(null);
        preChart.setData(data);
        preChart.invalidate();
    }

    private void HorizontalBarChart() {

    }

    private void initLineChart() {
        lineChart = findViewById(R.id.lineChart);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                UsageData usageData = (UsageData) e.getData();
                mainPresenter.on7DayValueSelected(usageData);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        lineChart.getDescription().setText("近7天试用时长");
        // background color
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);
        // enable touch gestures
        lineChart.setTouchEnabled(true);
        lineChart.getXAxis().setValueFormatter(new TimeValueFormatter());
        lineChart.getXAxis().setLabelRotationAngle(0);
        lineChart.getAxisLeft().setValueFormatter(new TimeValueFormatter());
        lineChart.setDrawGridBackground(true);
        // set listeners
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mainPresenter.on7DayValueSelected((UsageData) e.getData());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        // lineChart.setScaleXEnabled(true);
        // lineChart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChart.setPinchZoom(false);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    }


    @Override
    public void show7DayUsageData(LinkedList<UsageData> usageData) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        List<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < usageData.size(); i++) {
            yVals.add(new Entry(i + 1, usageData.get(i).getTotaltime(), usageData.get(i)));
        }
        LineDataSet lineDataSet = new LineDataSet(yVals, "app使用时长");
        lineDataSet.setValueFormatter(new TimeValueFormatter());
        lineDataSet.disableDashedLine();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        updateView(lineChart);
    }

    private void updateView(final View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.invalidate();
            }
        });
    }

    @Override
    public void showDayUsageData(String time, List<UsageStats> usageStats) {
        pieVals = new ArrayList<>();
        for (int i = 0; i < usageStats.size(); i++) {
            UsageStats stats = usageStats.get(i);
            if (stats == null) {
                continue;
            }
            if (stats.getTotalTimeInForeground() <= 1000 * 60 * 5) {
                continue;
            }
            String name = AppUtils.getInstance(getApplicationContext()).getAppName(stats.getPackageName());
            Drawable icon = AppUtils.getInstance(getApplicationContext()).getIcon(stats.getPackageName());
            if (icon == null) {
                continue;
            }
            icon = ImageUtils.zoomDrawable(icon, 150, 150);
            pieVals.add(new PieEntry(stats.getTotalTimeInForeground(), "", icon, stats));
        }
        preChart.setCenterText(time);
        PieData data = new PieData();
        PieDataSet dataSet = new PieDataSet(pieVals, "单日用量统计");
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        data.addDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter(preChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        preChart.highlightValues(null);
        preChart.setData(data);
        updateView(preChart);
    }

    @Override
    public void showTodayUsageData(List<USMInfo> usmInfos) {

    }
}
