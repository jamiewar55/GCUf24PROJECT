package com.example.bluetoothapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    public final static int MESSAGE_READ = 2;
    private final static int CONNECTING_STATUS = 3;
    private BluetoothAdapter mBTAdapter;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private BluetoothSocket mBTSocket = null;
    //AlertDialog dialog;
    ProgressDialog progressDialog;
    android.app.AlertDialog alertDialogAndroid;
    TextView textCartItemCount;
    boolean show_paired = false;
    boolean connected  = false;
    private long backPressedTime;
    private Toast backToast;
    String Toast_name  = "";
    String ms = "";

    float f1=0;
    float f2=0;
    float f3=0;
    float f4=0;
    float f5=0;
    float f6=0;

    String data_1 = "0";
    String data_2 = "0";
    String data_3 = "0";
    String data_4 = "0";
    String data_5 = "0";
    String data_6 = "0";
    private long update_Time;

    private static final String TAG = MainActivity.class.getSimpleName();
    /* Code used in requesting runtime permissions.*/
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /*Constant used in the location settings dialog.*/
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    /* The desired interval for location updates. Inexact. Updates may be more or less frequent.*/
    /*The fastest rate for active location updates. Exact. Updates will never be more frequent than this value.*/
    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /* Provides access to the Fused Location Provider API. */
    private FusedLocationProviderClient mFusedLocationClient;
    /*Provides access to the Location Settings API.*/
    private SettingsClient mSettingsClient;
    /*Stores parameters for requests to the FusedLocationProviderApi.*/
    private LocationRequest mLocationRequest;
    /* Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.*/
    private LocationSettingsRequest mLocationSettingsRequest;
    /*Callback for Location events.*/
    private LocationCallback mLocationCallback;
    /*Represents a geographical location.*/
    private Location mCurrentLocation;
    // UI Widgets.
    /*Tracks the status of the location updates request. Value changes when the user presses the Start Updates and Stop Updates buttons.*/
    private Boolean mRequestingLocationUpdates;
    /*Time when the location was updated represented as a String.*/
    private String mLastUpdateTime;
    boolean toggled = false;
    int total_time = 0;
    FloatingActionButton Fab2,Fab1;
    int state = 1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentOne fragment_one;
    FragmentTwo fragment_two;
    FragmentThree fragment_three;

    @SuppressLint("SetTextI18n")
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Handler handler = new Handler();
                if (result.getResultCode() == Activity.RESULT_OK) {
                    handler.postDelayed(() -> {
                        Toast_name = "Bluetooth permission granted";
                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                        show_paired = false;
                        showpaired();
                    }, 100);
                } else{
                    handler.postDelayed(() -> {
                        Toast_name = "Bluetooth permission denied!";
                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                    }, 100);
                }
            });

    @SuppressLint({"SetTextI18n", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sh0 = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sh0.edit();
        edit.clear();
        edit.apply();

        setContentView(R.layout.activity_main);

        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the following permissions.",
                    123, perms);
        }

        Fab1 = findViewById(R.id.fab1);
        Fab1.setOnClickListener(view ->
        {
            if(state==1)
            {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
                {
                    Toast.makeText(MainActivity.this, "Enable location !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else
                {
                    if (!checkPermissions())
                    {
                        requestPermissions();
                    }
                    else
                    {
                        state++;
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putBoolean("location_update", true);
                        myEdit.apply();
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragment_two=new FragmentTwo();
                        fragmentTransaction.replace(R.id.frameLayout_main,fragment_two);
                        fragmentTransaction.commit();
                    }
                }
            }
            else if(state==2)
            {
                state++;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragment_three=new FragmentThree();
                fragmentTransaction.replace(R.id.frameLayout_main,fragment_three);
                fragmentTransaction.commit();
            }
            else if(state==3)
            {
                state = 1;
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragment_one=new FragmentOne();
                fragmentTransaction.replace(R.id.frameLayout_main,fragment_one);
                fragmentTransaction.commit();
            }

        });

        Fab2 = findViewById(R.id.fab2);
        Fab2.setOnClickListener(view -> {
            if(connected)
            {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
                {
                    Toast.makeText(MainActivity.this, "Enable location !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
                else
                {
                    if (!checkPermissions())
                    {
                        requestPermissions();
                    }
                    else
                    {
                        toggled = !toggled;
                        SharedPreferences shp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEditp = shp.edit();
                        myEditp.putBoolean("toggled", toggled);
                        myEditp.apply();
                        if(toggled)
                        {
                            Toast.makeText(MainActivity.this, "Logging started", Toast.LENGTH_SHORT).show();
                            Fab2.setImageResource(R.drawable.baseline_pause_24);
                            mRequestingLocationUpdates = true;
                            startLocationUpdates();
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putBoolean("location_update", true);
                            myEdit.apply();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Logging stoped", Toast.LENGTH_SHORT).show();
                            Fab2.setImageResource(R.drawable.baseline_play_arrow_24);
                            if(mRequestingLocationUpdates)
                            {
                                stopLocationUpdates();
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putBoolean("location_update", false);
                                myEdit.apply();
                            }
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,  "Please connect with a Bluetooth device", Toast.LENGTH_SHORT).show();
            }
        });


        mBTArrayAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        mHandler = new Handler(Looper.getMainLooper()){
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_READ) {
                    String readMessage;
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);

                    if (update_Time + 100 <= System.currentTimeMillis()) {

                        if (!mRequestingLocationUpdates) {
                            mRequestingLocationUpdates = true;
                            startLocationUpdates();
                        }

                        try {
                            JSONObject obj = new JSONObject(readMessage);
                            data_1  = obj.getString("1");
                            data_2  = obj.getString("2");
                            data_3  = obj.getString("3");
                            data_4  = obj.getString("4");
                            data_5  = obj.getString("5");
                            data_6  = obj.getString("6");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Date today = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//formating according to my need
                        String date = formatter.format(today);
                        @SuppressLint("SimpleDateFormat")
                        DateFormat formatt = new SimpleDateFormat("HH:mm:ss");
                        Date dat = null;
                        try {
                            dat = formatt.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int h = (int) ((Objects.requireNonNull(dat)).getTime()/1000);

                        if(toggled)
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("s1", data_1);
                            myEdit.putString("s2", data_2);
                            myEdit.putString("s3", data_3);
                            myEdit.putString("s4", data_4);
                            myEdit.putString("s5", data_5);
                            myEdit.putString("s6", data_6);

                            double u1 = Double.parseDouble(data_1);
                            double u2 = Double.parseDouble(data_2);
                            double u3 = Double.parseDouble(data_3);
                            double u4 = Double.parseDouble(data_4);
                            double u5 = Double.parseDouble(data_5);
                            double u6 = Double.parseDouble(data_6);

                            f1 = Float.parseFloat(String.valueOf(u1));
                            f2 = Float.parseFloat(String.valueOf(u2));
                            f3 = Float.parseFloat(String.valueOf(u3));
                            f4 = Float.parseFloat(String.valueOf(u4));
                            f5 = Float.parseFloat(String.valueOf(u5));
                            f6 = Float.parseFloat(String.valueOf(u6));

                            myEdit.putFloat("f1"+ total_time, f1);
                            myEdit.putFloat("f2"+ total_time, f2);
                            myEdit.putFloat("f3"+ total_time, f3);
                            myEdit.putFloat("f4"+ total_time, f4);
                            myEdit.putFloat("f5"+ total_time, f5);
                            myEdit.putFloat("f6"+ total_time, f6);
                            myEdit.putString("d"+ total_time, date);
                            myEdit.putInt("u", total_time);
                            total_time = total_time +1;
                            myEdit.putString("time",String.valueOf(h));
                            myEdit.putString("h"+ total_time, String.valueOf(h));
                            myEdit.apply();
                        }

                    }
                    update_Time = System.currentTimeMillis();
                }

                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1){

                        ms = String.valueOf(msg.obj);
                        ms = ms.replaceAll("\n", "");

                        Toast_name = "Connected with " + ms;
                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                        if(alertDialogAndroid.isShowing())
                        {
                            alertDialogAndroid.dismiss();
                        }

                        connected = true;

                        if (!mRequestingLocationUpdates) {                                           //update Location after connected
                            mRequestingLocationUpdates = true;
                            startLocationUpdates();
                        }

                    }

                    else{
                        Toast_name = "Connection Failed!";
                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                        connected = false;
                    }
                    invalidateOptionsMenu();
                }
            }
        };

        if (mBTArrayAdapter == null) {
            Toast_name = "Bluetooth device not found!";
            Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
        }

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(BTReceiver, filter1);
        this.registerReceiver(BTReceiver, filter2);
        this.registerReceiver(BTReceiver, filter3);


        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        updateValuesFromBundle(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(MainActivity.this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragment_one=new FragmentOne();
        fragmentTransaction.replace(R.id.frameLayout_main,fragment_one);
        fragmentTransaction.commit();
        state = 1;
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
           if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
           if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
           if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateLocationUI();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(100)
                .setMaxUpdateDelayMillis(3000)
                .build();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime  = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {

        SharedPreferences shl = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEditl = shl.edit();
        myEditl.putBoolean("location_updated", false);
        myEditl.apply();

        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());

                    updateLocationUI();
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }


                    updateLocationUI();
                });
    }


    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("mLatitudeLabel", String.valueOf(mCurrentLocation.getLatitude()));
            myEdit.putString("mLongitudeLabel", String.valueOf(mCurrentLocation.getLongitude()));
            myEdit.apply();

            SharedPreferences shl = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditl = shl.edit();
            myEditl.putBoolean("location_updated", true);
            myEditl.apply();

        }
        invalidateOptionsMenu();
    }


    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task -> {
                    mRequestingLocationUpdates = false;
                    invalidateOptionsMenu();
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean location_user_update = sh.getBoolean("location_update",false);

        if(location_user_update)
        {
            mRequestingLocationUpdates = true;
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
            {
                if (mRequestingLocationUpdates && checkPermissions()) {
                    startLocationUpdates();
                }
                else if (!checkPermissions())
                {
                    requestPermissions();
                }
            }
        }
        else
        {
            stopLocationUpdates();
        }
        updateLocationUI();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean location_user_update = sh.getBoolean("location_update",false);
        if(!location_user_update) {
            stopLocationUpdates();
        }
    }

    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Allow "+getResources().getString(R.string.app_name)+" to access your location?")
                        .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE)
                        )
                        .setNegativeButton("cancel", (dialog, which) -> {
                            dialog.dismiss();
                            Toast.makeText(this, "Please allow this permission!", Toast.LENGTH_SHORT).show();
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    updateLocationUI();
                    break;
            }
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

        mBTArrayAdapter.clear();
        Set<BluetoothDevice> mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

        }
    }


    private final BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {//Do something if disconnected

                if(connected){
                    Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();

                    if(toggled)
                    {
                        toggled = false;
                        Fab2.setImageResource(R.drawable.baseline_play_arrow_24);

                        SharedPreferences shp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEditp = shp.edit();
                        myEditp.putBoolean("toggled", toggled);
                        myEditp.apply();
                    }
                }
                connected = false;
                invalidateOptionsMenu();
            }
        }
    };


    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NotNull Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private final AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(!connected){
                if(!mBTAdapter.isEnabled()) {
                    Toast_name = "Bluetooth is disabled";
                    Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
                progressDialog.setMessage("Connecting...");
                progressDialog.setTitle("Please wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                String info = ((TextView) view).getText().toString();
                final String address = info.substring(info.length() - 17);
                final String name = info.substring(0,info.length() - 17);

                new Thread()
                {
                    @Override
                    public void run() {
                        boolean fail = false;

                        BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                        try {
                            mBTSocket = createBluetoothSocket(device);
                        } catch (IOException e) {
                            fail = true;

                            Toast_name = "Socket creation failed";
                            Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();

                        }
                        // Establish the Bluetooth socket connection.
                        try {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                    return;
                                }
                            }
                            mBTSocket.connect();
                        } catch (IOException e) {
                            try {
                                fail = true;
                                mBTSocket.close();
                                mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                        .sendToTarget();
                            } catch (IOException e2) {
                                //insert code to deal with this
                                Toast_name = "Socket creation failed";
                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(!fail) {
                            mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                            mConnectedThread.start();

                            mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                    .sendToTarget();
                        }
                    }
                }.start();
            }
            else { // additional connection method prevent to ...
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure!")
                        .setMessage("Do you want to disconnect from the current device?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (mBTSocket != null) //If the btSocket is busy
                            {
                                try {
                                    mBTSocket.close(); //close connection

                                } catch (IOException e) {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }


                            if(!mBTAdapter.isEnabled()) {
                                Toast_name = "Bluetooth is disabled";
                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //dialog.dismiss();
                            progressDialog = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
                            progressDialog.setMessage("Connecting...");
                            progressDialog.setTitle("Please wait");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.show();
                            progressDialog.setCancelable(false);


                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {

                                    String info = ((TextView) view).getText().toString();
                                    final String address = info.substring(info.length() - 17);
                                    final String name = info.substring(0,info.length() - 17);


                                    new Thread()
                                    {
                                        @Override
                                        public void run() {
                                            boolean fail = false;

                                            BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                                            try {
                                                mBTSocket = createBluetoothSocket(device);
                                            } catch (IOException e) {
                                                fail = true;

                                                Toast_name = "Socket creation failed";
                                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();

                                            }
                                            // Establish the Bluetooth socket connection.
                                            try {
                                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                                                {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                                    {
                                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                                        return;
                                                    }
                                                }
                                                mBTSocket.connect();
                                            } catch (IOException e) {
                                                try {
                                                    fail = true;
                                                    mBTSocket.close();
                                                    mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                                            .sendToTarget();
                                                } catch (IOException e2) {
                                                    //insert code to deal with this
                                                    Toast_name = "Socket creation failed";
                                                    Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            if(!fail) {
                                                mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                                                mConnectedThread.start();

                                                mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                                        .sendToTarget();
                                            }
                                        }
                                    }.start();
                                }

                                public void onTick(long millisUntilFinished) {
                                }
                            }.start();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                //Creating dialog box
                AlertDialog dialog  = builder.create();
                dialog.show();
            }

        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            }
        }
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.option_menu, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);// return true so that the menu pop up is opened
        MenuItem settingsItem = menu.findItem(R.id.bluetooth);// set your desired icon here based on a flag if you like

        //final MenuItem menuItem = menu.findItem(R.id.bluetooth);
        View actionView = settingsItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        actionView.setOnClickListener(v -> onOptionsItemSelected(settingsItem));

        if (connected) {
            textCartItemCount.setBackgroundResource(R.drawable.ic_baseline_circle_24green);
        } else {
            textCartItemCount.setBackgroundResource(R.drawable.ic_baseline_circle_24red);
        }

        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.CSV) {

            final StringBuilder[] str1 = {new StringBuilder()};

            SharedPreferences shp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            int tot = shp.getInt("u", 0);

            for(int i = 0;i<tot;i++)
            {
                if(i==0)
                {
                    str1[0] .append("Time").append(",")  // date
                            .append("Motor Temp (°C)").append(",")
                            .append("Motor Temp (°C)").append(",")
                            .append("Motor RPM (rpm)").append(",")
                            .append("Wheel RPM (rpm)").append(",")  
                            .append("Voltage (V)").append(",")  // load
                            .append("Current (A)").append("\n")// rpm
                            .append(shp.getString("d" + i, "")).append(",")  // date
                            .append(shp.getFloat("f1" + i, 0)).append(",")   // temp1
                            .append(shp.getFloat("f2" + i, 0)).append(",")   // temp2
                            .append(shp.getFloat("f3" + i, 0)).append(",")   // rpm1
                            .append(shp.getFloat("f4" + i, 0)).append(",")   // rpm2
                            .append(shp.getFloat("f5" + i, 0)).append(",")   // Volt
                            .append(shp.getFloat("f6" + i, 0)).append("\n"); // amp
                }
                else
                {
                    str1[0]
                            .append(shp.getString("d" + i, "")).append(",")  // date
                            .append(shp.getFloat("f1" + i, 0)).append(",")   // temp1
                            .append(shp.getFloat("f2" + i, 0)).append(",")   // temp2
                            .append(shp.getFloat("f3" + i, 0)).append(",")   // rpm1
                            .append(shp.getFloat("f4" + i, 0)).append(",")   // rpm2
                            .append(shp.getFloat("f5" + i, 0)).append(",")   // Volt
                            .append(shp.getFloat("f6" + i, 0)).append("\n"); // amp
                }
            }

            if (!TextUtils.isEmpty(str1[0]))
            {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setMessage("Export data to CSV?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", (dialog, id2) -> new CountDownTimer(200, 1000) {
                            @SuppressLint("SetTextI18n")
                            public void onFinish() {

                                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                Date today = new Date();
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
                                String dateToStr = format.format(today);

                                if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                    requestStoragePermission();
                                } else {

                                    boolean success = true;

                                    String sub_folder = getResources().getString(R.string.app_name).replaceAll(" ","")+"_CSV";
                                    root = new File(root, sub_folder);

                                    if (!root.exists())
                                        success = root.mkdirs();
                                    if (success) {

                                        root = new File(root , "csv_"+dateToStr+".csv");

                                        String outFileName =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + getResources().getString(R.string.app_name).replaceAll(" ", "") + "_CSV" + File.separator;

                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref2", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("outFileName", outFileName);
                                        myEdit.apply();

                                        File finalRoot = root;

                                        ProgressDialog progressDialog2;
                                        progressDialog2 = new ProgressDialog(MainActivity.this, R.style.AlertDialogStyle);
                                        progressDialog2.setMessage("Please wait...");
                                        progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog2.show();
                                        progressDialog2.setCancelable(false);

                                        Handler handler = new Handler();
                                        String finalStr = str1[0].toString();
                                        handler.postDelayed(() -> {

                                            if (progressDialog2.isShowing()) {
                                                progressDialog2.cancel();
                                            }

                                            try
                                            {
                                                FileOutputStream fout = new FileOutputStream(finalRoot);
                                                fout.write(finalStr.getBytes());
                                                fout.close();

                                                Toast.makeText(MainActivity.this, "Export Completed", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(MainActivity.this, "CSV file can be founded at "+outFileName, Toast.LENGTH_LONG).show();

                                                str1[0] = new StringBuilder();
                                                //Toast.makeText(getActivity(), "Backup str1[0]"+str1[0], Toast.LENGTH_SHORT).show();
                                            }
                                            catch (FileNotFoundException e)
                                            {
                                                e.printStackTrace();
                                                boolean bool = false;
                                                try
                                                {
                                                    // try to create the file
                                                    bool = finalRoot.createNewFile();
                                                }
                                                catch (IOException ex)
                                                {
                                                    ex.printStackTrace();
                                                }
                                                if (!bool)
                                                {
                                                    throw new IllegalStateException("Failed to create image file");
                                                }
                                            }
                                            catch (IOException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }, 1000);
                                    }
                                    else {
                                       Toast.makeText(MainActivity.this, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            public void onTick(long millisUntilFinished) {
                            }
                        }.start())
                        .setNegativeButton("No", null)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this, "You have no data to export", Toast.LENGTH_SHORT).show();
            }
            
            return true;
        }

        else if(id == R.id.bluetooth) {

            new CountDownTimer(300, 1000) {

                public void onFinish() {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                            return;
                        }
                    }

                    mBTArrayAdapter.clear();
                    Set<BluetoothDevice> mPairedDevices = mBTAdapter.getBondedDevices();
                    if(mBTAdapter.isEnabled()) {
                        for (BluetoothDevice device : mPairedDevices)
                            mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                    }

                    if (mBTArrayAdapter == null) {
                        Toast_name = "Bluetooth device not found!";
                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                    }
                    else {

                        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                        View promptView = layoutInflater.inflate(R.layout.dialog_box, null);

                        ListView mDevicesListView = promptView.findViewById(R.id.devices_list_view);
                        mDevicesListView.setAdapter(mBTArrayAdapter);
                        mDevicesListView.setOnItemClickListener(mDeviceClickListener);
                        Button mdisconnect = promptView.findViewById(R.id.disconnect);
                        Button mclose      = promptView.findViewById(R.id.close);

                        ProgressBar scanInProg = promptView.findViewById(R.id.scanInProgress);

                        android.app.AlertDialog.Builder alertD = new android.app.AlertDialog.Builder(MainActivity.this);
                        alertD.setView(promptView);
                        alertD.setCancelable(true);

                        //android.app.AlertDialog alertDialogAndroid = alertD.create();
                        alertDialogAndroid = alertD.create();
                        alertDialogAndroid.show();
                        alertDialogAndroid.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Rect displayRectangle = new Rect();
                        Window window = MainActivity.this.getWindow();
                        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                        //alertDialogAndroid.getWindow().setLayout((int) (displayRectangle.width() * 0.90f), alertDialogAndroid.getWindow().getAttributes().height);
                        alertDialogAndroid.getWindow().setLayout((int) (displayRectangle.width() * 0.90f),  (int) (displayRectangle.height() * 0.95f));

                        SwitchCompat switchButton  = promptView.findViewById(R.id.switchButton);

                        switchButton.setChecked(mBTAdapter.isEnabled());


                        switchButton.setOnCheckedChangeListener((compoundButton, bChecked) -> {
                            if (bChecked) {
                                if (!mBTAdapter.isEnabled()) {
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    someActivityResultLauncher.launch(enableBtIntent);
                                    Toast_name = "Turning Bluetooth on ...";
                                } else {
                                    Toast_name = "Bluetooth is already enabled";
                                }
                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                            } else {
                                if (mBTSocket != null) //If the btSocket is busy
                                {
                                    try {
                                        mBTSocket.close(); //close connection

                                        connected = false;
                                        invalidateOptionsMenu();


                                    } catch (IOException e) {
                                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                                {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                                    {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                        return;
                                    }
                                }
                                mBTAdapter.disable();
                                mBTArrayAdapter.clear();
                            }
                        });

                        ImageButton refresh= promptView.findViewById(R.id.refresh);
                        refresh.setOnClickListener(v -> {

                            if(mBTAdapter.isEnabled()) {
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                        return;
                                    }
                                }
                                mBTArrayAdapter.clear();

                                refresh.setVisibility(View.INVISIBLE);
                                scanInProg.setVisibility(View.VISIBLE);

                                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

                                new CountDownTimer(2000, 1000) {
                                    public void onFinish() {
                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                                                return;
                                            }
                                        }
                                        mBTArrayAdapter.clear();
                                        Set<BluetoothDevice> mPairedDevices = mBTAdapter.getBondedDevices();
                                        if(mBTAdapter.isEnabled()) {
                                            for (BluetoothDevice device : mPairedDevices)
                                                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                        }

                                        refresh.setVisibility(View.VISIBLE);
                                        scanInProg.setVisibility(View.INVISIBLE);
                                    }
                                    public void onTick(long millisUntilFinished) {
                                    }
                                }.start();
                            }
                            else
                            {
                                Toast_name = "Bluetooth is disabled";
                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                            }

                        });



                        mdisconnect.setOnClickListener(v13 -> {
                            if (mBTSocket != null)
                            {
                                try {
                                    mBTSocket.close();

                                    if((mBTAdapter.isEnabled())&&(connected)){
                                        Toast_name = "Bluetooth device disconnected";
                                        Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                                        alertDialogAndroid.dismiss();

                                        if(toggled)
                                        {
                                            toggled = false;
                                            Fab2.setImageResource(R.drawable.baseline_play_arrow_24);

                                            SharedPreferences shp = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                            SharedPreferences.Editor myEditp = shp.edit();
                                            myEditp.putBoolean("toggled", toggled);                                // And saved the current state into  SharedPreferences
                                            myEditp.apply();
                                        }
                                    }

                                } catch (IOException e) {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(!connected) {
                                Toast_name = "Not connected";
                                Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                            }
                        });

                        mclose.setOnClickListener(v13 -> alertDialogAndroid.dismiss());
                    }
                }

                public void onTick(long millisUntilFinished) { }

            }.start();

            return true;
        }
        else  if(id == R.id.setting) {
            Handler handler = new Handler();
            handler.postDelayed(() -> startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS)), 300);
            return true;
        }

        else if(id == R.id.location_settings) {                                                     // when location icon on the option menu dropdown list is selected
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));   // go to location settings
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Allow " + getResources().getString(R.string.app_name) + " to access your storage?")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1)
                    )
                    .setNegativeButton("cancel", (dialog, which) -> {
                        dialog.dismiss();
                        Toast.makeText(this, "Please allow this permission!", Toast.LENGTH_SHORT).show();
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


    @SuppressLint("SetTextI18n")
    private void showpaired() {
        int x;
        if((mBTAdapter.isEnabled())&&(show_paired)) {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    return;
                }
            }

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    return;
                }
            }
            mBTArrayAdapter.clear();                                            // clear the listview items
            registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));  // also trigger the "blReceiver" Broadcast receiver

            Toast_name = "Scanning...";
            Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();

            x=2000;
        }
        else {
            x=100;
        }

        new CountDownTimer(x, 1000) {
            public void onFinish() {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }
                mBTArrayAdapter.clear();
                Set<BluetoothDevice> mPairedDevices = mBTAdapter.getBondedDevices();
                if(mBTAdapter.isEnabled()) {
                    for (BluetoothDevice device : mPairedDevices)
                        mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                else {
                    Toast_name = "Bluetooth is disabled. Turn on to show paired devices.";
                    Toast.makeText(MainActivity.this, Toast_name, Toast.LENGTH_SHORT).show();
                }
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean s1 = sh.getBoolean("state", false);

        if((connected)&&(!s1)){
            new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit the app?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, id) -> new CountDownTimer(1000, 1000) {
                        public void onFinish() {

                            if (mBTSocket != null) //If the btSocket is busy
                            {
                                try {
                                    mBTSocket.close(); //close connection

                                } catch (IOException e) {
                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            MainActivity.super.onBackPressed();
                        }
                        public void onTick(long millisUntilFinished) {
                        }
                    }.start())
                    .setNegativeButton("No", null)
                    .show();
        }
        else
        {
            if(!s1){
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    super.onBackPressed();
                    return;
                } else {
                    backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                } else {new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Permission Required")
                            .setMessage("This permission was already declined by you. Please open settings, go to \"Permissions\", and allow the permission.")
                            .setPositiveButton("Settings", (dialog, which) -> {
                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + this.getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            })
                            .setNegativeButton("cancel", (dialog, which) -> {
                                dialog.dismiss();
                                Toast.makeText(this, "Please allow this permission!", Toast.LENGTH_SHORT).show();

                            })
                            .create().show();
                }

            }
        }


        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(MainActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                } else {new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Permission Required")
                            .setMessage("This permission was already declined by you. Please open settings, go to \"Permissions\", and allow the location permission.")
                            .setPositiveButton("Settings", (dialog, which) -> {
                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                MainActivity.this.startActivity(i);
                            })
                            .setNegativeButton("cancel", (dialog, which) -> {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Please allow this permission!", Toast.LENGTH_SHORT).show();
                            })
                            .create().show();
                }
            }
        }
    }
}