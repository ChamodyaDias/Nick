package com.example.chamodyadias.bluetooth_remote.FaceDetection.facedetection;

import android.util.Log;

import java.util.HashMap;

public class FacePosition {
        public HashMap<String,Float> map = new HashMap<>();

        private onValueChangeListener valueChangeListener;

        public HashMap<String,Float> isInitialised() {
            return map;
        }

        public void setVariable(HashMap<String,Float> values ) {
            Log.v("Robot","Called");
            map = values;
            if (valueChangeListener != null) valueChangeListener.onChange();
        }

        public onValueChangeListener getValueChangeListener() {
            return valueChangeListener;
        }

        public void setValueChangeListener(onValueChangeListener valueChangeListener) {
            this.valueChangeListener = valueChangeListener;
        }

        public interface onValueChangeListener {
            void onChange();
        }

}


