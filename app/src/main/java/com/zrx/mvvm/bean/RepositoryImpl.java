package com.zrx.mvvm.bean;

import androidx.lifecycle.MutableLiveData;

import com.zrx.mvvm.api.RetrofitApi;
import com.zrx.mvvm.model.TestBean;
import com.zrx.mvvmbase.base.BaseModel;
import com.zrx.mvvmbase.base.bean.Resource;

import java.util.List;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public class RepositoryImpl extends BaseModel {

    public MutableLiveData<Resource<List<TestBean>>> getData(){
        MutableLiveData<Resource<List<TestBean>>> liveData = new MutableLiveData<>();
        return observeGo(((RetrofitApi)getApiService()).getData(),liveData);
    }

}
