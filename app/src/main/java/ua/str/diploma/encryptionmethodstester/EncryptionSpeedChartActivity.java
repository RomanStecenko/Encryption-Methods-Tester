package ua.str.diploma.encryptionmethodstester;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class EncryptionSpeedChartActivity extends AppCompatActivity {
    private BarChart mChart;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encryption_speed_chart);

        mChart = (BarChart) findViewById(R.id.chart);
        mChart.setDescription("");
        mChart.setNoDataTextDescription(getString(R.string.error));
        mChart.setDrawGridBackground(false);
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);

        dbHelper = new DbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initChart();
    }

    private void initChart() {
        Result[] results = dbHelper.getLastResults();
        if (results.length < 1) {
            Toast.makeText(this, R.string.finish_test, Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        int i = 0;
        for (Result result : results) {
            entries.add(new BarEntry(result.getEncryptionTime(), i));
            xVals.add(result.getMethod());
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "All 6 methods");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(xVals, dataSet);
        mChart.setData(barData);
        mChart.getLegend().setEnabled(false);
        mChart.invalidate();
        mChart.animateY(2000);
    }
//        set.setValueFormatter(new CpuRateFormatter());

//    public class CpuRateFormatter implements ValueFormatter {
//        @Override
//        public String getFormattedValue(float value) {
//            return String.format("%d ms", (int)value);
//        }
//    }
}
