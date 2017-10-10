package com.example.lenovo.mmfl3;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.lenovo.mmfl3.sourse.ConfidanceIntervalSaver;
import com.example.lenovo.mmfl3.sourse.ConfidenceInterval;
import com.example.lenovo.mmfl3.sourse.SubtractConfidanteInterval;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class TankeringActivity extends AppCompatActivity {

    //@BindView(R.id.etInputFuelLevel)
    EditText etInputFuelLevel;

    //@BindView(R.id.btnExecute)
    Button btnExecute;

    ArrayList<ConfidanceIntervalSaver> list;
    SubtractConfidanteInterval sub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tankering);


        sub = new SubtractConfidanteInterval();
        list = new ArrayList<>();


        etInputFuelLevel = (EditText) findViewById(R.id.etInputFuelLevel);
        btnExecute = (Button) findViewById(R.id.btnExecute);

        ArrayList<ConfidanceIntervalSaver> tmp = new ArrayList<>();
        tmp.add(new ConfidanceIntervalSaver(new ConfidenceInterval(2,3, 5),
                new ConfidenceInterval(4, 6,8)));


        btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Execute click");
                btnExecuteOnClickListener(v);
            }
        });

    }

    //@OnClick(R.id.btnExecute)
    void btnExecuteOnClickListener(View view) {
        if(etInputFuelLevel.getText().toString().equals("")) {
            Snackbar.make(view, getString(R.string.error_field_is_empty), Snackbar.LENGTH_LONG).show();
        } else {
            double fuelLevel = getDecimalValue(etInputFuelLevel);
            if (fuelLevel < 300) {
                Snackbar.make(view, R.string.low_fuel_level, Snackbar.LENGTH_LONG).show();
                return;
            } else {
                calculate(fuelLevel);

                Intent intent = new Intent(this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent.putExtra("list", bundle);
                startActivity(intent);

            }
        }
    }


    private double getDecimalValue(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    private void calculate(double fuelLevel) {
        list.clear();
        ConfidenceInterval neededFuel = ConfidenceInterval.getRandomConfidanceInterval(fuelLevel);
        ConfidenceInterval leftedFuel = ConfidenceInterval.clearNumberToConfidenceInterval(fuelLevel);
/*
    топливо яке є на танкерв
    топливо необхідне для перевезення
 */
        System.out.println("List add");
        while (true) {
            System.out.println(neededFuel.toString()+ leftedFuel.toString());
            list.add(new ConfidanceIntervalSaver(neededFuel, leftedFuel));
            if (neededFuel.getUpper_boundary() > leftedFuel.getLower_boundary()) {
                System.out.println("<---EXIT--->");
                break;
            }

            leftedFuel = sub.calculate(leftedFuel, neededFuel);
            neededFuel = ConfidenceInterval.getRandomConfidanceInterval(fuelLevel);
        }
    }

}
