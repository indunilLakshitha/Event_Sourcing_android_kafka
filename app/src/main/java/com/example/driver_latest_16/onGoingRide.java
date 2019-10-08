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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class onGoingRide extends AppCompatActivity {
   Button end;
    URL Url = null;
    Bitmap bmImg = null;
    InputStream is = null;
    String recData;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going_state);
        end=(Button)findViewById(R.id.btnEndRide);
        Intent i = getIntent();
        recData= i.getExtras().getString("txtData","");
        ref= FirebaseDatabase.getInstance().getReference();
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ends(recData);


            }
        });
    }

    //--------------------------------------------------------------------------------------------------save event to kafka topic
    public void kafkaEvent(String id){
//        Toast.makeText(popUp.this,"kafka updated",Toast.LENGTH_LONG).show();
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"riderId").append("\":\"").append("001").append("\"");
        sb.append("\"driverId").append("\":\"").append(id).append("\"");
        sb.append(", \"driverCurrentLocation").append("\":\"").append("location").append("\"");
        sb.append(", \"driverEndLocation").append("\":\"").append("location2").append("\"");
        sb.append(", \"state").append("\":\"").append("ended by rider").append("\"");
        sb.append('}');

        final String kafkaJason = "http://192.168.43.161:8080/kafka/publish/" + sb;
        onGoingRide.AsyncTaskExample asyncTask = new onGoingRide.AsyncTaskExample();

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
    public void ends(String id){


        ref.child("rideStates").child("activeRides").child(id).child("status").setValue("ended");
        kafkaEvent(id);

        Intent start=new Intent(onGoingRide.this,onlineActivity.class);
        startActivity(start);

    }
}
