package com.miqt.time.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.miqt.time.R;
import com.miqt.time.bean.USMInfo;
import com.miqt.time.presenter.MainPresenter;
import com.miqt.time.utils.TimeValueFormatter;
import com.miqt.time.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {
    MainPresenter mainPresenter;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart = findViewById(R.id.lineChart);
        chart.getDescription().setText("近7天试用时长");
        // background color
        chart.setBackgroundColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.getXAxis().setValueFormatter(new TimeValueFormatter());
        chart.getXAxis().setLabelRotationAngle(0);
        chart.getAxisLeft().setValueFormatter(new TimeValueFormatter());
        chart.setDrawGridBackground(false);
        // set listeners
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(false);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mainPresenter = new MainPresenter(this);
        mainPresenter.start(this);
    }


    @Override
    public void show7DayUsageData(long[] times) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        List<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            yVals.add(new Entry(i + 1, times[i]));
        }
        LineDataSet lineDataSet = new LineDataSet(yVals, "app使用时长");
        lineDataSet.setValueFormatter(new TimeValueFormatter());
        lineDataSet.disableDashedLine();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);
    }

    @Override
    public void showTodayUsageData(List<USMInfo> usmInfos) {

    }
}
