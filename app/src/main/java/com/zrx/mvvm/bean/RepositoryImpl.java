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

    //这里放的是所有的网络请求操作

    public MutableLiveData<Resource<List<TestBean>>> getData(){
        MutableLiveData<Resource<List<TestBean>>> liveData = new MutableLiveData<>();
        //observeGo()是BaseModel中封装的方法，大家可以点进去看看
        return observeGo(((RetrofitApi)getApiService()).getData(),liveData);
    }

}
