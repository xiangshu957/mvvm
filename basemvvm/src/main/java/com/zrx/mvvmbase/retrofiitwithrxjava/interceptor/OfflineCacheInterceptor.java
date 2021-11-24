package com.zrx.mvvmbase.retrofiitwithrxjava.interceptor;

import android.content.Context;

import com.zrx.mvvmbase.utils.networks.NetWorkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:
 */
public class OfflineCacheInterceptor implements Interceptor {

    private static OfflineCacheInterceptor offlineCacheInterceptor;
    private int offlineCacheTime;
    private Context context;

    private OfflineCacheInterceptor(){

    }

    public static OfflineCacheInterceptor getInstance(){
        if (offlineCacheInterceptor == null){
            synchronized (OfflineCacheInterceptor.class){
                if (offlineCacheInterceptor == null){
                    offlineCacheInterceptor = new OfflineCacheInterceptor();
                }
            }
        }
        return offlineCacheInterceptor;
    }

    public void setOfflineCacheTime(int offlineCacheTime) {
        this.offlineCacheTime = offlineCacheTime;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtils.isNetWorkConnected(context)){
            if (offlineCacheTime != 0){
                int temp = offlineCacheTime;
                request = request.newBuilder()
                        .header("Cache-Control","public, only-if-cached, max-stale="+temp)
                        .build();
                offlineCacheTime = 0;
            }else {
                request = request.newBuilder()
                        .header("Cache-Control","no-cache")
                        .build();
            }
        }
        return chain.proceed(request);
    }
}
