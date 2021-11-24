package com.zrx.mvvm.application;

import android.app.Application;
import android.content.Context;

import com.zrx.mvvm.api.RetrofitApi;
import com.zrx.mvvm.common.SysCommon;
import com.zrx.mvvmbase.retrofiitwithrxjava.RetrofitManager;
import com.zrx.mvvmbase.utils.PreferenceUtil;


/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/18
 * DES:
 */
public class MyApplication extends Application {

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        PreferenceUtil.getInstance().init(this, "test_sp");
        RetrofitManager.getInstance(SysCommon.BASE_URL, RetrofitApi.class);
    }


    public static Context getContext() {
        return context;
    }
}
