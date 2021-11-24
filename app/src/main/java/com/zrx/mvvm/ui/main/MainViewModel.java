package com.zrx.mvvm.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zrx.mvvm.bean.RepositoryImpl;
import com.zrx.mvvm.model.TestBean;
import com.zrx.mvvmbase.base.BaseViewModel;
import com.zrx.mvvmbase.base.bean.Resource;

import java.util.List;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public class MainViewModel extends BaseViewModel<RepositoryImpl> {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Resource<List<TestBean>>> getData() {
        return getRepository().getData();
    }
}
