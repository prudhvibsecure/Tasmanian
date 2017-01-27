package com.tasmanian.properties;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by w7 on 22/08/2016.
 */
public class SplashScreen extends Activity {
    private Runnable mUpdateTimeTask = null;
    private Handler mHandler = new Handler();

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                break;
            case DisplayMetrics.DENSITY_HIGH:
                break;
        }
        initiate();
        Toast.makeText(getApplicationContext(),"Version: "+applicationVName(),Toast.LENGTH_LONG).show();
    }

    public String applicationVName() {
        String versionName = "";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void launchActivty() {
        SplashScreen.this.finish();
        Intent mainIntent = new Intent(SplashScreen.this, TasmanianProperty.class);
        SplashScreen.this.startActivity(mainIntent);
    }

    private void initiate() {

        try {
            mUpdateTimeTask = new Runnable() {
                public void run() {
                    launchActivty();
                }
            };
            mHandler.postDelayed(mUpdateTimeTask, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
