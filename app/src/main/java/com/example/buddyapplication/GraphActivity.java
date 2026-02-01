package com.example.buddyapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphActivity extends AppCompatActivity {

    BarChart barChart;   // Gender chart
    PieChart pieChart;   // State chart
    DatabaseHelper db;
    Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        btnBack = findViewById(R.id.btnBack);

        db = new DatabaseHelper(this);

        btnBack.setOnClickListener(v -> finish());

        loadGenderChart();
        loadStateChart();
    }


    private void loadGenderChart() {

        ArrayList<friend> list = db.getAllFriend();

        int male = 0;
        int female = 0;

        for (friend f : list) {
            if (f.gender.equalsIgnoreCase("Male")) male++;
            else if (f.gender.equalsIgnoreCase("Female")) female++;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, male));
        entries.add(new BarEntry(1, female));

        BarDataSet dataSet = new BarDataSet(entries, "Friends by Gender");
        dataSet.setColors(new int[]{Color.BLUE, Color.MAGENTA});

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.setData(data);
        barChart.getDescription().setText("Gender Statistics");
        barChart.animateY(1000);
        barChart.invalidate();
    }


    private void loadStateChart() {

        ArrayList<friend> list = db.getAllFriend();
        HashMap<String, Integer> stateCount = new HashMap<>();

        for (friend f : list) {
            String state = f.addr4;   // addr4 = state

            if (state == null || state.isEmpty()) state = "Unknown";

            if (stateCount.containsKey(state)) {
                stateCount.put(state, stateCount.get(state) + 1);
            } else {
                stateCount.put(state, 1);
            }
        }

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (String state : stateCount.keySet()) {
            entries.add(new PieEntry(stateCount.get(state), state));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Friends by State");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14f);

        pieChart.setData(data);
        pieChart.getDescription().setText("State Distribution");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
