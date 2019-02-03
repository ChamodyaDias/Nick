package com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chamodyadias.bluetooth_remote.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FollowSubject {
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = remote.btSocket;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public void follow(HashMap<String, Float> map) {
        Log.v("Robot", "came to follow called");
        Float FacePositionCenterX = map.get("FacePositionCenterX");
        Log.v("Robot", "FacePositionCenterX = " + FacePositionCenterX);
        Float FacePositionCenterY = map.get("FacePositionCenterY");
        Log.v("Robot", "FacePositionCenterY = " + FacePositionCenterY);

        if (0.0 < FacePositionCenterX && FacePositionCenterX < 1920.0 && 0.0 < FacePositionCenterY && FacePositionCenterY < 1080) {
            if (710.0 > FacePositionCenterX || FacePositionCenterX > 1210.0) {
                Log.v("Robot", "Out of region turning ");
                if (510.0 > FacePositionCenterX) {
                    try {
                        Log.v("Robot", "turning Right");
//            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(myBluetooth.getAddress());
//            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//            btSocket.connect();
                        if (btSocket != null) {
                            Log.v("Robot", "came to bluetooth called");
                            btSocket.getOutputStream().write("Q".getBytes());
                        } else {
                            Log.v("Robot", "came to else called");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (FacePositionCenterX > 1410.0) {
                    try {
                        Log.v("Robot", "turning Left");
//            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(myBluetooth.getAddress());
//            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//            btSocket.connect();
                        if (btSocket != null) {
                            Log.v("Robot", "came to bluetooth called");
                            btSocket.getOutputStream().write("P".getBytes());
                        } else {
                            Log.v("Robot", "came to else called");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                int pan = Math.round(((FacePositionCenterX) / (1920)) * (130 - 70) + 70);
                int tilt = Math.round(((1080 - FacePositionCenterY) / (1080)) * (115 - 80) + 80);
                Log.v("Robot", "pan = " + pan);
                Log.v("Robot", "tilt = " + tilt);


                try {
                    Log.v("Robot", "came to try");
//            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(myBluetooth.getAddress());
//            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//            btSocket.connect();
                    if (btSocket != null) {
                        Log.v("Robot", "came to bluetooth called");
                        btSocket.getOutputStream().write("Z".getBytes());
                        btSocket.getOutputStream().write("<".getBytes());
                        btSocket.getOutputStream().write(Integer.toString(pan).getBytes());
                        btSocket.getOutputStream().write(",".getBytes());
                        btSocket.getOutputStream().write(Integer.toString(tilt).getBytes());
                        btSocket.getOutputStream().write(">".getBytes());
                    } else {
                        Log.v("Robot", "came to else called");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        //new task().execute(map);

    }


    public class task extends AsyncTask<HashMap<String, Float>, Void, Void> {

        @Override
        public Void doInBackground(HashMap<String, Float>... maps) {
            Log.v("Robot", "success called");


            return null;
        }
    }
}