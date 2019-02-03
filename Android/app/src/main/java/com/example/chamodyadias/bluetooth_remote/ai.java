
package com.example.chamodyadias.bluetooth_remote;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;

public class ai extends AppCompatActivity implements AIListener {

    AIService aiService;
    TextView t;
    private ProgressDialog progress;
    public String message = "";
    TextToSpeech t1;
//    public String assetsDir = "";
//    public SpeechRecognizer recognizer;
//    public String KEYPHRASE = "nick";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);


        t = (TextView) findViewById(R.id.textView3);

        int permission = ContextCompat.checkSelfPermission(ai.this, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makerequest();
        }

        final AIConfiguration config = new AIConfiguration("07c0c43301fa411c8fefa33a7377e3d9",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(ai.this, config);

        aiService.setListener(ai.this);


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        tapToSpeak(getCurrentFocus());

    }

    protected void makerequest() {
        ActivityCompat.requestPermissions(ai.this, new String[]{Manifest.permission.RECORD_AUDIO}, 101);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }


    public void tapToSpeak(View view) {
        //Toast.makeText(ai.this,"HIII",Toast.LENGTH_SHORT).show();
        aiService.startListening();
    }

    public void playResponse(String message) {
        t1.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onResult(AIResponse res) {
        try {
            AIResponse[] response = {res};
            new task().execute(response);
        } catch (Exception e) {
            msg(e.toString());
            Log.v("Robot", e.toString());
        }
    }

    private class task extends AsyncTask<AIResponse, Void, Void> {
        protected boolean Success = false;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ai.this, "Getting Response...", "Please Wait...!");
        }

        @Override
        protected Void doInBackground(AIResponse... response) {
            Log.v("Robot", "AI RESPONSE" + response.toString());
            Result result1 = response[0].getResult();
            Fulfillment resultFulfillment = result1.getFulfillment();
            String message = resultFulfillment.getSpeech();
            try {
                //t.setText(result1.getResolvedQuery() + "Action" + result1.getAction());
                Log.v("Robot", "Speech" + message);
                playResponse(message);
                Intent resultIntent = new Intent();

                //Check for actions
                if (result1.getAction().contentEquals("Move")) {
                    //String parameters = (new Gson().toJson(result1.getParameters()));
                    String val = "true";
                    HashMap<String,JsonElement> param = result1.getParameters();
                    System.out.println("Robot" + param.size());
                    System.out.println("Robot" + param.keySet());
                    HashMap<String,String> hashMap = new HashMap<>();
                    Iterator entries = param.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        hashMap.put(entry.getKey().toString(),entry.getValue().toString());
                    }
                    resultIntent.putExtra("Extra_Has_Parameters", val);
                    resultIntent.putExtra("Extra_Action", result1.getAction());
                    resultIntent.putExtra("Extra_Parameters", hashMap);
                    Log.v("Robot", "From Move" + hashMap.toString());

                } else if (result1.getAction().contentEquals("Turn")) {
                    String val = "true";
                    HashMap<String,JsonElement> param = result1.getParameters();
                    System.out.println("Robot" + param.size());
                    System.out.println("Robot" + param.keySet());
                    HashMap<String,String> hashMap = new HashMap<>();
                    Iterator entries = param.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        hashMap.put(entry.getKey().toString(),entry.getValue().toString());
                    }
                    resultIntent.putExtra("Extra_Has_Parameters", val);
                    resultIntent.putExtra("Extra_Action", result1.getAction());
                    resultIntent.putExtra("Extra_Parameters", hashMap);
                    Log.v("Robot", "From Turn" + hashMap.toString());

                } else {
                    resultIntent.putExtra("Extra_Has_Parameters", "false");
                }
                //resultIntent.putExtra(Extra_Action,response);

                setResult(ai.RESULT_OK, resultIntent);
                Success = true;
//                if (progess.isShowing()) {
//                    progess.dismiss();
//                }
                finish();
        /*
        Intent callAction = new Intent(ai.this, remote.class);
        callAction.putExtra(Extra_Action,result1.getAction());
        startActivity(callAction);
        //finish();
        */

            } catch (Exception e) {
                //msg(e.toString());
                Log.v("Robot", e.toString());
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!Success) {
                //msg("Error");
                //finish();
            } else {
                //msg("Done!");
                //finish();
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        }
    }


    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aiService != null) {
            aiService.stopListening();
        }
    }
}
