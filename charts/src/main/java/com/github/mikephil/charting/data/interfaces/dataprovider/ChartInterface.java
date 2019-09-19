package com.github.mikephil.charting.data.interfaces.dataprovider;

import android.graphics.RectF;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Interface that provides everything there is to know about the dimensions, bounds, and indexRange
 * of the chart.
 *
 * @author Philipp Jahoda
 */
public interface ChartInterface {

    /** Returns the minimum x value of the chart, regardless of zoom or translation. */
    float getXChartMin();

    /** Returns the maximum x value of the chart, regardless of zoom or translation. */
    float getXChartMax();

    float getXRange();

    /** Returns the minimum y value of the chart, regardless of zoom or translation. */
    float getYChartMin();

    /** Returns the maximum y value of the chart, regardless of zoom or translation. */
    float getYChartMax();

    /**
     * Returns the maximum distance in screen dp a touch can be away from an entry to cause it to
     * get highlighted.
     */
    float getMaxHighlightDistance();

    int getWidth();

    int getHeight();

    MPPointF getCenterOfView();

    MPPointF getCenterOffsets();

    RectF getContentRect();

    ValueFormatter getDefaultValueFormatter();

    ChartData getData();

    int getMaxVisibleCount();
}
