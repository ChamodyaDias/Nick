package com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

//public class FacePositionListener {

    public interface FacePositionValueListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        // public void onObjectReady(String title);
        // or when data has been loaded
        void onValuesChanged(HashMap<String,Float> map);
    }


//    public class sendFacedata extends AsyncTask<HashMap<String,Float>,Void,Void> {
//        @Override
//        public Void doInBackground(HashMap... hashMaps){
//            Log.v("Robot","Async task for sending face data");
//            Log.v("Robot",hashMaps[0].toString());
//            listener.onValuesChanged(hashMaps[0]);
//            return null;
//        }
//
//    }
//}
