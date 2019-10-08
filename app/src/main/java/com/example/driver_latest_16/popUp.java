package com.example.driver_latest_16;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class popUp extends Activity {
    DatabaseReference ref;
    String recData;
    URL Url = null;
    InputStream is = null;
    Button accpt,cnsl;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        Intent i = getIntent();
        mAuth=FirebaseAuth.getInstance();
        final String id =mAuth.getCurrentUser().getUid();
        accpt=(Button)findViewById(R.id.btnOk);
        cnsl=(Button)findViewById(R.id.btnCnsl);
//--------------------------------------------------------------------------------------------------passing id to popup
//        recData= i.getExtras().getString("txtData","");
//        Toast.makeText(popUp.this,"receivd"+recData,Toast.LENGTH_LONG).show();
//--------------------------------------------------------------------------------------------------popup layout
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width =dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.4));

        ref= FirebaseDatabase.getInstance().getReference("rideStates");


//--------------------------------------------------------------------------------------------------accept button
        accpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kafkaEvent(id);
                acceptState("bhqV3lLtbNNJ1DyHaWsaZcG8YRd2");

                Intent shw = new Intent(popUp.this,startRide.class);
                startActivity(shw);

            }
        });
//--------------------------------------------------------------------------------------------------cansl button
        cnsl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shw2 = new Intent(popUp.this,onlineActivity.class);
                startActivity(shw2);
            }
        });

    }
//--------------------------------------------------------------------------------------------------satatus pending --> accepted
    public void acceptState(String id){
        ref.child("activeRides").child(id).child("status").setValue("accepted");

//        Intent i = new Intent(getBaseContext(),popUp.class);
//        i.putExtra("txtData", recData);
//        Intent i=new Intent(popUp.this,startRide.class);
//        startActivity(i);

    }


    //--------------------------------------------------------------------------------------------------save event to kafka topic
    public void kafkaEvent(String id){
        Toast.makeText(popUp.this,"kafka updated",Toast.LENGTH_LONG).show();
    final StringBuffer sb = new StringBuffer("{");
        sb.append("\"riderId").append("\":\"").append("001").append("\"");
        sb.append("\"driverId").append("\":\"").append(id).append("\"");
        sb.append(", \"driverCurrentLocation").append("\":\"").append("location").append("\"");
        sb.append(", \"driverEndLocation").append("\":\"").append("location2").append("\"");
        sb.append(", \"state").append("\":\"").append("accepted by rider").append("\"");
        sb.append('}');

        final String kafkaJason = "http://192.168.43.161:8080/kafka/publish/" + sb;
        popUp.AsyncTaskExample asyncTask = new popUp.AsyncTaskExample();

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
