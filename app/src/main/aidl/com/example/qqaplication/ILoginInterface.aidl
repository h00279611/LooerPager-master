// ILoginInterface.aidl
package com.example.qqaplication;

// Declare any non-default types here with import statements

interface ILoginInterface {

    // 登录
    void login();

    // 登录返回
    void loginCallback(boolean loginStatus, String loginUser);

}
