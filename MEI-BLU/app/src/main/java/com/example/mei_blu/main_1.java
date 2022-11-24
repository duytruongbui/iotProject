package com.example.mei_blu;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class main_1 extends AppCompatActivity {

    private int dem1 = 0, dem2 = 0, dem3 = 0, dem4 = 0;

    private ImageView den1, den2, quat;
    private ImageView door, gas, mua;
    private TextView nhietdo, doam;

    private String sdata;
    private int tb1 = 0, tb2 = 0, tb3 = 0;
    private int tb4 = 0, tb5 = 0, tb6 = 0;
    private String schuoi;
    private ImageView blue, voice;
    private TextView demo;

    static final int REQUEST_CODE_SPEECH_INPUT = 1; // khai báo biến giọng nói

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        den1 = findViewById(R.id.den1);
        den2 = findViewById(R.id.den2);
        quat = findViewById(R.id.quat);

        door = findViewById(R.id.cua);
        gas = findViewById(R.id.gas);
        mua = findViewById(R.id.mua);

        nhietdo = findViewById(R.id.txtnd);
        doam = findViewById(R.id.txtda);
        voice = findViewById(R.id.imgvoice);
        blue = findViewById(R.id.imgbl);

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //speak();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Xin chào!");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception ex)
                {
                    Toast.makeText(main_1.this, ""+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        blue.setOnClickListener(new View.OnClickListener() { // quay về bluetooth
            @Override
            public void onClick(View v) {
                Intent x = new Intent(main_1.this, deviceslist.class);
                startActivity(x);
            }
        });

        den1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/led");

                dem1++;
                if (dem1 % 2 == 0) myRef.setValue("on");
                else myRef.setValue("off");
            }
        });

//        den2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("smarthome/tb2");
//
//                dem2++;
//                if (dem2 % 2 == 0) myRef.setValue("on");
//                else myRef.setValue("off");
//            }
//        });


        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/door");

                dem2++;
                if (dem2 % 2 == 0) myRef.setValue("on");
                else myRef.setValue("off");
            }
        });

        //LAY DU LIEU TU FIREBASE DE DONG BO TRANG THAI
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try
                {
                    String  led = dataSnapshot.child("smarthome/led").getValue().toString();
                    String  a_door = dataSnapshot.child("smarthome/door").getValue().toString();
                    String  cooler = dataSnapshot.child("smarthome/cooler").getValue().toString();

                    String  sthief = dataSnapshot.child("smarthome/thief").getValue().toString();
                    String  smua = dataSnapshot.child("smarthome/mua").getValue().toString();

                    String  snd = dataSnapshot.child("smarthome/nhietdo").getValue().toString();
                    String  sda = dataSnapshot.child("smarthome/doam").getValue().toString();

                    if(sthief.equals("on")) gas.setBackgroundResource(R.drawable.criminal_here);
                    else gas.setBackgroundResource(R.drawable.criminal);

                    if(smua.equals("on")) mua.setBackgroundResource(R.drawable.comua);
                    else mua.setBackgroundResource(R.drawable.nomua);

                    if(led.equals("on"))
                    {
                        den1.setBackgroundResource(R.drawable.batden);
                        dem1 = 0;
                    }
                    else
                    {
                        den1.setBackgroundResource(R.drawable.tatden);
                        dem1 = 1;
                    }

                    if(a_door.equals("on"))
                    {
                        door.setBackgroundResource(R.drawable.mocua);
                        dem2 = 0;
                    }
                    else
                    {
                        door.setBackgroundResource(R.drawable.dongcua);
                        dem2 = 1;
                    }

                    if(cooler.equals("on"))
                    {
                        quat.setBackgroundResource(R.drawable.fan_on);
                        //Toast.makeText(getApplicationContext(), "BAT DEN", Toast.LENGTH_SHORT).show();
//                        dem3 = 0;
                    }
                    else
                    {
                        quat.setBackgroundResource(R.drawable.fan);
                        //Toast.makeText(getApplicationContext(), "TAT DEN", Toast.LENGTH_SHORT).show();
//                        dem3 = 1;
                    }

                    nhietdo.setText(snd);
                    doam.setText(sda);

                }
                catch (Exception ex)
                {
                    Toast.makeText(main_1.this, "Lỗi CSDL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //demo.setText(result.get(0));
                    String sdata = result.get(0).toUpperCase();

                    if(sdata.equals("BẬT ĐÈN 1"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/led");
                        myRef.setValue("on");
                    }
                    if(sdata.equals("TẮT ĐÈN 1"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/led");
                        myRef.setValue("off");
                    }

                    if(sdata.equals("BẬT ĐÈN 2"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/door");
                        myRef.setValue("on");
                    }
                    if(sdata.equals("TẮT ĐÈN 2"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/door");
                        myRef.setValue("off");
                    }

                    if(sdata.equals("MỞ CỬA"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/cooler");
                        myRef.setValue("on");
                    }
                    if(sdata.equals("ĐÓNG CỬA"))
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("smarthome/cooler");
                        myRef.setValue("off");
                    }

                }
                break;
            }
        }
    }
}