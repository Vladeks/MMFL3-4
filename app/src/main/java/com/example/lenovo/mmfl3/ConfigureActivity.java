package com.example.lenovo.mmfl3;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.example.lenovo.mmfl3.sourse.AddConfidanceInterval;
import com.example.lenovo.mmfl3.sourse.ConfidenceInterval;
import com.example.lenovo.mmfl3.sourse.ConfidenceIntervalOperations;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ConfigureActivity extends AppCompatActivity {

    @BindView(R.id.enterOperandsLayout)
    View enterOperandsLayout;

    @BindView(R.id.resultLayout)
    View resultLayout;

    @BindView(R.id.fuzzyNumberResultLayout)
    View fuzzyNumberResultLayout;

    @BindView(R.id.graphLayout)
    View graphLayout;

    @BindView(R.id.etFozzyOfA)
    EditText etFozzyOfA;

    @BindView(R.id.etLBofA)
    EditText etLBofA;

    @BindView(R.id.etUBofA)
    EditText etUBofA;

    @BindView(R.id.etLFozzyOfB)
    EditText etFozzyOfB;

    @BindView(R.id.etLBofB)
    EditText etLBofB;

    @BindView(R.id.etUBofB)
    EditText etUBofB;

    @BindView(R.id.etXvalue)
    EditText etXvalue;

    @BindView(R.id.tvFuzzyNumberC)
    TextView tvFuzzyNumberC;

    @BindView(R.id.tVmAx)
    TextView tVmAx;

    @BindView(R.id.tVmBx)
    TextView tVmBx;

    @BindView(R.id.tVmCx)
    TextView tVmCx;

    @BindView(R.id.tvMembershipFuncA)
    TextView tvMembershipFuncA;

    @BindView(R.id.tvMembershipFuncB)
    TextView tvMembershipFuncB;

    @BindView(R.id.tvMembershipFuncC)
    TextView tvMembershipFuncC;

    @BindView(R.id.tvAlphaA)
    TextView tvAlphaA;

    @BindView(R.id.tvAlphaB)
    TextView tvAlphaB;

    @BindView(R.id.tvAlphaC)
    TextView tvAlphaC;

    @BindView(R.id.btnCalculate)
    Button btnCalculate;

    @BindView(R.id.btnFind)
    Button btnFind;

    @BindView(R.id.graph)
    GraphView graph;

    ConfidenceInterval intervalA;
    ConfidenceInterval intervalB;
    ConfidenceInterval intervalC;
    ConfidenceIntervalOperations operation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    enterOperandsLayout.setVisibility(View.VISIBLE);
                    fuzzyNumberResultLayout.setVisibility(View.GONE);
                    graphLayout.setVisibility(View.GONE);
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return true;
                case R.id.navigation_dashboard:
                    fuzzyNumberResultLayout.setVisibility(View.VISIBLE);
                    enterOperandsLayout.setVisibility(View.GONE);
                    graphLayout.setVisibility(View.GONE);
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return true;
                case R.id.navigation_graph:
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    graphLayout.setVisibility(View.VISIBLE);
                    enterOperandsLayout.setVisibility(View.GONE);
                    fuzzyNumberResultLayout.setVisibility(View.GONE);

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        ButterKnife.bind(this);
        fuzzyNumberResultLayout.setVisibility(View.GONE);
        graphLayout.setVisibility(View.GONE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setUnenabled();

        operation = new AddConfidanceInterval();
    }

    @OnClick(R.id.btnCalculate)
    void buttonCalculateOnClickListener(View view) {
        intervalA = getIntervalFromView(etLBofA, etFozzyOfA, etUBofA, view);
        intervalB = getIntervalFromView(etLBofB, etFozzyOfB, etUBofB, view);

        if(intervalA == null | intervalB == null) {
//            Snackbar.make(view, getString(R.string.error_interval_dont_initialized), Snackbar.LENGTH_LONG).show();
            return;
        } else {
            System.out.println(intervalA.toString());
            System.out.println(intervalB.toString());
            intervalC = operation.calculate(intervalA, intervalB);

            tvFuzzyNumberC.setText(intervalC.toString());

            tvMembershipFuncA.setText(getMembershipResultString(intervalA));
            tvMembershipFuncB.setText(getMembershipResultString(intervalB));
            tvMembershipFuncC.setText(getMembershipResultString(intervalC));

            tvAlphaA.setText(intervalA.getAlphaString());
            tvAlphaB.setText(intervalB.getAlphaString());
            tvAlphaC.setText(intervalC.getAlphaString());

            drawGraph();

            btnFind.setClickable(true);
            etXvalue.setEnabled(true);
        }

    }

    @OnClick(R.id.btnFind)
    void buttonFindOnCliclListener(View view) {
        if (etXvalue.getText().toString().equals("")){
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
        } else {
            double valueX = getDecimalValue(etXvalue);
            if(intervalA == null || intervalB == null || intervalC == null) {
                Snackbar.make(view, getString(R.string.error_interval_dont_initialized), Snackbar.LENGTH_LONG).show();
            } else {
                tVmAx.setText(String.valueOf(intervalA.calculateMembershioFunction(valueX)));
                tVmBx.setText(String.valueOf(intervalB.calculateMembershioFunction(valueX)));
                tVmCx.setText(String.valueOf(intervalC.calculateMembershioFunction(valueX)));
                drawX(valueX);
            }
        }
    }

    @OnTextChanged(R.id.etLBofA)
    public void etLBofAonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    @OnTextChanged(R.id.etFozzyOfA)
    public void etFozzyOfAonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    @OnTextChanged(R.id.etUBofA)
    public void etUBofAonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    @OnTextChanged(R.id.etLBofB)
    public void etLBofBonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    @OnTextChanged(R.id.etLFozzyOfB)
    public void etLFozzyOfBonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    @OnTextChanged(R.id.etUBofB)
    public void etUBofBonTextChanged(CharSequence s, int start, int before, int count) {
        setUnenabled();
    }

    /**
     *
     * @param lowerBound
     * @param plural
     * @param upperBound
     * @param view
     * @return new ConfidenceInterval or null
     */
    private ConfidenceInterval getIntervalFromView(EditText lowerBound, EditText plural, EditText upperBound, View view){
        if(lowerBound.getText().toString().equals("") ||
                plural.getText().toString().equals("") ||
                upperBound.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
            return null;
        } else {
            double leftBound = getDecimalValue(lowerBound);
            double mid = getDecimalValue(plural);
            double rightBound = getDecimalValue(upperBound);

            if(leftBound <= mid && leftBound <= rightBound && mid <= rightBound ) {
                ConfidenceInterval interval = new ConfidenceInterval(leftBound, mid, rightBound);
                return interval;
            } else {
                Snackbar.make(view, getString(R.string.error_interval), Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
    }

    private double getDecimalValue(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    private void setUnenabled() {
        btnFind.setClickable(false);
        etXvalue.setEnabled(false);
    }

    private String getMembershipResultString(ConfidenceInterval number) {
        return String.format("0, when x <= %.1f or x >= %.1f\n" +
                        "(x - %.1f) / (%.1f - %.1f), when %.1f <= x <= %.1f\n" +
                        "(%.1f - x) / (%.2f - %.1f), when %.1f <= x <= %.1f\n",
                number.getLower_boundary(), number.getUpper_boundary(), number.getLower_boundary(),
                number.getPlural_element(), number.getLower_boundary(), number.getLower_boundary(),
                number.getPlural_element(), number.getUpper_boundary(), number.getUpper_boundary(),
                number.getPlural_element(), number.getPlural_element(), number.getUpper_boundary());
    }

    private String getAlphaString(ConfidenceInterval number) {
        return String.format("[%.2f, %.2f]", number.calculateLeftBound(0), number.calculateRightBound(0));
    }

    private void drawConfidanceInterval(ConfidenceInterval number, int color, String title) {
        DataPoint[] data = new DataPoint[] {
                new DataPoint(number.getLower_boundary(), 0),
                new DataPoint(number.getPlural_element(), 1),
                new DataPoint(number.getUpper_boundary(), 0)
        };
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        series.setThickness(10);
        series.setColor(color);
        series.setTitle(title);
        graph.addSeries(series);

//        for (int i = 0; i < data.length; i++) {
//            drawLine(data[i]);
//        }
    }

    private void drawLine(DataPoint point) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                point,
                new DataPoint(point.getX(), -10)
        });
// custom paint to make a dotted line
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.GRAY);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        series.setCustomPaint(paint);
        series.setThickness(3);

        graph.addSeries(series);
    }

    private void drawGraph() {
        graph.getSeries().clear();
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(1.2);
        graph.getViewport().setMinY(-0.5);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        drawConfidanceInterval(intervalA, Color.BLUE, "interval A");
        drawConfidanceInterval(intervalB, Color.GREEN, "interval B");
        drawConfidanceInterval(intervalC, Color.RED, "interval C");
    }

    private void drawX(double x) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(x, 1),
                new DataPoint(x, 0)
        });
        series.setColor(Color.MAGENTA);
        series.setTitle("X");
        series.setThickness(12);
        graph.addSeries(series);
        graph.onDataChanged(true,true);
    }

}
