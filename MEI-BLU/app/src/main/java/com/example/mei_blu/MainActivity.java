package com.example.mei_blu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    Handler bluetoothIn;
    private  int khoidong = 0;
    private ImageView wifi;
    private  TextView show;


    private static final String TAG = "";
    private OutputStream outStream = null;

    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;// khởi tạo bluetooth
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //cấp quyền bluetooth

    // String for MAC address
    private static String address = "00:19:10:08:8e:9a"; //ip bluetooth
    private TextView nhietdo, doam;

    private ImageView den1, den2, den3;
    private ImageView cua, mua, gas;
    private TextView dts;
    private int dem1 = 1, dem2 = 1, dem3 = 1;
    private int dem4 = 0, dem5 = 0, dem6 = 0;


    @SuppressLint({"HandlerLeak", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wifi = findViewById(R.id.imgwifi);
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(MainActivity.this, main_1.class);
                startActivity(x);
            }
        });

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.run);
       // mConnectedThread.write("!");
        den1 = findViewById(R.id.den1);
        den2 = findViewById(R.id.den2);
        den3 = findViewById(R.id.den3);

        show = findViewById(R.id.textView2);
        //show.setVisibility(View.INVISIBLE);

        cua = findViewById(R.id.cua);
        gas = findViewById(R.id.gas);
        mua = findViewById(R.id.mua);

        nhietdo = findViewById(R.id.txtnd);
        doam = findViewById(R.id.txtda);

        den1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem1++;
                if(dem1%2==0)
                {
                    mConnectedThread.write("a");
                    //den1.setBackgroundResource(R.drawable.batden);
                }
                else
                {
                    mConnectedThread.write("b");
                    //den1.setBackgroundResource(R.drawable.tatden);
                }
                mp.start();
            }
        });

        den2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem2++;
                if(dem2%2==0)
                {
                    //den2.setBackgroundResource(R.drawable.batden);
                    mConnectedThread.write("c");

                }
                else
                {
                    mConnectedThread.write("d");
                    //den2.setBackgroundResource(R.drawable.tatden);
                }
                mp.start();
            }
        });

        /*den3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem3++;
                if(dem3%2==0) mConnectedThread.write("e");
                else mConnectedThread.write("f");
            }
        });*/

        cua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dem4++;
                if(dem4%2==0)
                {
                    mConnectedThread.write("g");
                    //cua.setBackgroundResource(R.drawable.mocua);
                }
                else
                {
                    mConnectedThread.write("h");
                    //cua.setBackgroundResource(R.drawable.dongcua);
                }
                mp.start();
            }
        });

        //dts = findViewById(R.id.ts);

        bluetoothIn = new Handler() {   // nhận dữ liệu từ master
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (msg.what == handlerState) {										//if message is what we want
                    String readMessage = (String) msg.obj;
                    if(readMessage.length()>=20)
                    {
                        recDataString.append(readMessage);
                        show.setText("Data Received = " + recDataString);
                        //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                        if (endOfLineIndex > 0) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            //txtString.setText("Data Received = " + dataInPrint);
                            int dataLength = dataInPrint.length();							//get length of data received

                            if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                            {
                                String snhietdo = recDataString.substring(1, 3); //30601 //1 - 3 : 30
                                String sdoam = recDataString.substring(4, 6); //30601 //1 - 3 : 30

                                nhietdo.setText(snhietdo);
                                doam.setText(sdoam);

                                // #27/69/0/1/1/1/1/~
                                // #37/66/1/0/0/1/0/0/~

                                // #27/62/0/0/1/0/0/~

                            String st1 = recDataString.substring(7, 8); //GAS
                            String st2 = recDataString.substring(9, 10); //MUA

                            String st3 = recDataString.substring(11, 12); //30601 //1 - 3 : 30
                            String st4 = recDataString.substring(13, 14); //30601 //1 - 3 : 30
                            String st5 = recDataString.substring(15, 16); //30601 //1 - 3 : 30

                            String st6 = recDataString.substring(16, 17); //30601 //1 - 3 : 30

                            if(st3.equals("1"))
                            {
                                dem1 = 0;
                                den1.setBackgroundResource(R.drawable.batden);
                            }
                            else
                            {
                                dem1 = 1;
                                den1.setBackgroundResource(R.drawable.tatden);
                            }

                            if(st4.equals("1"))
                            {
                                dem2 = 0;
                                den2.setBackgroundResource(R.drawable.batden);
                            }
                            else
                            {
                                dem2 = 1;
                                den2.setBackgroundResource(R.drawable.tatden);
                            }

                            if(st5.equals("1"))
                            {
                                dem4 = 0;
                                cua.setBackgroundResource(R.drawable.mocua);
                            }
                            else
                            {
                                dem4 = 1;
                                cua.setBackgroundResource(R.drawable.dongcua);
                            }

                            if(st1.equals("1"))
                            {
                                dem5 = 0;
                                gas.setBackgroundResource(R.drawable.cogas);
                            }
                            else
                            {
                                dem5 = 1;
                                gas.setBackgroundResource(R.drawable.nogas);
                            }

                            if(st2.equals("1"))
                            {
                                dem6 = 0;
                                mua.setBackgroundResource(R.drawable.comua);
                            }
                            else
                            {
                                dem6 = 1;
                                mua.setBackgroundResource(R.drawable.nomua);
                            }

                            }

                            recDataString.delete(0, recDataString.length()); 					//clear all string data
                            dataInPrint = " ";
                        }
                    }
                    // msg.arg1 = bytes from connect thread

                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();



    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(deviceslist.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //mConnectedThread.write("!");

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called

    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}