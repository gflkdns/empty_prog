package com.miqt.time.activity;

import android.app.usage.UsageStats;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BaseDataSet;
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
import com.github.mikephil.charting.utils.MPPointF;
import com.miqt.time.R;
import com.miqt.time.bean.USMInfo;
import com.miqt.time.bean.UsageData;
import com.miqt.time.presenter.MainPresenter;
import com.miqt.time.utils.AppNameValueFormatter;
import com.miqt.time.utils.AppUtils;
import com.miqt.time.utils.ImageUtils;
import com.miqt.time.utils.TimeValueFormatter;
import com.miqt.time.view.MainView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    MainPresenter mainPresenter;
    LineChart lineChart;
    private ArrayList<PieEntry> pieVals;
    private PieChart preChart;
    HorizontalBarChart horizontalBarChart;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLineChart();
       // HorizontalBarChart();

        textView = findViewById(R.id.textView);
        initPieChart();
        mainPresenter = new MainPresenter(this);
        mainPresenter.start(this);
    }

    private void initPieChart() {
        preChart = findViewById(R.id.preChart);
        preChart.setNoDataText("无数据");
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


        setColors(dataSet);
        data.addDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter(preChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        preChart.highlightValues(null);
        preChart.setData(data);
        preChart.getDescription().setEnabled(false);
        preChart.getLegend().setEnabled(false);
        preChart.invalidate();
    }

    private void HorizontalBarChart() {
       // horizontalBarChart = findViewById(R.id.horizontalBarChart);
    }

    private void initLineChart() {
        lineChart = findViewById(R.id.lineChart);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
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

        lineChart.setBackgroundColor(Color.rgb(104, 241, 175));
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
        lineChart.setNoDataText("无数据");
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
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
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
        dataSet.setIconsOffset(MPPointF.getInstance(0, 20));
        setColors(dataSet);
        data.addDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter(preChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        preChart.highlightValues(null);
        preChart.setData(data);
        updateView(preChart);
    }

    private void setColors(BaseDataSet dataSet) {
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
    }

    @Override
    public void showTodayUsageData(final List<USMInfo> usmInfos) {
        if (usmInfos == null) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < usmInfos.size(); i++) {
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

      runOnUiThread(new Runnable() {
          @Override
          public void run() {
              textView.setText(builder.toString());
          }
      });

//        if (usmInfos == null || usmInfos.size() == 1) {
//            return;
//        }
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                int viewheight = usmInfos.size() * 100;
//                viewheight = Math.max(viewheight, 800);
//                ViewGroup.LayoutParams layoutParams = horizontalBarChart.getLayoutParams();
//                layoutParams.height = viewheight;
//                horizontalBarChart.setLayoutParams(layoutParams);
//            }
//        });
//
//        final BarData data = new BarData();
//        List<BarEntry> values = new ArrayList<>();
//        for (int i = 0; i < usmInfos.size(); i++) {
//            USMInfo info = usmInfos.get(i);
//            Drawable icon = AppUtils.getInstance(getApplicationContext()).getIcon(info.getPkgName());
//            icon = ImageUtils.zoomDrawable(icon, 150, 150);
//            if (icon == null) continue;
//            values.add(
//                    new BarEntry(i, info.getCloseTime() - info.getOpenTime(),
//                            icon, info));
//        }
//        BarDataSet set = new BarDataSet(values, "");
//        set.setIconsOffset(new MPPointF(50,0));
//        setColors(set);
//        data.addDataSet(set);
//        data.setValueFormatter(new PercentFormatter(preChart));
//        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.BLACK);
//        data.setValueFormatter(new AppNameValueFormatter(getApplicationContext()));
//
//        horizontalBarChart.setData(data);
//        horizontalBarChart.invalidate();
    }
}
