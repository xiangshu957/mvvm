package com.zrx.mvvm.api;

import com.zrx.mvvm.common.SysCommon;
import com.zrx.mvvm.model.TestBean;
import com.zrx.mvvmbase.base.bean.Resource;
import com.zrx.mvvmbase.retrofiitwithrxjava.RetrofitApiService;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public interface RetrofitApi extends RetrofitApiService {

    @GET(SysCommon.YM_RUL)
    Observable<Resource<TestBean>> getData();

}
