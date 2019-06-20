package com.cw.fbb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.aratek.trustfinger.sdk.TrustFingerDevice;
import com.cw.fbb.MainActivity;
import com.cw.fbb.MyApplication;


public abstract class BaseFragment extends Fragment {
    protected TrustFingerDevice mTrustFingerDevice;
    protected MyApplication mApp;

    protected boolean viewCreated = false;


    public abstract void setDatas(TrustFingerDevice device);

    public void handleMsg(String msg, int color) {
        ((MainActivity) getActivity()).handleMsg(msg, color);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (MyApplication) getActivity().getApplication();
    }


    protected <T> void saveParameterToPreferences(String key , T value){
        if (value instanceof String || value == null){
            mApp.getSp().edit().putString(key , (String) value).apply();
        }else{
            mApp.getSp().edit().putBoolean(key , (Boolean) value).apply();
        }
    }

    protected <T> Object getParameterFromPreferences(String key , T defaultValue){
        if (defaultValue instanceof String || defaultValue == null){
            return mApp.getSp().getString(key , (String) defaultValue);
        }else{
            return mApp.getSp().getBoolean(key , (Boolean) defaultValue);
        }
    }
}
