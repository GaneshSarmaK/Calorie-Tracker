package com.example.drawertest;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Report extends Fragment implements  View.OnClickListener {
    View vReport;
    Button bar;
    Button pie;
    SharedPreferences preferences;
    BarChart barChart;
    PieChart pieChart;

    TextView barDate1;
    TextView barDate2;
    TextView pieDate;
    private DatePickerDialog.OnDateSetListener barDate1Listener;
    private DatePickerDialog.OnDateSetListener barDate2Listener;
    private DatePickerDialog.OnDateSetListener pieDateListener;


    String barFromDate ;
    String barToDate ;
    String pieChartDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);

        barChart = vReport.findViewById(R.id.barChart);
        pieChart = vReport.findViewById(R.id.pieChart);
        pieChart.setNoDataText("");
        barChart.setNoDataText("");

        barDate1 = (TextView) vReport.findViewById(R.id.date1);
        barDate2 = (TextView) vReport.findViewById(R.id.date2);
        pieDate = (TextView) vReport.findViewById(R.id.pieDate);

        bar = (Button) vReport.findViewById(R.id.barButton);
        pie = (Button) vReport.findViewById(R.id.pieButton);


        bar.setOnClickListener(this);
        pie.setOnClickListener(this);


        pieDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Integer year = cal.get(Calendar.YEAR);
                Integer month = cal.get(Calendar.MONTH);
                Integer day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth
                        ,pieDateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        barDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Integer year = cal.get(Calendar.YEAR);
                Integer month = cal.get(Calendar.MONTH);
                Integer day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth
                        ,barDate1Listener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        barDate2Listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mt,dt;
                if(month < 10)
                    mt = "0"+month;
                else
                    mt = month+"";
                if(dayOfMonth<10)
                    dt= "0"+dayOfMonth;
                else
                    dt = dayOfMonth+"";
                String date = year + "-" + mt + "-" + dt;
                barDate2.setText(date);
                barToDate = date;
            }
        };

        barDate1Listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mt,dt;
                if(month < 10)
                    mt = "0"+month;
                else
                    mt = month+"";
                if(dayOfMonth<10)
                    dt= "0"+dayOfMonth;
                else
                    dt = dayOfMonth+"";
                String date = year + "-" + mt + "-" + dt;
                barDate1.setText(date);
                barFromDate = date;
            }
        };

        pieDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mt,dt;
                if(month < 10)
                    mt = "0"+month;
                else
                    mt = month+"";
                if(dayOfMonth<10)
                    dt= "0"+dayOfMonth;
                else
                    dt = dayOfMonth+"";
                String date = year + "-" + mt + "-" + dt;
                pieDate.setText(date);
                pieChartDate = date;
            }
        };

        barDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Integer year = cal.get(Calendar.YEAR);
                Integer month = cal.get(Calendar.MONTH);
                Integer day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth
                        ,barDate2Listener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });





        return vReport;
    }

    @Override
    public void onClick(View v) {

        if (R.id.barButton == v.getId()) {



            preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            int userId = preferences.getInt("userId", 0);

            if(barFromDate.length() == 10 && barToDate.length() == 10) {
                PopulateBarChart obj = new PopulateBarChart();
                obj.execute(barFromDate, barToDate);
            }
            else
                Toast.makeText(getContext(),"Invalid Dates",Toast.LENGTH_SHORT).show();

        }
        else {



            preferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            int userId = preferences.getInt("userId", 0);

            if(pieDate.length() == 10) {
                PieAsyncTask obj = new PieAsyncTask();
                obj.execute(pieChartDate);
            }
            else
                Toast.makeText(getContext(),"Invalid Dates",Toast.LENGTH_SHORT).show();


        }
    }


    private class PopulateBarChart extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {

            SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            return RestMethods.getDataForBarChart(params[0], params[1], preferences.getInt("userId",0));
        }

        @Override
        protected void onPostExecute(JSONObject data) {

            List<Integer> caloriesConsumed = new ArrayList<>();
            List<String> reportDates = new ArrayList<>();
            List<Integer> caloriedBurned = new ArrayList<>();
            try {
                caloriesConsumed = (List<Integer>) data.get("calConsumed");
                caloriedBurned = (List<Integer>) data.get("calBurned");
                ;
                reportDates = (List<String>) data.get("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<BarEntry> calorieConsumedData = new ArrayList<>();
            ArrayList<BarEntry> calorieBurnedData = new ArrayList<>();

            for (int i = 0; i < caloriedBurned.size(); i++) {
                calorieConsumedData.add(new BarEntry(i + 1, caloriesConsumed.get(i)));
                calorieBurnedData.add(new BarEntry(i + 1, caloriedBurned.get(i)));
            }


            BarDataSet bardataset1 = new BarDataSet(calorieConsumedData, "Calories Consumed");

            BarDataSet bardataset2 = new BarDataSet(calorieBurnedData, "Calories Burned");
            barChart.animateY(4000);
            //BarData dataSet = new BarData(reportDates, bardataset);
            BarData dataSet = new BarData(bardataset1, bardataset2);
            bardataset2.setColors(Color.rgb(120, 180, 200));
            bardataset1.setColors(Color.rgb(164, 228, 251));

            barChart.setData(dataSet);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(reportDates));
            barChart.getAxisLeft().setAxisMinimum(1);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(true);
            xAxis.setGranularityEnabled(true);

            float barSpace = 0.02f;
            float groupSpace = 0.3f;
            int groupCount = 4;

            barChart.getAxisLeft().setDrawGridLines(false);
            dataSet.setBarWidth(0.15f);
            barChart.getXAxis().setAxisMinimum(0);
            barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
            barChart.groupBars(0, 0.5f, 0.075f);
            barChart.setNoDataText("");

        }

    }

    private class PieAsyncTask extends AsyncTask<String, Void, HashMap<String, Integer>> {
        @Override
        protected HashMap<String, Integer> doInBackground(String... params) {
            //get data here
            SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            return RestMethods.getDataForPieChart(params[0], preferences.getInt("userId", 0));

        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> data) {

            List<PieEntry> pieEntries = new ArrayList<>();

            Iterator iterator = data.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Integer value = (Integer) data.get(key);
                pieEntries.add(new PieEntry(value, key));
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "Report");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(dataSet);
            com.github.mikephil.charting.charts.PieChart pieChart = vReport.findViewById(R.id.pieChart);
            pieChart.setData(pieData);
            pieChart.animateY(3000);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.invalidate();

        }

    }
}
