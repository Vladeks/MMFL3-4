package com.example.lenovo.mmfl3;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.example.lenovo.mmfl3.sourse.ConfidanceIntervalSaver;
import com.example.lenovo.mmfl3.sourse.ConfidenceInterval;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ArrayList<ConfidanceIntervalSaver> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getIntent() !=  null)
            list = (ArrayList<ConfidanceIntervalSaver>)getIntent().getBundleExtra("list").getSerializable("list");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mPagerAdapter = new SectionsPagerAdapter(list, this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends PagerAdapter {

        private ArrayList<ConfidanceIntervalSaver> list;
        private Context context;
        private LayoutInflater mLayoutInflater;

        public SectionsPagerAdapter(ArrayList<ConfidanceIntervalSaver> list, Context context) {
            this.list= list;
            this.context = context;
            mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == list.size()-1){
                return getString(R.string.section_conflict);
            }
            return getString(R.string.section_format, position+1);
        }

        public void addList(ArrayList<ConfidanceIntervalSaver> list) {
            list.clear();
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View rootView = mLayoutInflater.inflate(R.layout.fragment_tankering, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            GraphView graph = (GraphView) rootView.findViewById(R.id.graphFragment);

            textView.setText(getPageTitle(position));

            ConfidenceInterval intervalA = list.get(position).getIntervalA();
            ConfidenceInterval intervalB = list.get(position).getIntervalB();

            drawGraph(graph, intervalA, intervalB, intervalB.getUpper_boundary()+10);
            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            ConfidenceInterval dummyItem = (ConfidenceInterval) ((View) object).getTag();
            int position = list.indexOf(dummyItem);
            if (position >= 0) {
                // The current data matches the data in this active fragment, so let it be as it is.
                return position;
            } else {
                // Returning POSITION_NONE means the current data does not matches the data this fragment is showing right now.  Returning POSITION_NONE constant will force the fragment to redraw its view layout all over again and show new data.
                return POSITION_NONE;
            }
        }

        private void drawGraph(GraphView graph, ConfidenceInterval intervalA, ConfidenceInterval intervalB, double max) {
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMaxY(1.2);
            graph.getViewport().setMinY(-0.1);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
            graph.getLegendRenderer().setVisible(true);
            graph.getViewport().setMaxX(max);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            drawConfidanceInterval(graph, intervalA, Color.GREEN, "Замовлення "+intervalA.toString());
            drawConfidanceInterval(graph, intervalB, Color.RED, "Паливо на танкері "+intervalB.toString());
        }

        private void drawConfidanceInterval(GraphView graph ,ConfidenceInterval number, int color, String title) {
            System.out.println("Draw"+number.toString());
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

        }


    }
}
