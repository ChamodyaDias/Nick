// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.chamodyadias.bluetooth_remote.FaceDetection.LivePreviewActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.FrameMetadata;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.GraphicOverlay;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.VisionProcessorBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Face Detector Demo.
 */
public class FaceDetectionProcessor extends VisionProcessorBase<List<FirebaseVisionFace>> {
FollowSubject followSubject = new FollowSubject();

    private static final String TAG = "Robot";

    private final FirebaseVisionFaceDetector detector;

    public FacePosition FP = new FacePosition();

    public FaceDetectionProcessor() {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setTrackingEnabled(true)
                        .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.v(TAG, "Exception thrown while trying to close Face Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionFace>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<FirebaseVisionFace> faces,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        for (int i = 0; i < faces.size(); ++i) {
            FirebaseVisionFace face = faces.get(i);
            FaceGraphic faceGraphic = new FaceGraphic(graphicOverlay);
            graphicOverlay.add(faceGraphic);
            faceGraphic.updateFace(face, frameMetadata.getCameraFacing());
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.v(TAG, "Face detection failed " + e);
    }


//    public class FollowSubject extends AsyncTask<HashMap<String,Float>,Void,Void> {
//        @Override
//        public Void doInBackground(HashMap<String,Float>... maps){
//            Log.v("Robot","success called");
//            Float val = maps[0].get("FacePositionLeft");
//            Log.v("Robot",val.toString());
//            myBluetooth = BluetoothAdapter.getDefaultAdapter();
//            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(myBluetooth.getAddress());
//            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//            btSocket.connect();
//            if(btSocket.isConnected()){
//                Log.v("Robot","came to bluetooth called");
//            }
//            return null;
//        }


}
