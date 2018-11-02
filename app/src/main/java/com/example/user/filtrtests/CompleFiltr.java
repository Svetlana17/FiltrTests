
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
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
//комплементарный фильтр
public class CompleFiltr
        extends AppCompatActivity  implements SensorEventListener {
    Button button;
    private SensorManager mSensorManager;
    Sensor sensorGiroscope;
    Sensor sensorAcc;
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
    float xf;
    float yf;
    float zf;
    float xfiltr;

    float yfiltr;
    float zfiltr;
    private final float k = 0.04f;
    private final float alpha = 0.4f;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comple_filtr);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGiroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0),
        });
        series.setColor(Color.GREEN);
        graph.addSeries(series);
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
                new DataPoint(0, 0),
        });
        seriesZZ.setColor(Color.LTGRAY);

//
        seriesYY = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0),
        });
        seriesYY.setColor(Color.MAGENTA);

        graph.addSeries(seriesX);
        graph.addSeries(series);
        graph.addSeries(seriesZ);
        graph.addSeries(seriesXX);
        graph.addSeries(seriesYY);
        graph.addSeries(seriesZZ);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);
        feedMultiple();

    }


    public void addEntry (SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        System.out.println(x);
        float y = values[1];
        System.out.println(y);
        float z = values[2];
        System.out.println(z);

        graph2LastXValue += 1d;
        graph2LastYValue += 1d;
        graph2LastZValue += 1d;




        xfiltr=k*x;
        System.out.println(xfiltr);

        yfiltr=k*y;
        zfiltr=k*z;



        //  yy=complementaryRatio*y+(1-complementaryRatio)*accelY;
        // zz=complementaryRatio*z+(1-complementaryRatio)*accelZ;
//            yy=yy+altha*(y-yy);
//            zz=zz+altha*(z-zz);

          series.appendData(new DataPoint(graph2LastYValue, y), true, 20);
      //  seriesX.appendData(new DataPoint(graph2LastXValue, x), true, 20);
        // seriesZ.appendData(new DataPoint(graph2LastZValue, z), true, 20);
      ///  seriesXX.appendData(new DataPoint(graph2LastXValue, xfiltr), true, 20);
         seriesYY.appendData(new DataPoint(graph2LastYValue, yfiltr), true, 20);
//         seriesZZ.appendData(new DataPoint(graph2LastZValue, zfiltr), true, 20);
//          graph.addSeries(seriesX);
//
//          graph.addSeries(seriesXX);
        graph.addSeries(series);
        graph.addSeries(seriesYY);
        //  graph.addSeries(seriesZ);
        // graph.addSeries(seriesZZ);
    }


    private void feedMultiple () {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause () {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener((SensorEventListener) this);

    }

    //    @Override
    public void onSensorChanged ( final SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
            //accelVals = lowPass( event.values.clone(), accelVals );
            if (plotData) {
//            addEntry(event);
                //
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                addEntry(event);
                            }
                        });


                    }

                }).start();

                //
                plotData = false;
            }
    }

    //    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }

    @Override
    protected void onResume () {
        super.onResume();
        mSensorManager.registerListener((SensorEventListener) this, sensorGiroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy () {
        mSensorManager.unregisterListener((SensorEventListener) CompleFiltr.this);
        thread.interrupt();
        super.onDestroy();
    }


    public void onClick (View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

