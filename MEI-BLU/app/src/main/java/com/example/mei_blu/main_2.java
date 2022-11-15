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


public class main_2 extends AppCompatActivity {

    Handler bluetoothIn;
    private  int khoidong = 0;

    private int d1 = 1, d2 = 1, d3 = 1, d4 = 1;
    private int c1 = 1, c2 = 1, c3 = 1, c4 = 1;
    private int cd = 1, cs = 1;

    private static final String TAG = "";
    private OutputStream outStream = null;

    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    private TextView nhietdo, doam;

    private ImageView den1, den2, den3;
    private ImageView cua, mua, gas;
    private TextView dts;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        den1 = findViewById(R.id.den1);
        den2 = findViewById(R.id.den2);
        den3 = findViewById(R.id.den3);

        cua = findViewById(R.id.cua);
        gas = findViewById(R.id.gas);
        mua = findViewById(R.id.mua);

        nhietdo = findViewById(R.id.txtnd);
        doam = findViewById(R.id.txtda);

        //dts = findViewById(R.id.ts);

        bluetoothIn = new Handler() {
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (msg.what == handlerState) {										//if message is what we want
                    String readMessage = (String) msg.obj;
                    // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);
                    //dts.setText("Data Received = " + recDataString);
                    //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();							//get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            String snhietdo = recDataString.substring(1, 3); //30601 //1 - 3 : 30
                            String skhoangcach = recDataString.substring(3, 5); //30601 //3 - 5 : 60
                            String svantoc = recDataString.substring(5, 7); //30601 //3 - 5 : 60
                            String sgocquay = recDataString.substring(7, 10); //30601 //3 - 5 : 60
                            String sauto = recDataString.substring(10, 11); //30601 //3 - 5 : 60
                            String sden = recDataString.substring(11, 12); //30601 //3 - 5 : 60
                            String sgas = recDataString.substring(12, 13); //30601 //3 - 5 : 60
                            /*if(sgas.equals("1"))
                            {
                                gas.setBackgroundResource(R.drawable.gas);
                            }
                            else
                            {
                                gas.setBackgroundResource(R.drawable.ngas);
                            }

                            if(sden.contains("1"))
                            {
                                dot.setBackgroundResource(R.drawable.recs);
                                main.setBackgroundResource(R.drawable.bgb);
                                car.setBackgroundResource(R.drawable.cars);
                            }
                            else {
                                dot.setBackgroundResource(R.drawable.rec);
                                main.setBackgroundResource(R.drawable.bga);
                                car.setBackgroundResource(R.drawable.xss);

                            }*/
                            /*if(stb1.contains("1"))
                            {
                                //set den sang
                                //tb1.setBackgroundResource(R.drawable.a1);
                            }
                            else
                            {
                                //set den tat
                                //tb1.setBackgroundResource(R.drawable.a2);
                            }*/

                            //nhietdo.setText(snhietdo +"°C");
                            //khoangcach.setText(skhoangcach);

                        }

                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
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

        mConnectedThread.write("!");

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