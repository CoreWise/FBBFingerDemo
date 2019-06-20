package com.cw.fbb;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;



public class MyApplication extends Application {


    private static MyApplication app;


    public static final String RootTag="CW";

    public static final String COMMON_PATH = "/sdcard/AratekTrustFinger";
    public static final String DB_PATH = "/sdcard/AratekTrustFinger/DB";
    public static final boolean SAVE_TO_SDCARD = false;

    public static final String SHAREDPREFERENCE_NAME = "sharedpreference_trustfinger";

    public static final String AUTO_SAVE = "auto_save";
    public static final String FEATURE_PATH = "feature_path";
    public static final String FEATURE_FORMAT = "feature_format";
    public static final String IMAGE_FORMAT = "image_format";
    public static final String CAPTURE_IMAGE_QUALITY_THRESHOLD = "capture_image_quality_threshold";
    public static final String ENROLL_IMAGE_QUALITY_THRESHOLD = "enroll_image_quality_threshold";
    public static final String VERIFY_IMAGE_QUALITY_THRESHOLD = "verify_image_quality_threshold";
    public static final String VERIFY_SECURITY_LEVEL = "verify_security_level";
    public static final String IDENTIFY_IMAGE_QUALITY_THRESHOLD = "identify_image_quality_threshold";
    public static final String IDENTIFY_SECURITY_LEVEL = "identify_security_level";







    public static MyApplication getInstance() {
        return app;
    }

        private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();
        app=this;

        sp = getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        put(AUTO_SAVE, true);
        put( FEATURE_PATH, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "AratekTrustFinger" + File.separator +
                "FingerData");
        put( FEATURE_FORMAT, "Aratek Bione");
        put( IMAGE_FORMAT, "BMP");
        put( CAPTURE_IMAGE_QUALITY_THRESHOLD, "50");
        put( ENROLL_IMAGE_QUALITY_THRESHOLD, "50");
        put( VERIFY_IMAGE_QUALITY_THRESHOLD, "50");
        put( VERIFY_SECURITY_LEVEL, "Level4");
        put( IDENTIFY_IMAGE_QUALITY_THRESHOLD, "50");
        put( IDENTIFY_SECURITY_LEVEL, "Level4");
        editor.apply();


    }







    public void put(String key, String value) {
        if (!sp.contains(key)) {
            editor.putString(key, value);
        }

    }

    public void put(String key, boolean value) {
        if (!sp.contains(key)) {
            editor.putBoolean(key, value);
        }
    }

    public SharedPreferences getSp() {
        return sp;
    }



    private ProgressDialog progressDialog;


    public void showProgressDialog(Context context, String message) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void cancleProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }

    }


}
