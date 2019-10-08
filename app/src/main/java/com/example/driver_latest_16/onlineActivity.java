package com.example.driver_latest_16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class onlineActivity extends AppCompatActivity {


    int i;
    Handler one;
    Button online,cl;
    private static final String NODE_USERS = "onlineDrivers";
    private FirebaseAuth mAuth;
    URL Url = null;
    InputStream is = null;
    Bitmap bmImg = null;
    StringBuffer sb;
    public int x;
    String ride="gjhgjhghgjhghj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        online=(Button)findViewById(R.id.btnGoOnline);
//        cl=(Button)findViewById(R.id.test);
        one=new Handler();


//        cl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent x=new Intent(onlineActivity.this,startRide.class);
//                startActivity(x);
//            }
//        });

        mAuth = FirebaseAuth.getInstance();
        x=1;

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful()) {
                                    String token = task.getResult().getToken();

//                                    click(token);
                                 saveToken(token);
//                                 startStartActivity();
                                    read();
//                                   startPopUp();
                                } else {


                                }
                            }
                        });
      }
        });
//        read();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
// -------------------------------------------------------------------------------------------------save token

    private void saveToken(String token) {
        String email = mAuth.getCurrentUser().getEmail();
        User user = new User(email, token);
//        NODE_USERS
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference();


        dbUsers.child("users").child("online Drivers").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(onlineActivity.this, "Token Saved", Toast.LENGTH_LONG).show();
                    online.setText("GO OFFLINE");
                }
            }
        });
    }

// -------------------------------------------------------------------------------------------------remove token
    private void delToken(String token) {
        String email = mAuth.getCurrentUser().getEmail();
        User user = new User(email, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        dbUsers.child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(onlineActivity.this, "Token del", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


//--------------------------------------------------------------------------------------------------asyncTask for record events to kafka
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

            return bmImg;


        }}

        private void  click(String token) {


        if (x==0){
            online.setText("GO OFFLINE");

          saveToken(token);

            x=1;
            Toast.makeText(onlineActivity.this,x,Toast.LENGTH_LONG).show();
        }else if (x==1){
            online.setText("GO ONLINE");
         delToken(token);
            x=0;
            Toast.makeText(onlineActivity.this,x,Toast.LENGTH_LONG).show();
        }else {

            Toast.makeText(onlineActivity.this,"error",Toast.LENGTH_LONG).show();
        }

        }

    private void startStartActivity() {
        Intent intent = new Intent(this, startRide.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void  startPopUp(){

//        Intent shw=new Intent(onlineActivity.this,popUp.class);
//        startActivity(shw);
        Intent i = new Intent(getBaseContext(),popUp.class);
        i.putExtra("txtData", ride);
        startActivity(i);

    }



//    ----------------------------------------------------------------------------------------------reading for a ride
Thread t1=new Thread(){
    @Override
    public void run() {
        for (i=0;i<1000;i=i+2){

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            one.post(new Runnable() {
                @Override
                public void run() {
//                    read();
//                        Toast.makeText(ProfileActivity.this,"number : "+i,Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
};

    //----------------------------------------------------------------------------------------------read database status
    public void read(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mostafa = ref.child("id");
        mostafa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ride = dataSnapshot.getValue(String.class).toString();
//                Toast.makeText(onlineActivity.this,   ride, Toast.LENGTH_LONG).show();
                //do what you want with the email


                if(ride.length()>1){
//
                  startPopUp();
////                   t1.interrupt();
                }
//                Toast.makeText(ProfileActivity.this,   state, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(ProfileActivity.this, "complete : ", Toast.LENGTH_LONG).show();
            }
        });



    }

}
