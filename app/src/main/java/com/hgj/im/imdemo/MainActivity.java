package com.hgj.im.imdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.lifenumber.im.LnImManager;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Unbinder unbinder;
    private LnImManager lnImManager;
    private Handler mHandler = new ActivityHandler(this);

    private static class ActivityHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public ActivityHandler(MainActivity activity) {
            weakReference = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x100:
                    boolean loginResult = (boolean) msg.obj;
                    Log.e(TAG, "登录" + (loginResult ? "成功" : "失败"));
                    Log.e(TAG, "0x100");
                    break;
                case 0x101:
                    boolean logout = (boolean) msg.obj;
                    Log.e(TAG, "退出" + (logout ? "成功" : "失败"));
                    Log.e(TAG, "0x101");
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    /**
     * 用户注册
     */
    @OnClick({R.id.btn, R.id.disconnect})
    void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                login("hanguojing0724", "hanguojing0724");
                break;
            case R.id.disconnect:
                logout();
                break;
        }

    }


    private void logout() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lnImManager == null) {
                    lnImManager = LnImManager.getInstance();
                    lnImManager.initLnImManager("***.****.****", 5222);
                }
                boolean disconnect = lnImManager.logout();

                Message msg = Message.obtain();
                msg.what = 0x101;
                msg.obj = disconnect;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    private void login(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lnImManager == null) {
                    lnImManager = LnImManager.getInstance();
                    lnImManager.initLnImManager("***.****.****", 5222);
                }
                boolean login = lnImManager.login(username, password);
                Message msg = Message.obtain();
                msg.what = 0x100;
                msg.obj = login;
                mHandler.sendMessage(msg);

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
