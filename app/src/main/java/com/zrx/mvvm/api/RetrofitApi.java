package com.zrx.mvvm.api;

import com.zrx.mvvm.bean.ResponseModelCustom;
import com.zrx.mvvm.common.SysCommon;
import com.zrx.mvvm.model.TestBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public interface RetrofitApi {

    @POST(SysCommon.YM_RUL)
    Observable<ResponseModelCustom<List<TestBean>>> getData();

}
