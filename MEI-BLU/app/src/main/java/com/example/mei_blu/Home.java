package com.example.mei_blu;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private TextView temperature, humidity;
    private ImageView led;
    private ImageView door;
    private ImageView thief;
    private ImageView rain;
    private ImageView fan;

    private int count_led = 0, count_door = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView bluetooth = findViewById(R.id.bluetoothImage);

        temperature = findViewById(R.id.tempNumber);
        humidity = findViewById(R.id.humidityNumber);

        ConstraintLayout ledLayout = findViewById(R.id.ledLayout);
        ConstraintLayout doorLayout = findViewById(R.id.doorLayout);
        ConstraintLayout micLayout = findViewById(R.id.micLayout);

        led = findViewById(R.id.ledImage);
        door = findViewById(R.id.doorImage);

        thief = findViewById(R.id.thiefImage);
        rain = findViewById(R.id.rainImage);
        fan = findViewById(R.id.fanImage);

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(Home.this, deviceslist.class);
                startActivity(x);
            }
        });

        ledLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dataRef = database.getReference("smarthome/led");

                count_led++;
                if (count_led % 2 == 0) dataRef.setValue("on");
                else dataRef.setValue("off");
            }
        });

        doorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dataRef = database.getReference("smarthome/door");

                count_door++;
                if (count_door % 2 == 0) dataRef.setValue("on");
                else dataRef.setValue("off");
            }
        });

        micLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
                startActivityForResult(intent, 100);
            }
        });

//        Firebase Synchronize
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String dataTemperature = snapshot.child("smarthome/nhietdo").getValue().toString();
                    String dataHumidity = snapshot.child("smarthome/doam").getValue().toString();

                    String dataLed = snapshot.child("smarthome/led").getValue().toString();
                    String dataDoor = snapshot.child("smarthome/door").getValue().toString();

                    String dataThief = snapshot.child("smarthome/thief").getValue().toString();
                    String dataRain = snapshot.child("smarthome/mua").getValue().toString();
                    String dataFan = snapshot.child("smarthome/cooler").getValue().toString();

                    temperature.setText(dataTemperature);
                    humidity.setText(dataHumidity);

                    if(dataLed.equals("on"))
                    {

                        led.setBackgroundResource(R.drawable.batden);
                        count_led = 0;
                    }
                    else
                    {
                        led.setBackgroundResource(R.drawable.tatden);
                        count_led = 1;
                    }

                    if(dataDoor.equals("on"))
                    {

                        door.setBackgroundResource(R.drawable.mocua);
                        count_door = 0;
                    }
                    else
                    {
                        door.setBackgroundResource(R.drawable.dongcua);
                        count_door = 1;
                    }

                    if (dataThief.equals("on")) thief.setBackgroundResource(R.drawable.criminal_here);
                    else thief.setBackgroundResource(R.drawable.criminal);

                    if (dataRain.equals("on")) rain.setBackgroundResource(R.drawable.comua);
                    else rain.setBackgroundResource(R.drawable.nomua);

                    if (dataFan.equals("on")) fan.setBackgroundResource(R.drawable.fan_on);
                    else fan.setBackgroundResource(R.drawable.fan);



                }
                catch (Exception e) {
                    Toast.makeText(Home.this, "Lỗi CSDL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String sdata = result.get(0).toUpperCase();

            if (sdata.equals("ON THE LIGHT")) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/led");
                myRef.setValue("on");
            }
            if (sdata.equals("OFF THE LIGHT")) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/led");
                myRef.setValue("off");
            }

            if (sdata.equals("OPEN THE DOOR")) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/door");
                myRef.setValue("on");
            }
            if (sdata.equals("CLOSE THE DOOR")) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("smarthome/door");
                myRef.setValue("off");
            }
        }
    }

}
