package com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;

import java.util.HashMap;

public class NameViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<HashMap<String,Float>> facedata;

    public MutableLiveData<HashMap<String,Float>> getfacedata() {
        if (facedata == null) {
            facedata = new MutableLiveData<>();
        }
        return facedata;
    }


// Rest of the ViewModel...
}