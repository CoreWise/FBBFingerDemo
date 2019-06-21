package com.cw.fbb;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.cw.serialportsdk.cw;

/**
 * 作者：李阳
 * 时间：2019/6/12
 * 描述：此处只是判断机型设置相应的横竖屏，对二次开发没啥影响
 */
public abstract class BaseActivity extends FragmentActivity {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //先判断机器型号，再确定是否横屏或者竖屏，U1，U3竖屏，U8 A370横屏


        switch (cw.getDeviceModel()) {

            case cw.Device_U3:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                break;
            case cw.Device_U8:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                break;
            case cw.Device_A370_CW20:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.general_tips);
            builder.setMessage(R.string.general_exit);

            //设置确定按钮
            builder.setNegativeButton(R.string.general_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            //设置取消按钮
            builder.setPositiveButton(R.string.general_no, null);
            //显示提示框
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
