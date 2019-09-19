package com.youzan.zancharts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.dataset.LineDataSet;
import com.github.mikephil.charting.data.entry.Entry;
import com.github.mikephil.charting.data.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.youzan.zancharts.internal.Dates;
import com.youzan.zancharts.internal.Drawables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liangfei on 7/22/16.
 */

public class ZanLineChart extends LineChart {
    private static final String TAG = "ZanLineChart";

    private List<Line> mLines;

    private boolean mHighlightEnabled = false;

    // helpers
    public ZanLineChart(Context context) {
        super(context);
    }

    public ZanLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZanLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        // description
        setNoDataText(null);
        setNoDataTextDescription(null);
        mDescPaint.setTextAlign(Paint.Align.LEFT);
        mDescPaint.setColor(0xFF333333);
        mDescPaint.setTextSize(Utils.dp2px(16));

        // gestures
        setTouchEnabled(true);
        setScaleEnabled(false);
        setDragEnabled(false);
        setPinchZoom(false);
        setDoubleTapToZoomEnabled(false);

        // borders
        setDrawBorders(false);

        // description
        setDescription("");

        setHighlightPerDragEnabled(true);

        // x axis
        XAxis xAxis = getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Entry entry = getData().getDataSetByIndex(0).getEntryForXPos(value);
                if (entry == null) return String.valueOf(value);
                return ((ChartItem) entry.getData()).title;
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        // right axis
        YAxis rightAxis = getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);

        // left axis
        YAxis leftAxis = getAxisLeft();
        leftAxis.setAxisMinValue(0);
        leftAxis.setGranularity(2f);
        leftAxis.setLabelCount(4, true);
        leftAxis.setSpaceTop(10f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setZeroLineColor(Color.BLACK);
        leftAxis.setGridColor(0xFFCCCCCC);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(0xFFCCCCCC);
        leftAxis.setTextSize(12.f);
        leftAxis.setYOffset(-8.f);
        leftAxis.setXOffset(0);

        // legend
        Legend legend = getLegend();
        legend.setTextSize(12f);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setXEntrySpace(20f);

        setDescriptionPosition(Utils.dp2px(15),
                legend.getYOffset() + Utils.calcTextHeight(mDescPaint, "L"));
        setDescriptionColor(0xFF333333);
        setDescriptionTextSize(16f);

        ((DefaultValueFormatter) getDefaultValueFormatter()).setup(0);
    }


    public void addLines(List<Line> lines) {
        mLines = new ArrayList<>(lines);
        updateUI();
    }

    public void addLines(Line... lines) {
        if (lines == null) return;
        addLines(Arrays.asList(lines));
    }

    public void addLine(Line line) {
        if (mLines == null) {
            mLines = new ArrayList<>();
        }
        mLines.add(line);
        updateUI();
    }

    private void updateUI() {
        if (mLines == null || mLines.size() == 0) return;

        List<ILineDataSet> sets = new ArrayList<>();

        for (int i = 0, size = mLines.size(); i < size; i++) {
            Line line = mLines.get(i);
            int itemCount = line.items.size();
            List<Entry> entries = new ArrayList<>(itemCount);

            for (int x = 0; x < itemCount; x++) {
                ChartItem item = line.items.get(x);
                float y = 0;
                try {
                    y = Float.parseFloat(item.value);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Entry entry = new Entry(x, y);
                entry.setData(item);
                entries.add(entry);
            }

            LineDataSet dataSet = new LineDataSet(entries, line.label,line.color);
            dataSet.setMode(LineDataSet.Mode.LINEAR);
            dataSet.setDrawValues(false);
            dataSet.setColor(line.color);
            dataSet.setLineWidth(1f);
            dataSet.setDrawCircles(true);
            dataSet.setDrawCircleHole(false);
            dataSet.setDrawFilled(false);
            dataSet.setFillDrawable(Drawables.gradient(line.color));
            dataSet.setHighlightEnabled(mHighlightEnabled);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.setHighLightColor(0xFFC4C4C4);
            sets.add(dataSet);
        }

        LineData data = new LineData(sets);

        float yMax = Math.max(8, data.getYMax());

        getAxisLeft().setAxisMaxValue((int) (yMax / 2) * 2 + 0.5f);
        setData(data);
    }

    public void setHighlightEnabled(boolean enabled) {
        mHighlightEnabled = enabled;

        if (enabled) {
            addHighlightListener();
        }
    }

    private void addHighlightListener() {
        setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setDescription(Dates.toChinese(((ChartItem) e.getData()).title));
                setLegendValues(e, h);
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private ValueFormatter mLabelValueFormatter = new DefaultValueFormatter(0);
    private void setLegendValues(Entry e, Highlight l) {
        LineData data = getLineData();
        int dataSetCount = data.getDataSetCount();

        final float xPos = e.getX();
        for (int i = 0; i < dataSetCount; i++) {
            LineDataSet dataSet = (LineDataSet) data.getDataSetByIndex(i);
            ChartItem item = ((ChartItem) dataSet.getEntryForXPos(xPos).getData());

            String value = item.value;
            float floatValue = 0f;
            try {
                floatValue = Float.parseFloat(value);
            } catch (NumberFormatException ex) {

            }
            value = mLabelValueFormatter.getFormattedValue(floatValue, e, i, getViewPortHandler());
            String label = dataSet.getLabel();
            final int colonIndex = label.indexOf(':');
            if (colonIndex != -1) {
                label = label.substring(0, colonIndex);
            }
            dataSet.setLabel(label + ": " + value);
        }
    }
}
