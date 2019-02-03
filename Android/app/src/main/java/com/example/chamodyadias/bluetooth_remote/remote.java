package com.example.chamodyadias.bluetooth_remote;

import android.Manifest;
import android.app.ActionBar;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.widget.Toolbar;

import com.example.chamodyadias.bluetooth_remote.FaceDetection.GraphicOverlay;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.LivePreviewActivity;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection.FaceGraphic;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection.FacePosition;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection.FacePositionValueListener;
import com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection.NameViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


//public class remote extends AppCompatActivity implements RecognitionListener {
public class remote extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {
    Button ready;
    Button faceDetect;
    String address = null;
    TextView test = null;
    private ProgressDialog progess;
    BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;
    private Boolean isBtconnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private DroidSpeech droidSpeech;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        ready = (Button) findViewById(R.id.button2);
        faceDetect = (Button) findViewById(R.id.button3);

        Intent called_intent = getIntent();

        //Try to connect to Bluetooth Device
        Bundle m = getIntent().getExtras();
        if (m.containsKey("device_address")) {
            msg("Device address intent");
            address = called_intent.getStringExtra(DeviceList.EXTRA_TEXT);
            new ConnectBT().execute();
        }

        // Initializing the droid speech and setting the listener
        droidSpeech = new DroidSpeech(this, getFragmentManager());
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setShowRecognitionProgressView(true);
        droidSpeech.setOneStepResultVerify(false);
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
        droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE);
        droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

//        fp.setCustomObjectListener(new FacePositionValueListener() {
//            @Override
//            public void onValuesChanged(HashMap<String, Float> map) {
//                msg("Listener event called");
//                msg(map.toString());
//
//            }
//        });
        // Get the ViewModel.



    }

    public void playResponse(String message) {
        t1.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }


    //Container for action
    private class actionParameters {
        String action = "";
        HashMap<String,String> parameters = new HashMap<String, String>();

        actionParameters(String act, HashMap para) {
            this.action = act;
            Iterator entries = para.entrySet().iterator();
            while (entries.hasNext()){
                Map.Entry entry = (Map.Entry) entries.next();
                this.parameters.put(entry.getKey().toString(),entry.getValue().toString());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1): {
                try {
                    if (resultCode == ai.RESULT_OK) {
                        Log.v("Robot", "came to result");
                        if (data.hasExtra("Extra_Action")) {
                            if (!data.getStringExtra("Extra_Has_Parameters").equals(null)) {
                                if (data.getStringExtra("Extra_Has_Parameters").contentEquals("true")) {
                                    HashMap<String,String> param = (HashMap<String,String>) data.getSerializableExtra("Extra_Parameters");
                                    //Object para = new Gson().fromJson(data.getStringExtra("Extra_Parameters"), Object.class);
                                    Log.v("Robot", "Result parameters = " + param.toString());
                                    actionParameters action_tobe_called = new actionParameters(data.getStringExtra("Extra_Action"), param);
                                    new callAction().execute(action_tobe_called);
                                }
                                else {
                                    Log.v("Robot", "no parameters found");
                                    droidSpeech.startDroidSpeechRecognition();
                                    return;
                                }
                            } else {
                                Log.v("Robot", "parameters equal null");
                                try{
                                    Thread.sleep(3000);
                                    droidSpeech.startDroidSpeechRecognition();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Log.v("Robot", "no such etra");
                            try{
                                Thread.sleep(3000);
                                droidSpeech.startDroidSpeechRecognition();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }


                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void readyClick(View view) {
        Intent i = new Intent(this, ai.class);
        startActivityForResult(i, 1);
    }

    public void faceDetectClick(View view) {
        Intent i = new Intent(this, LivePreviewActivity.class);
        startActivityForResult(i, 1);
    }


    //Async Task to complete the action
    public class callAction extends AsyncTask<actionParameters, Void, Void> {
        //msg("came here");
        //msg(action);
        public boolean Success = false;

        @Override
        protected void onPreExecute() {
            Log.v("Robot", "Called Action");
            progess = ProgressDialog.show(remote.this, "Executing...", "Please Wait...!");
        }

        @Override
        protected Void doInBackground(actionParameters... act) {
            if (act[0] != null && !act[0].action.isEmpty()) {
                String resultAction = act[0].action;
                HashMap<String,String> resultParameters = act[0].parameters;
//                Log.v("Robot", "action = " + resultAction);
                Log.v("Robot", "Parameters = " + resultParameters.get("Direction"));
                try {

                    if (resultAction.contentEquals("Move") && resultParameters.containsKey("DirectionFB")) {
                        //msg("came here");
                        if(resultParameters.get("DirectionFB").equals("\"Forward\"")) {
                            Log.v("Robot", "action = " + resultAction);
                            Log.v("Robot", "Parameters = " + resultParameters.toString());
                            if(forward()){
                                Success = true;
                            }
                        }
                        else if(resultParameters.get("DirectionFB").equals("\"Backward\"")) {
                            Log.v("Robot", "action = " + resultAction);
                            Log.v("Robot", "Parameters = " + resultParameters.toString());
                            if(backward()){
                                Success = true;
                            }
                        }
                    } else if (resultAction.contentEquals("Turn") && resultParameters.containsKey("DirectionLR")) {
                        //msg("came here");
                        if(resultParameters.get("DirectionLR").equals("\"Left\"")) {
                            if(turnLeft()){
                                Success = true;
                            };

                        }
                        else if(resultParameters.get("DirectionLR").equals("\"Right\"")) {
                            if(turnRight()){
                                Success = true;
                            };
                        }
                    } else {
                        //msg("couldnt call function");
                        Success = false;


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //msg("couldnt call function");
                Success = false;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!Success) {
                //msg("Error");
                //finish();
                playResponse("Command Execution Failed");
                if (progess.isShowing()) {
                    progess.dismiss();
                }
                try {
                    Thread.sleep(3000);
                    droidSpeech.startDroidSpeechRecognition();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Log.v("Robot", "came to start listening");
            } else {
                //msg("Done!");
                //finish();
                playResponse("Command Execution Successful");
                if (progess.isShowing()) {
                    progess.dismiss();
                }
                try {
                    Thread.sleep(3000);
                    droidSpeech.startDroidSpeechRecognition();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //recognizer.startListening("keywordSearch");
                //Log.v("Robot", "came to start listening");

            }
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progess = ProgressDialog.show(remote.this, "Connecting...", "Please Wait...!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtconnected) {

                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                //msg("failed");
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                if (progess.isShowing()) {
                    progess.dismiss();
                }
                //finish();
            } else {
                msg("Connected");
                if (progess.isShowing()) {
                    progess.dismiss();
                }
                //Connection Successfull. Start Listening for commands.
                Log.v("Robot", "came to start listening");
                droidSpeech.startDroidSpeechRecognition();
            }
        }
    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Error");
            }
        }
        finish();
    }

    private boolean forward() {
//        msg("Going forward");
//        Log.v("Robot", "Parameters = " + resultParameters.toString());
        boolean Success = false;
        if (btSocket != null) {
            try {
                Log.v("Robot", "Forward function called =");
//                for (int i=0;i<=5;i++) {
                btSocket.getOutputStream().write("F".toString().getBytes());
//                }
                btSocket.getOutputStream().write(" ".toString().getBytes());
                Success = true;
            } catch (IOException e) {
                //msg("Error");
                Success = false;
            }
        }
        return Success;
    }

    private boolean backward() {
//        msg("Going forward");
        boolean Success = false;
        if (btSocket != null) {
            try {
                Log.v("Robot", "Backward function called =");
//                for (int i=0;i<=5;i++) {
                btSocket.getOutputStream().write("B".toString().getBytes());
//                }
                btSocket.getOutputStream().write(" ".toString().getBytes());
                Success = true;
            } catch (IOException e) {
//                msg("Error");
                Success = false;
            }
        }return Success;
    }

    private boolean turnLeft() {
//        msg("Going forward");
        boolean Success = false;
        if (btSocket != null) {
            try {
                Log.v("Robot", "Backward function called =");
//                for (int i=0;i<=5;i++) {
                    btSocket.getOutputStream().write("L".toString().getBytes());
//                }
                btSocket.getOutputStream().write(" ".toString().getBytes());
                Success = true;
            } catch (IOException e) {
//                msg("Error");
                Success = false;
            }
        }return Success;
    }

    private boolean turnRight() {
//        msg("Going forward");
        boolean Success = false;
        if (btSocket != null) {
            try {
                Log.v("Robot", "Backward function called =");
//                for (int i=0;i<=5;i++) {
                    btSocket.getOutputStream().write("R".toString().getBytes());
//                }
                btSocket.getOutputStream().write(" ".toString().getBytes());
                Success = true;
            } catch (IOException e) {
//                msg("Error");
                Success = false;
            }
        }return Success;
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    // MARK: DroidSpeechListener Methods

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        Log.v("Robot", "Current speech language = " + currentSpeechLanguage);
        Log.v("Robot", "Supported speech languages = " + supportedSpeechLanguages.toString());

//        if(supportedSpeechLanguages.contains("ta-IN"))
//        {
//            // Setting the droid speech preferred language as tamil if found
//            droidSpeech.setPreferredLanguage("ta-IN");
//
//            // Setting the confirm and retry text in tamil
//            droidSpeech.setOneStepVerifyConfirmText("உறுதிப்படுத்த");
//            droidSpeech.setOneStepVerifyRetryText("மீண்டும் முயற்சிக்க");
//        }
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
        // Log.v(TAG, "Rms change value = " + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {
        Log.v("Robot", "Live speech result = " + liveSpeechResult);

    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        // Setting the final speech results
//        this.finalSpeechResult.setText(finalSpeechResult);

        if (droidSpeech.getContinuousSpeechRecognition()) {
            int[] colorPallets1 = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
            int[] colorPallets2 = new int[]{Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN};

            // Setting random color pallets to the recognition progress view
            droidSpeech.setRecognitionProgressViewColors(new Random().nextInt(2) == 0 ? colorPallets1 : colorPallets2);
            if (finalSpeechResult.contentEquals("Nick hello") || finalSpeechResult.contentEquals("Nick listen") || finalSpeechResult.contentEquals("robot Nick") || finalSpeechResult.contentEquals("robot listen")) {
                droidSpeech.closeDroidSpeechOperations();
                playResponse("I am listening");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readyClick(getCurrentFocus());
            }
        } else if(finalSpeechResult.contentEquals("goodbye")) {
//            stop.setVisibility(View.GONE);
//            start.setVisibility(View.VISIBLE);
            droidSpeech.closeDroidSpeechOperations();
            playResponse("See you soon! Bye");
            try {
                Thread.sleep(1000);
                //finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDroidSpeechClosedByUser() {
//        stop.setVisibility(View.GONE);
//        start.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

//        stop.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                // Stop listening
//                stop.performClick();
//            }
//        });
    }

    // MARK: DroidSpeechPermissionsListener Method

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {
//        if(audioPermissionGiven)
//        {
//            start.post(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    // Start listening
//                    start.performClick();
//                }
//            });
//        }
//        else
//        {
//            if(errorMsgIfAny != null)
//            {
//                // Permissions error
//                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show();
//            }
//
//            stop.post(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    // Stop listening
//                    stop.performClick();
//                }
//            });
//        }
    }

}


