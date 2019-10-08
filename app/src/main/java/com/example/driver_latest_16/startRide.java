package com.example.driver_latest_16;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class startRide extends AppCompatActivity {
    Button start,end;
    URL Url = null;
    Bitmap bmImg = null;
    InputStream is = null;
    DatabaseReference ref;
    String recData;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_status2);
        Intent i = getIntent();
//        recData= i.getExtras().getString("txtData","");
        start=(Button)findViewById(R.id.btnStartRide);

        ref= FirebaseDatabase.getInstance().getReference("rideStates");
        mAuth=FirebaseAuth.getInstance();
        final String id=mAuth.getCurrentUser().getUid();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                kafkaEvent(id);
                changeState("bhqV3lLtbNNJ1DyHaWsaZcG8YRd2");
                Intent i = new Intent(getBaseContext(),onGoingRide.class);
                i.putExtra("txtData", "bhqV3lLtbNNJ1DyHaWsaZcG8YRd2");
                startActivity(i);
            }
        });
    }
//    @SuppressLint("StaticFieldLeak")
//    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            try {
//
//                Url = new URL(strings[0]);
//                HttpURLConnection conn = (HttpURLConnection) Url
//                        .openConnection();
//                conn.setDoInput(true);
//                conn.connect();
//                is = conn.getInputStream();
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bmImg;
//
//
//        }}

    //--------------------------------------------------------------------------------------------------satatus accepted-->started

    public  void changeState(String id){

            ref.child("activeRides").child(id).child("status").setValue("started");
            kafkaEvent(id);

        }

    public void kafkaEvent(String id){

 Toast.makeText(startRide.this,"kafka updated",Toast.LENGTH_LONG).show();

//--------------------------------------------------------------------------------------------------save event to kafka topic
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"riderId").append("\":\"").append("001").append("\"");
        sb.append("\"driverId").append("\":\"").append(id).append("\"");
        sb.append(", \"driverCurrentLocation").append("\":\"").append("location").append("\"");
        sb.append(", \"driverEndLocation").append("\":\"").append("location2").append("\"");
        sb.append(", \"state").append("\":\"").append("started by rider").append("\"");
        sb.append('}');
        final String kafkaJason = "http://192.168.43.161:8080/kafka/publish/" + sb;
        startRide.AsyncTaskExample asyncTask = new startRide.AsyncTaskExample();
        asyncTask.execute(kafkaJason);

    }

//    ----------------------------------------------------------------------------------------------asyncTask for kafka producer request
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {

                Url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) Url
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;


        }
    }



}
