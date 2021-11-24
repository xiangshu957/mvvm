package com.zrx.mvvm.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.zrx.mvvm.bean.RepositoryImpl;
import com.zrx.mvvmbase.base.BaseViewModel;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public class MainViewModel extends BaseViewModel<RepositoryImpl> {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }


}
