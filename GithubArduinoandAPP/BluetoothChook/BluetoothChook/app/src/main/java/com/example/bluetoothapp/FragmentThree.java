package com.example.bluetoothapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class FragmentThree extends Fragment {

    public FragmentThree() { }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private final Handler mHandler = new Handler();

    boolean toggled;


    LineChart lineChart1,lineChart2,lineChart3,lineChart4,lineChart5,lineChart6;
    LineDataSet set1,set2,set3,set4,set5,set6;
    ArrayList<Entry> dataVals1 = new ArrayList<>();
    ArrayList<Entry> dataVals2 = new ArrayList<>();
    ArrayList<Entry> dataVals3 = new ArrayList<>();
    ArrayList<Entry> dataVals4 = new ArrayList<>();
    ArrayList<Entry> dataVals5 = new ArrayList<>();
    ArrayList<Entry> dataVals6 = new ArrayList<>();
    float f1=0;
    float f2=0;
    float f3=0;
    float f4=0;
    float f5=0;
    float f6=0;

    String data_1 = "0";
    String data_2 = "0";
    String data_3 = "0";
    String data_4 = "0";
    String data_5 = "0";
    String data_6 = "0";

    Drawable drawable;

    XAxis xAxis1,xAxis2,xAxis3,xAxis4,xAxis5,xAxis6;
    YAxis yAxis1,yAxis2,yAxis3,yAxis4,yAxis5,yAxis6;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_fragment_three, container, false);

        lineChart1 = root.findViewById(R.id.brain_chart1);
        lineChart1.setNoDataText("No data found ❗");
        lineChart1.setNoDataTextColor(Color.parseColor("#000000"));

        lineChart2 = root.findViewById(R.id.brain_chart2);
        lineChart2.setNoDataText("No data found ❗");
        lineChart2.setNoDataTextColor(Color.parseColor("#000000"));

        lineChart3 = root.findViewById(R.id.brain_chart3);
        lineChart3.setNoDataText("No data found ❗");
        lineChart3.setNoDataTextColor(Color.parseColor("#000000"));

        lineChart4 = root.findViewById(R.id.brain_chart4);
        lineChart4.setNoDataText("No data found ❗");
        lineChart4.setNoDataTextColor(Color.parseColor("#000000"));

        lineChart5 = root.findViewById(R.id.brain_chart5);
        lineChart5.setNoDataText("No data found ❗");
        lineChart5.setNoDataTextColor(Color.parseColor("#000000"));

        lineChart6 = root.findViewById(R.id.brain_chart6);
        lineChart6.setNoDataText("No data found ❗");
        lineChart6.setNoDataTextColor(Color.parseColor("#000000"));


        lineChart1.getAxisLeft().setDrawGridLines(false);
        lineChart1.getXAxis().setDrawGridLines(false);
        lineChart1.setTouchEnabled(true);
        lineChart1.setClickable(true);
        lineChart1.setDragEnabled(true);
        lineChart1.setHighlightPerDragEnabled(true);
        lineChart1.setHighlightPerTapEnabled(true);
        lineChart1.setDoubleTapToZoomEnabled(false);
        lineChart1.getAxisRight().setDrawGridLines(false);
        lineChart1.getAxisRight().setDrawLabels(false);
        lineChart1.getAxisRight().setDrawAxisLine(false);
        lineChart1.getAxisLeft().setDrawAxisLine(false);
        lineChart1.getXAxis().setDrawAxisLine(false);
        lineChart1.setBackgroundColor(Color.WHITE);
        lineChart1.setScaleYEnabled(false);
        lineChart1.setScaleXEnabled(true);
        lineChart1.getViewPortHandler().setMaximumScaleX(40);
        lineChart1.getDescription().setEnabled(true);
        lineChart1.getDescription().setText("");
        lineChart1.animateX(1000);
        lineChart1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart1.setExtraOffsets(10, 30, 10, 10);
        Legend L1 = lineChart1.getLegend();
        L1.setXOffset(-5);
        L1.setYOffset(10);
        L1.setTextColor(Color.parseColor("#000000"));
        L1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis1 = lineChart1.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawGridLines(false);
        xAxis1.setLabelCount(8);
        xAxis1.setLabelRotationAngle(-55);
        xAxis1.setTextColor(Color.parseColor("#000000"));
        yAxis1 = lineChart1.getAxisLeft();
        yAxis1.setAxisMinimum(0);
        yAxis1.setAxisLineColor(Color.parseColor("#656565"));
        yAxis1.setLabelCount(5, true);
        yAxis1.setDrawGridLines(true);
        yAxis1.setTextColor(Color.parseColor("#000000"));
        yAxis1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        lineChart2.getAxisLeft().setDrawGridLines(false);
        lineChart2.getXAxis().setDrawGridLines(false);
        lineChart2.setTouchEnabled(true);
        lineChart2.setClickable(true);
        lineChart2.setDragEnabled(true);
        lineChart2.setHighlightPerDragEnabled(true);
        lineChart2.setHighlightPerTapEnabled(true);
        lineChart2.setDoubleTapToZoomEnabled(false);
        lineChart2.getAxisRight().setDrawGridLines(false);
        lineChart2.getAxisRight().setDrawLabels(false);
        lineChart2.getAxisRight().setDrawAxisLine(false);
        lineChart2.getAxisLeft().setDrawAxisLine(false);
        lineChart2.getXAxis().setDrawAxisLine(false);
        lineChart2.setBackgroundColor(Color.WHITE);
        lineChart2.setScaleYEnabled(false);
        lineChart2.setScaleXEnabled(true);
        lineChart2.getViewPortHandler().setMaximumScaleX(40);
        lineChart2.getDescription().setEnabled(true);
        lineChart2.getDescription().setText("");
        lineChart2.animateX(1000);
        lineChart2.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart2.setExtraOffsets(10, 30, 10, 10);
        Legend L2 = lineChart2.getLegend();
        L2.setXOffset(-5);
        L2.setYOffset(10);
        L2.setTextColor(Color.parseColor("#000000"));
        L2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis2 = lineChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        xAxis2.setLabelCount(8);
        xAxis2.setLabelRotationAngle(-55);
        xAxis2.setTextColor(Color.parseColor("#000000"));
        yAxis2 = lineChart2.getAxisLeft();
        yAxis2.setAxisMinimum(0);
        yAxis2.setAxisLineColor(Color.parseColor("#656565"));
        yAxis2.setLabelCount(5, true);
        yAxis2.setDrawGridLines(true);
        yAxis2.setTextColor(Color.parseColor("#000000"));
        yAxis2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        lineChart3.getAxisLeft().setDrawGridLines(false);
        lineChart3.getXAxis().setDrawGridLines(false);
        lineChart3.setTouchEnabled(true);
        lineChart3.setClickable(true);
        lineChart3.setDragEnabled(true);
        lineChart3.setHighlightPerDragEnabled(true);
        lineChart3.setHighlightPerTapEnabled(true);
        lineChart3.setDoubleTapToZoomEnabled(false);
        lineChart3.getAxisRight().setDrawGridLines(false);
        lineChart3.getAxisRight().setDrawLabels(false);
        lineChart3.getAxisRight().setDrawAxisLine(false);
        lineChart3.getAxisLeft().setDrawAxisLine(false);
        lineChart3.getXAxis().setDrawAxisLine(false);
        lineChart3.setBackgroundColor(Color.WHITE);
        lineChart3.setScaleYEnabled(false);
        lineChart3.setScaleXEnabled(true);
        lineChart3.getViewPortHandler().setMaximumScaleX(40);
        lineChart3.getDescription().setEnabled(true);
        lineChart3.getDescription().setText("");
        lineChart3.animateX(1000);
        lineChart3.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart3.setExtraOffsets(10, 30, 10, 10);
        Legend L3 = lineChart3.getLegend();
        L3.setXOffset(-5);
        L3.setYOffset(10);
        L3.setTextColor(Color.parseColor("#000000"));
        L3.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis3 = lineChart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setDrawGridLines(false);
        xAxis3.setLabelCount(8);
        xAxis3.setLabelRotationAngle(-55);
        xAxis3.setTextColor(Color.parseColor("#000000"));
        yAxis3 = lineChart3.getAxisLeft();
        yAxis3.setAxisMinimum(0);
        yAxis3.setAxisLineColor(Color.parseColor("#656565"));
        yAxis3.setLabelCount(5, true);
        yAxis3.setDrawGridLines(true);
        yAxis3.setTextColor(Color.parseColor("#000000"));
        yAxis3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        lineChart4.getAxisLeft().setDrawGridLines(false);
        lineChart4.getXAxis().setDrawGridLines(false);
        lineChart4.setTouchEnabled(true);
        lineChart4.setClickable(true);
        lineChart4.setDragEnabled(true);
        lineChart4.setHighlightPerDragEnabled(true);
        lineChart4.setHighlightPerTapEnabled(true);
        lineChart4.setDoubleTapToZoomEnabled(false);
        lineChart4.getAxisRight().setDrawGridLines(false);
        lineChart4.getAxisRight().setDrawLabels(false);
        lineChart4.getAxisRight().setDrawAxisLine(false);
        lineChart4.getAxisLeft().setDrawAxisLine(false);
        lineChart4.getXAxis().setDrawAxisLine(false);
        lineChart4.setBackgroundColor(Color.WHITE);
        lineChart4.setScaleYEnabled(false);
        lineChart4.setScaleXEnabled(true);
        lineChart4.getViewPortHandler().setMaximumScaleX(40);
        lineChart4.getDescription().setEnabled(true);
        lineChart4.getDescription().setText("");
        lineChart4.animateX(1000);
        lineChart4.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart4.setExtraOffsets(10, 30, 10, 10);
        Legend L4 = lineChart4.getLegend();
        L4.setXOffset(-5);
        L4.setYOffset(10);
        L4.setTextColor(Color.parseColor("#000000"));
        L4.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L4.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis4 = lineChart4.getXAxis();
        xAxis4.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis4.setDrawGridLines(false);
        xAxis4.setLabelCount(8);
        xAxis4.setLabelRotationAngle(-55);
        xAxis4.setTextColor(Color.parseColor("#000000"));
        yAxis4 = lineChart4.getAxisLeft();
        yAxis4.setAxisMinimum(0);
        yAxis4.setAxisLineColor(Color.parseColor("#656565"));
        yAxis4.setLabelCount(5, true);
        yAxis4.setDrawGridLines(true);
        yAxis4.setTextColor(Color.parseColor("#000000"));
        yAxis4.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        lineChart5.getAxisLeft().setDrawGridLines(false);
        lineChart5.getXAxis().setDrawGridLines(false);
        lineChart5.setTouchEnabled(true);
        lineChart5.setClickable(true);
        lineChart5.setDragEnabled(true);
        lineChart5.setHighlightPerDragEnabled(true);
        lineChart5.setHighlightPerTapEnabled(true);
        lineChart5.setDoubleTapToZoomEnabled(false);
        lineChart5.getAxisRight().setDrawGridLines(false);
        lineChart5.getAxisRight().setDrawLabels(false);
        lineChart5.getAxisRight().setDrawAxisLine(false);
        lineChart5.getAxisLeft().setDrawAxisLine(false);
        lineChart5.getXAxis().setDrawAxisLine(false);
        lineChart5.setBackgroundColor(Color.WHITE);
        lineChart5.setScaleYEnabled(false);
        lineChart5.setScaleXEnabled(true);
        lineChart5.getViewPortHandler().setMaximumScaleX(40);
        lineChart5.getDescription().setEnabled(true);
        lineChart5.getDescription().setText("");
        lineChart5.animateX(1000);
        lineChart5.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart5.setExtraOffsets(10, 30, 10, 10);
        Legend L5 = lineChart5.getLegend();
        L5.setXOffset(-5);
        L5.setYOffset(10);
        L5.setTextColor(Color.parseColor("#000000"));
        L5.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L5.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis5 = lineChart5.getXAxis();
        xAxis5.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis5.setDrawGridLines(false);
        xAxis5.setLabelCount(8);
        xAxis5.setLabelRotationAngle(-55);
        xAxis5.setTextColor(Color.parseColor("#000000"));
        yAxis5 = lineChart5.getAxisLeft();
        yAxis5.setAxisMinimum(0);
        yAxis5.setAxisLineColor(Color.parseColor("#656565"));
        yAxis5.setLabelCount(5, true);
        yAxis5.setDrawGridLines(true);
        yAxis5.setTextColor(Color.parseColor("#000000"));
        yAxis5.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        lineChart6.getAxisLeft().setDrawGridLines(false);
        lineChart6.getXAxis().setDrawGridLines(false);
        lineChart6.setTouchEnabled(true);
        lineChart6.setClickable(true);
        lineChart6.setDragEnabled(true);
        lineChart6.setHighlightPerDragEnabled(true);
        lineChart6.setHighlightPerTapEnabled(true);
        lineChart6.setDoubleTapToZoomEnabled(false);
        lineChart6.getAxisRight().setDrawGridLines(false);
        lineChart6.getAxisRight().setDrawLabels(false);
        lineChart6.getAxisRight().setDrawAxisLine(false);
        lineChart6.getAxisLeft().setDrawAxisLine(false);
        lineChart6.getXAxis().setDrawAxisLine(false);
        lineChart6.setBackgroundColor(Color.WHITE);
        lineChart6.setScaleYEnabled(false);
        lineChart6.setScaleXEnabled(true);
        lineChart6.getViewPortHandler().setMaximumScaleX(40);
        lineChart6.getDescription().setEnabled(true);
        lineChart6.getDescription().setText("");
        lineChart6.animateX(1000);
        lineChart6.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart6.setExtraOffsets(10, 30, 10, 10);
        Legend L6 = lineChart6.getLegend();
        L6.setXOffset(-5);
        L6.setYOffset(10);
        L6.setTextColor(Color.parseColor("#000000"));
        L6.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        L6.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        xAxis6 = lineChart6.getXAxis();
        xAxis6.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis6.setDrawGridLines(false);
        xAxis6.setLabelCount(8);
        xAxis6.setLabelRotationAngle(-55);
        xAxis6.setTextColor(Color.parseColor("#000000"));
        yAxis6 = lineChart6.getAxisLeft();
        yAxis6.setAxisMinimum(0);
        yAxis6.setAxisLineColor(Color.parseColor("#656565"));
        yAxis6.setLabelCount(5, true);
        yAxis6.setDrawGridLines(true);
        yAxis6.setTextColor(Color.parseColor("#000000"));
        yAxis6.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });


        SharedPreferences SH = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int u = SH.getInt("u", 0);
        if(u!=0){
            update_view();
        }
        
        mToastRunnable.run();

        return root;
    }

    private void update_view()
    {
        xAxis1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        xAxis2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        xAxis3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        xAxis4.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        xAxis5.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        xAxis6.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date(((long)value)*1000);
                return sdf.format(date);
            }
        });

        Date today = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String date = formatter.format(today);
        @SuppressLint("SimpleDateFormat")
        DateFormat formatt = new SimpleDateFormat("HH:mm:ss");
        Date dat = null;
        try {
            dat = formatt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SharedPreferences sh2 = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int u = sh2.getInt("u", 0);

        for (int i = 1; i < u; i++) {
            String h1 = sh2.getString("h" + i, "0");
            float f1 = sh2.getFloat("f1" + i, 0);
            dataVals1.add(new Entry(Float.parseFloat(h1), f1, null, null));
        }

        lineChart1.moveViewToX(Objects.requireNonNull(dat).getTime());
        set1 = new LineDataSet(dataVals1, "Motor Temperature");
        set1.setValues(dataVals1);
        set1.setColor(Color.parseColor("#FF796F"));
        set1.setFormSize(8f);
        set1.setForm(Legend.LegendForm.SQUARE);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setDrawVerticalHighlightIndicator(false);
        set1.setDrawCircleHole(true);
        set1.setCircleHoleRadius(1f);
        set1.setCircleColor(Color.parseColor("#FF796F"));
        set1.setCircleRadius(2.5f);
        set1.setDrawCircles(true);
        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill1);
        set1.setFillDrawable(drawable);
        set1.setLineWidth(1.5f);
        LineData data1 = new LineData(set1);
        lineChart1.clear();
        lineChart1.notifyDataSetChanged();
        lineChart1.setData(data1);
        lineChart1.invalidate();
        lineChart1.setMaxVisibleValueCount(101);
        lineChart1.setVisibleXRangeMaximum(100);
        set1.setDrawValues(false);

        for (int i = 1; i < u; i++) {
            String h2 = sh2.getString("h" + i, "0");
            float f2= sh2.getFloat("f2" + i, 0);
            dataVals2.add(new Entry(Float.parseFloat(h2), f2, null, null));
        }
        lineChart2.moveViewToX(Objects.requireNonNull(dat).getTime());
        set2 = new LineDataSet(dataVals2, "Brake Temperature");
        set2.setValues(dataVals2);
        set2.setColor(Color.parseColor("#FF796F"));
        set2.setFormSize(8f);
        set2.setForm(Legend.LegendForm.SQUARE);
        set2.setDrawHorizontalHighlightIndicator(false);
        set2.setDrawVerticalHighlightIndicator(false);
        set2.setDrawCircleHole(true);
        set2.setCircleHoleRadius(1f);
        set2.setCircleColor(Color.parseColor("#FF796F"));
        set2.setCircleRadius(2.5f);
        set2.setDrawCircles(true);
        set2.setDrawFilled(true);
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill1);
        set2.setFillDrawable(drawable);
        set2.setLineWidth(1.5f);
        LineData data2 = new LineData(set2);
        lineChart2.clear();
        lineChart2.notifyDataSetChanged();
        lineChart2.setData(data2);
        lineChart2.invalidate();
        lineChart2.setMaxVisibleValueCount(101);
        lineChart2.setVisibleXRangeMaximum(100);
        set2.setDrawValues(false);

        for (int i = 1; i < u; i++) {
            String h3 = sh2.getString("h" + i, "0");
            float f3= sh2.getFloat("f3" + i, 0);
            dataVals3.add(new Entry(Float.parseFloat(h3), f3, null, null));
        }

        lineChart3.moveViewToX(Objects.requireNonNull(dat).getTime());
        set3 = new LineDataSet(dataVals3, "Motor RPM");
        set3.setValues(dataVals3);
        set3.setColor(Color.parseColor("#3F51B5"));
        set3.setFormSize(8f);
        set3.setForm(Legend.LegendForm.SQUARE);
        set3.setDrawHorizontalHighlightIndicator(false);
        set3.setDrawVerticalHighlightIndicator(false);
        set3.setDrawCircleHole(true);
        set3.setCircleHoleRadius(1f);
        set3.setCircleColor(Color.parseColor("#505050"));
        set3.setCircleRadius(2.5f);
        set3.setDrawCircles(true);
        set3.setDrawFilled(true);
        set3.setMode(LineDataSet.Mode.LINEAR);
        set3.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill2);
        set3.setFillDrawable(drawable);
        set3.setLineWidth(1.5f);
        LineData data3 = new LineData(set3);
        lineChart3.clear();
        lineChart3.notifyDataSetChanged();
        lineChart3.setData(data3);
        lineChart3.invalidate();
        lineChart3.setMaxVisibleValueCount(101);
        lineChart3.setVisibleXRangeMaximum(100);
        set3.setDrawValues(false);

        for (int i = 1; i < u; i++) {
            String h4 = sh2.getString("h" + i, "0");
            float f4= sh2.getFloat("f4" + i, 0);
            dataVals4.add(new Entry(Float.parseFloat(h4), f4, null, null));
        }

        lineChart4.moveViewToX(Objects.requireNonNull(dat).getTime());
        set4 = new LineDataSet(dataVals4, "Wheel RPM");
        set4.setValues(dataVals4);
        set4.setColor(Color.parseColor("#3F51B5"));
        set4.setFormSize(8f);
        set4.setForm(Legend.LegendForm.SQUARE);
        set4.setDrawHorizontalHighlightIndicator(false);
        set4.setDrawVerticalHighlightIndicator(false);
        set4.setDrawCircleHole(true);
        set4.setCircleHoleRadius(1f);
        set4.setCircleColor(Color.parseColor("#3F51B5"));
        set4.setCircleRadius(2.5f);
        set4.setDrawCircles(true);
        set4.setDrawFilled(true);
        set4.setMode(LineDataSet.Mode.LINEAR);
        set4.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill2);
        set4.setFillDrawable(drawable);
        set4.setLineWidth(1.5f);
        LineData data4 = new LineData(set4);
        lineChart4.clear();
        lineChart4.notifyDataSetChanged();
        lineChart4.setData(data4);
        lineChart4.invalidate();
        lineChart4.setMaxVisibleValueCount(101);
        lineChart4.setVisibleXRangeMaximum(100);
        set4.setDrawValues(false);

        for (int i = 1; i < u; i++) {
            String h5 = sh2.getString("h" + i, "0");
            float f5= sh2.getFloat("f5" + i, 0);
            dataVals5.add(new Entry(Float.parseFloat(h5), f5, null, null));
        }

        lineChart5.moveViewToX(Objects.requireNonNull(dat).getTime());
        set5 = new LineDataSet(dataVals5, "Voltage");
        set5.setValues(dataVals5);
        set5.setColor(Color.parseColor("#505050"));
        set5.setFormSize(8f);
        set5.setForm(Legend.LegendForm.SQUARE);
        set5.setDrawHorizontalHighlightIndicator(false);
        set5.setDrawVerticalHighlightIndicator(false);
        set5.setDrawCircleHole(true);
        set5.setCircleHoleRadius(1f);
        set5.setCircleColor(Color.parseColor("#505050"));
        set5.setCircleRadius(2.5f);
        set5.setDrawCircles(true);
        set5.setDrawFilled(true);
        set5.setMode(LineDataSet.Mode.LINEAR);
        set5.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill3);
        set5.setFillDrawable(drawable);
        set5.setLineWidth(1.5f);
        LineData data5 = new LineData(set5);
        lineChart5.clear();
        lineChart5.notifyDataSetChanged();
        lineChart5.setData(data5);
        lineChart5.invalidate();
        lineChart5.setMaxVisibleValueCount(101);
        lineChart5.setVisibleXRangeMaximum(100);
        set5.setDrawValues(false);

        for (int i = 1; i < u; i++) {
            String h6 = sh2.getString("h" + i, "0");
            float f6= sh2.getFloat("f6" + i, 0);
            dataVals6.add(new Entry(Float.parseFloat(h6), f6, null, null));
        }

        lineChart6.moveViewToX(Objects.requireNonNull(dat).getTime());
        set6 = new LineDataSet(dataVals6, "Current");
        set6.setValues(dataVals6);
        set6.setColor(Color.parseColor("#505050"));
        set6.setFormSize(8f);
        set6.setForm(Legend.LegendForm.SQUARE);
        set6.setDrawHorizontalHighlightIndicator(false);
        set6.setDrawVerticalHighlightIndicator(false);
        set6.setDrawCircleHole(true);
        set6.setCircleHoleRadius(1f);
        set6.setCircleColor(Color.parseColor("#505050"));
        set6.setCircleRadius(2.5f);
        set6.setDrawCircles(true);
        set6.setDrawFilled(true);
        set6.setMode(LineDataSet.Mode.LINEAR);
        set6.setCubicIntensity(0.1f);
        drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill3);
        set6.setFillDrawable(drawable);
        set6.setLineWidth(1.5f);
        LineData data6 = new LineData(set6);
        lineChart6.clear();
        lineChart6.notifyDataSetChanged();
        lineChart6.setData(data6);
        lineChart6.invalidate();
        lineChart6.setMaxVisibleValueCount(101);
        lineChart6.setVisibleXRangeMaximum(100);
        set6.setDrawValues(false);
    }

    private final Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(this, 1000);

            SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
            toggled = sh.getBoolean("toggled",false);
            String s3 = sh.getString("time", "0");

            if(toggled)
            {

                data_1 = sh.getString("s1", "0");
                data_2 = sh.getString("s2", "0");
                data_3 = sh.getString("s3", "0");
                data_4 = sh.getString("s4", "0");
                data_5 = sh.getString("s5", "0");
                data_6 = sh.getString("s6", "0");

                double u1 = Double.parseDouble(data_1);
                double u2 = Double.parseDouble(data_2);
                double u3 = Double.parseDouble(data_3);
                double u4 = Double.parseDouble(data_4);
                double u5 = Double.parseDouble(data_5);
                double u6 = Double.parseDouble(data_6);

                f1 = Float.parseFloat(String.valueOf(u1));
                f2 = Float.parseFloat(String.valueOf(u2));
                f3 = Float.parseFloat(String.valueOf(u3));
                f4 = Float.parseFloat(String.valueOf(u4));
                f5 = Float.parseFloat(String.valueOf(u5));
                f6 = Float.parseFloat(String.valueOf(u6));

                xAxis1.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });

                xAxis2.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });

                xAxis3.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });

                xAxis4.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });

                xAxis5.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });

                xAxis6.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date(((long)value)*1000);
                        return sdf.format(date);
                    }
                });


                Date today = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String date = formatter.format(today);
                @SuppressLint("SimpleDateFormat")
                DateFormat formatt = new SimpleDateFormat("HH:mm:ss");
                Date dat = null;
                try {
                    dat = formatt.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dataVals1.add(new Entry(Float.parseFloat(s3),f1, null, null));
                lineChart1.moveViewToX(Objects.requireNonNull(dat).getTime());
                set1 = new LineDataSet(dataVals1, "Motor Temperature");
                set1.setValues(dataVals1);
                set1.setColor(Color.parseColor("#FF796F"));
                set1.setFormSize(8f);
                set1.setForm(Legend.LegendForm.SQUARE);
                set1.setDrawHorizontalHighlightIndicator(false);
                set1.setDrawVerticalHighlightIndicator(false);
                set1.setDrawCircleHole(true);
                set1.setCircleHoleRadius(1f);
                set1.setCircleColor(Color.parseColor("#FF796F"));
                set1.setCircleRadius(2.5f);
                set1.setDrawCircles(true);
                set1.setDrawFilled(true);
                set1.setMode(LineDataSet.Mode.LINEAR);
                set1.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill1);
                set1.setFillDrawable(drawable);
                set1.setLineWidth(1.5f);
                LineData data1 = new LineData(set1);
                lineChart1.clear();
                lineChart1.notifyDataSetChanged();
                lineChart1.setData(data1);
                lineChart1.invalidate();
                lineChart1.setMaxVisibleValueCount(101);
                lineChart1.setVisibleXRangeMaximum(100);
                set1.setDrawValues(false);


                dataVals2.add(new Entry(Float.parseFloat(s3),f2, null, null));
                lineChart2.moveViewToX(Objects.requireNonNull(dat).getTime());
                set2 = new LineDataSet(dataVals2, "Brake Temperature");
                set2.setValues(dataVals2);
                set2.setColor(Color.parseColor("#FF796F"));
                set2.setFormSize(8f);
                set2.setForm(Legend.LegendForm.SQUARE);
                set2.setDrawHorizontalHighlightIndicator(false);
                set2.setDrawVerticalHighlightIndicator(false);
                set2.setDrawCircleHole(true);
                set2.setCircleHoleRadius(1f);
                set2.setCircleColor(Color.parseColor("#FF796F"));
                set2.setCircleRadius(2.5f);
                set2.setDrawCircles(true);
                set2.setDrawFilled(true);
                set2.setMode(LineDataSet.Mode.LINEAR);
                set2.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill1);
                set2.setFillDrawable(drawable);
                set2.setLineWidth(1.5f);
                LineData data2 = new LineData(set2);
                lineChart2.clear();
                lineChart2.notifyDataSetChanged();
                lineChart2.setData(data2);
                lineChart2.invalidate();
                lineChart2.setMaxVisibleValueCount(101);
                lineChart2.setVisibleXRangeMaximum(100);
                set2.setDrawValues(false);

                dataVals3.add(new Entry(Float.parseFloat(s3),f3, null, null));
                lineChart3.moveViewToX(Objects.requireNonNull(dat).getTime());
                set3 = new LineDataSet(dataVals3, "Motor RPM");
                set3.setValues(dataVals3);
                set3.setColor(Color.parseColor("#3F51B5"));
                set3.setFormSize(8f);
                set3.setForm(Legend.LegendForm.SQUARE);
                set3.setDrawHorizontalHighlightIndicator(false);
                set3.setDrawVerticalHighlightIndicator(false);
                set3.setDrawCircleHole(true);
                set3.setCircleHoleRadius(1f);
                set3.setCircleColor(Color.parseColor("#505050"));
                set3.setCircleRadius(2.5f);
                set3.setDrawCircles(true);
                set3.setDrawFilled(true);
                set3.setMode(LineDataSet.Mode.LINEAR);
                set3.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill2);
                set3.setFillDrawable(drawable);
                set3.setLineWidth(1.5f);
                LineData data3 = new LineData(set3);
                lineChart3.clear();
                lineChart3.notifyDataSetChanged();
                lineChart3.setData(data3);
                lineChart3.invalidate();
                lineChart3.setMaxVisibleValueCount(101);
                lineChart3.setVisibleXRangeMaximum(100);
                set3.setDrawValues(false);

                dataVals4.add(new Entry(Float.parseFloat(s3),f4, null, null));
                lineChart4.moveViewToX(Objects.requireNonNull(dat).getTime());
                set4 = new LineDataSet(dataVals4, "Wheel RPM");
                set4.setValues(dataVals4);
                set4.setColor(Color.parseColor("#3F51B5"));
                set4.setFormSize(8f);
                set4.setForm(Legend.LegendForm.SQUARE);
                set4.setDrawHorizontalHighlightIndicator(false);
                set4.setDrawVerticalHighlightIndicator(false);
                set4.setDrawCircleHole(true);
                set4.setCircleHoleRadius(1f);
                set4.setCircleColor(Color.parseColor("#3F51B5"));
                set4.setCircleRadius(2.5f);
                set4.setDrawCircles(true);
                set4.setDrawFilled(true);
                set4.setMode(LineDataSet.Mode.LINEAR);
                set4.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill2);
                set4.setFillDrawable(drawable);
                set4.setLineWidth(1.5f);
                LineData data4 = new LineData(set4);
                lineChart4.clear();
                lineChart4.notifyDataSetChanged();
                lineChart4.setData(data4);
                lineChart4.invalidate();
                lineChart4.setMaxVisibleValueCount(101);
                lineChart4.setVisibleXRangeMaximum(100);
                set4.setDrawValues(false);

                dataVals5.add(new Entry(Float.parseFloat(s3),f5, null, null));
                lineChart5.moveViewToX(Objects.requireNonNull(dat).getTime());
                set5 = new LineDataSet(dataVals5, "Voltage");
                set5.setValues(dataVals5);
                set5.setColor(Color.parseColor("#505050"));
                set5.setFormSize(8f);
                set5.setForm(Legend.LegendForm.SQUARE);
                set5.setDrawHorizontalHighlightIndicator(false);
                set5.setDrawVerticalHighlightIndicator(false);
                set5.setDrawCircleHole(true);
                set5.setCircleHoleRadius(1f);
                set5.setCircleColor(Color.parseColor("#505050"));
                set5.setCircleRadius(2.5f);
                set5.setDrawCircles(true);
                set5.setDrawFilled(true);
                set5.setMode(LineDataSet.Mode.LINEAR);
                set5.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill3);
                set5.setFillDrawable(drawable);
                set5.setLineWidth(1.5f);
                LineData data5 = new LineData(set5);
                lineChart5.clear();
                lineChart5.notifyDataSetChanged();
                lineChart5.setData(data5);
                lineChart5.invalidate();
                lineChart5.setMaxVisibleValueCount(101);
                lineChart5.setVisibleXRangeMaximum(100);
                set5.setDrawValues(false);

                dataVals6.add(new Entry(Float.parseFloat(s3),f6, null, null));
                lineChart6.moveViewToX(Objects.requireNonNull(dat).getTime());
                set6 = new LineDataSet(dataVals6, "Current");
                set6.setValues(dataVals6);
                set6.setColor(Color.parseColor("#505050"));
                set6.setFormSize(8f);
                set6.setForm(Legend.LegendForm.SQUARE);
                set6.setDrawHorizontalHighlightIndicator(false);
                set6.setDrawVerticalHighlightIndicator(false);
                set6.setDrawCircleHole(true);
                set6.setCircleHoleRadius(1f);
                set6.setCircleColor(Color.parseColor("#505050"));
                set6.setCircleRadius(2.5f);
                set6.setDrawCircles(true);
                set6.setDrawFilled(true);
                set6.setMode(LineDataSet.Mode.LINEAR);
                set6.setCubicIntensity(0.1f);
                drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.chart_fill3);
                set6.setFillDrawable(drawable);
                set6.setLineWidth(1.5f);
                LineData data6 = new LineData(set6);
                lineChart6.clear();
                lineChart6.notifyDataSetChanged();
                lineChart6.setData(data6);
                lineChart6.invalidate();
                lineChart6.setMaxVisibleValueCount(101);
                lineChart6.setVisibleXRangeMaximum(100);
                set6.setDrawValues(false);
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mToastRunnable);
    }
}

