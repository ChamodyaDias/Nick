package com.example.chamodyadias.bluetooth_remote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class DeviceList extends AppCompatActivity {
    //Widgets
    Button btnPaired;
    ListView deviceList;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevice = null;
    private OutputStream outStream = null;
    public static String EXTRA_TEXT = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        btnPaired = (Button)findViewById(R.id.button);
        deviceList = (ListView)findViewById(R.id.ListView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null){
            Toast.makeText(getApplicationContext(),"Bluetooth Device not available",Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else{
            if(myBluetooth != null && myBluetooth.isEnabled()){
                Toast.makeText(getApplicationContext(),"Came here",Toast.LENGTH_LONG).show();
            }
            else{
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }

        btnPaired.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"paired devices called",Toast.LENGTH_LONG).show();
                pairedDevicesList();
            }
        });

    }

    private void pairedDevicesList(){
        pairedDevice = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevice.size()>0){
            for(BluetoothDevice bt : pairedDevice){
                list.add(bt.getName() + "\n" + bt.getAddress()); //get device name and address
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"No Paired Bluetooth Devices Found",Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            //Toast.makeText(getApplicationContext(),"Activity Called",Toast.LENGTH_LONG).show();
            //get device MAC address

            String info = ((TextView)v).getText().toString();
            String address = info.substring(info.length()-17);

            Intent i = new Intent(getApplicationContext(),remote.class);

            i.putExtra(EXTRA_TEXT,address);
            //String Speech = i.getStringExtra(DeviceList.ETRA_ADDRESS);
            //Toast.makeText(getApplicationContext(),Speech,Toast.LENGTH_LONG).show();
            startActivity(i);
        }
    };

}

