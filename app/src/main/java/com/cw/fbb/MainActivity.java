package com.cw.fbb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.aratek.trustfinger.sdk.DeviceListener;
import com.aratek.trustfinger.sdk.DeviceOpenListener;
import com.aratek.trustfinger.sdk.TrustFinger;
import com.aratek.trustfinger.sdk.TrustFingerDevice;
import com.aratek.trustfinger.sdk.TrustFingerException;

import com.cw.fbb.adapter.MyPagerAdapter;
import com.cw.fbb.fragment.CaptureFragment;
import com.cw.fbb.fragment.EnrollFragment;
import com.cw.fbb.fragment.IdentifyFragment;
import com.cw.fbb.fragment.VerifyFragment;
import com.cw.fbb.utils.MediaPlayerHelper;
import com.cw.fpgabsdk.USBFingerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements DeviceOpenListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TextView mTextView_msg;
    private TextView mTextView_version;

    private TrustFinger mTrustFinger;
    protected TrustFingerDevice mTrustFingerDevice;



    private List<Fragment> fragmnts = new ArrayList<Fragment>();
    private CaptureFragment mCaptureFragment;
    private EnrollFragment mEnrollFragment;
    private VerifyFragment mVerifyFragment;
    private IdentifyFragment mIdentifyFragment;
    private String[] titles;
    private Handler handler = new Handler();
    private int mDeviceId = 0;
    private boolean isDeviceOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        init();
    }

    private void init() {
        initDatas();
        File file = new File(MyApplication.COMMON_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }



    private void findViews() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mTextView_msg = findViewById(R.id.tv_msg);
        mTextView_version = findViewById(R.id.tv_version);
    }

    private void initTrustFinger() {
        try {
            mTrustFinger = TrustFinger.getInstance(this.getApplicationContext());
            mTrustFinger.initialize();
            mTrustFinger.setDeviceListener(new DeviceListener() {
                @Override
                public void deviceAttached(List<String> deviceList) {
                    handleMsg("Device atached!", Color.BLACK);
                }

                @Override
                public void deviceDetached(List<String> deviceList) {
                    isDeviceOpened = false;
                    setFragmentDatas(null);
                    handleMsg("Device detached!", Color.RED);
                }
            });

        } catch (TrustFingerException e) {
            handleMsg("TrustFinger getInstance Exception: " + e.getType().toString() + "", Color.RED);

            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            showAlertDialog(true, "The system does not support simultaneous access to two devices" + ".");
        }

    }


    private void initDatas() {

        isDeviceOpened = false;

        mCaptureFragment = new CaptureFragment();
        mEnrollFragment = new EnrollFragment();
        mVerifyFragment = new VerifyFragment();
        mIdentifyFragment = new IdentifyFragment();

        fragmnts.add(mCaptureFragment);
        fragmnts.add(mEnrollFragment);
        fragmnts.add(mVerifyFragment);
        fragmnts.add(mIdentifyFragment);

        titles = getResources().getStringArray(R.array.tabs_name);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmnts, titles));
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    private void showAlertDialog(final boolean isError, String msg) throws TrustFingerException {
        MediaPlayerHelper.payMedia(this, R.raw.no_fingerprint_device_detected);
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Error")
                .setMessage(msg)
                .setNeutralButton("Redetect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isError) {
                            initTrustFinger();
                        } else {
                            showAlertDialog(false, "No fingerprint device detected!");
                        }
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();
    }

    private void setFragmentDatas(TrustFingerDevice mTrustFingerDevice) {
        if (mCaptureFragment != null) {
            mCaptureFragment.setDatas(mTrustFingerDevice);
        }
        if (mEnrollFragment != null) {
            mEnrollFragment.setDatas(mTrustFingerDevice);
        }
        if (mVerifyFragment != null) {
            mVerifyFragment.setDatas(mTrustFingerDevice);
        }
        if (mIdentifyFragment != null) {
            mIdentifyFragment.setDatas(mTrustFingerDevice);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.getInstance().showProgressDialog(this, "Loading");
        USBFingerManager.getInstance(this).openUSB(new USBFingerManager.OnUSBFingerListener() {
            @Override
            public void onOpenUSBFingerSuccess(String device, UsbManager mUsbManager, UsbDevice mDevice) {
                MyApplication.getInstance().cancleProgressDialog();
                initTrustFinger();
                mDeviceId = 0;
                try {
                    mTrustFinger.openDevice(mDeviceId, MainActivity.this);
                } catch (TrustFingerException e) {
                    handleMsg("Device open exception:" + e.getType().toString() + "", Color.RED);
                    e.printStackTrace();
                }
            }

            @Override
            public void onOpenUSBFingerFailure(String error) {
                MyApplication.getInstance().cancleProgressDialog();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mTrustFingerDevice!=null) {
                mTrustFingerDevice.close();
            }
            mTrustFingerDevice = null;
            isDeviceOpened = false;
            setFragmentDatas(null);
            handleMsg("Device closed!", Color.RED);
            if (mTrustFinger != null) {
                mTrustFinger.release();
            }
            USBFingerManager.getInstance(this).closeUSB();
        } catch (TrustFingerException e) {
            handleMsg("Device close exception : " + e.getType().toString() + "", Color.RED);
            e.printStackTrace();
        }
    }


    public void handleMsg(final String msg, final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mTextView_msg.setText(msg);
                mTextView_msg.setTextColor(color);
            }
        });
    }

    @Override
    public void openSuccess(TrustFingerDevice trustFingerDevice) {

        handleMsg("Device open success", Color.BLACK);
        mTrustFingerDevice = trustFingerDevice;
        isDeviceOpened = true;
        setFragmentDatas(mTrustFingerDevice);
    }

    @Override
    public void openFail(String s) {
        handleMsg("Device open fail", Color.RED);
        isDeviceOpened = false;
    }



}
