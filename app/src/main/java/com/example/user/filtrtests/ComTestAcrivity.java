package com.example.user.filtrtests;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ComTestAcrivity extends AppCompatActivity {
    //2 обединяем это комплементарный фильтр
    GraphView graph;
    private double graph2LastXValue = 5d;
    private double graph2LastYValue = 5d;
    private double graph2LastZValue = 5d;
    private Double[] dataPoints;
    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> seriesX;
    LineGraphSeries<DataPoint> seriesZ;
    LineGraphSeries<DataPoint> seriesXX;
    LineGraphSeries<DataPoint> seriesYY;
    LineGraphSeries<DataPoint> seriesZZ;
    private Thread thread;
    private boolean plotData = true;
    SensorManager gyroManager, accManager;
    Sensor gyroSensor, accSensor;
    private SensorManager mSensorManager;
    private float k = 0.05f;
   public float xx;
    float x;
    float yy;
    float zz;
    float xf;
    float yf;
    float zf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_test_acrivity);






        gyroManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = gyroManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = accManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);




    graph = (GraphView) findViewById(R.id.graph1);
//    series = new LineGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(0, 0),});
//    series.setColor(Color.GREEN);
//        graph.addSeries(series);
    seriesX = new LineGraphSeries<DataPoint>(new DataPoint[]{
        new DataPoint(0, 0),
    });
        seriesX.setColor(Color.BLACK);

    seriesZ = new LineGraphSeries<DataPoint>(new DataPoint[]{
        new DataPoint(0, 0),
    });
        seriesZ.setColor(Color.RED);
    seriesXX = new LineGraphSeries<DataPoint>(new DataPoint[]{
        new DataPoint(0, 0),

    });
        seriesXX.setColor(Color.YELLOW);
//
    seriesZZ = new LineGraphSeries<DataPoint>(new DataPoint[]{
        new DataPoint(0, 0),});
        seriesZZ.setColor(Color.LTGRAY);
    seriesYY = new LineGraphSeries<DataPoint>(new DataPoint[]{
        new DataPoint(0, 0), });seriesYY.setColor(Color.MAGENTA);

//        graph.addSeries(seriesX);
//        graph.addSeries(series);
//        graph.addSeries(seriesZ);
//        graph.addSeries(seriesXX);
//        graph.addSeries(seriesYY);
//        graph.addSeries(seriesZZ);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);
    feedMultiple();

}

    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    //
//
//
//
public SensorEventListener accListener = new SensorEventListener() {
    @Override
    public void onSensorChanged(SensorEvent event) {

        float xx = event.values[0];
        float yy = event.values[1];
        float zz = event.values[2];

//            tv_accX.setText("X Acc : " + x + " m/s2");
//            tv_accY.setText("Y Acc: " + y + " m/s2");
//            tv_accZ.setText("Z Acc: " + z + " m/s2");
    }

    public void addEntry(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        System.out.println(x);
        float y = values[1];
        System.out.println(y);
        float z = values[2];
        System.out.println(z);


        float xx = event.values[0];
        float yy = event.values[1];
        float zz = event.values[2];


        graph2LastXValue += 1d;
        graph2LastYValue += 1d;
        graph2LastZValue += 1d;


        xf = k*x+(1-k)*xx;
        yf = k*y * (1-k)*yy;
        zf = k*z+(1-k)*zz;

        //   series.appendData(new DataPoint(graph2LastYValue, y), true, 20);
        //  seriesX.appendData(new DataPoint(graph2LastXValue, x), true, 20);
        seriesZ.appendData(new DataPoint(graph2LastZValue, z), true, 20);
        //seriesXX.appendData(new DataPoint(graph2LastXValue, xx), true, 20);
        //  seriesYY.appendData(new DataPoint(graph2LastYValue, yy), true, 20);
        seriesZZ.appendData(new DataPoint(graph2LastZValue, zf), true, 20);
        //  graph.addSeries(seriesX);
        // graph.addSeries(seriesXX);
        graph.addSeries(seriesZ);
       // graph.addSeries(series);
        //graph.addSeries(seriesYY);
        graph.addSeries(seriesZZ);
    }

    private void addDataPoint(double acceleration) {
        dataPoints[499] = acceleration;
    }

    private void feedMultiple() {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    //@Override
    protected void onPause() {
     //  super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener((SensorEventListener) this);

    }
    public void onResume() {
      //  super.onResume();
        gyroManager.registerListener(gyroListener, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        accManager.registerListener(accListener, accSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    public SensorEventListener gyroListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float xx = event.values[0];
            float yy = event.values[1];
            float zz = event.values[2];

//            textX.setText("X : " + x + " rad/s");
//            textY.setText("Y : " + y + " rad/s");
//            textZ.setText("Z : " + z + " rad/s");
        }
    };



        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
//}

