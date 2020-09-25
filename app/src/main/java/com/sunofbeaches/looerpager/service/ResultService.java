package com.sunofbeaches.looerpager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qqaplication.ILoginInterface;


public class ResultService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ILoginInterface.Stub() {
            @Override
            public void login() throws RemoteException {

            }

            @Override
            public void loginCallback(boolean loginStatus, String loginUser) throws RemoteException {
                // 不用挂起
                Log.e("netbase >>>>", "loginStatus:" + loginStatus +", loginUser:" + loginUser);
            }
        };
    }
}
