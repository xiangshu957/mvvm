package com.zrx.mvvmbase.retrofiitwithrxjava.interceptor;

import android.text.TextUtils;

import com.zrx.mvvmbase.utils.PreferenceUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:在有网络的情况下
 * 如果宅在网络有效期内则取缓存，否则请求网络
 * 重点：一般okhttp只缓存不太改变的数据适合get
 * (这里注意，如果一个接口设置了缓存30秒，下次请求这个接口的30秒内都会去取缓存，即使你设置0也不起效。因为缓存文件里的标识里已经有30秒的有效期)
 */
public class NetCacheInterceptor implements Interceptor {

    private static NetCacheInterceptor cacheInterceptor;

    private int onlineCacheTime;

    public static NetCacheInterceptor getInstance(){
        if (cacheInterceptor == null){
            synchronized (NetCacheInterceptor.class){
                if (cacheInterceptor == null){
                    cacheInterceptor = new NetCacheInterceptor();
                }
            }
        }
        return cacheInterceptor;
    }

    private NetCacheInterceptor(){

    }

    public void setOnlineCacheTime(int onlineCacheTime) {
        this.onlineCacheTime = onlineCacheTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        String token = (String) PreferenceUtil.getInstance().get("USER_TOKEN","");
        if (!TextUtils.isEmpty(token)){
            builder.addHeader("token",token).build();
        }
        request = builder.build();
        Response response = chain.proceed(request);
        List<String> list = response.headers().values("Token");
        if (list.size()>0){
            PreferenceUtil.getInstance().put("USER_TOKEN",list.get(0));
        }
        if (onlineCacheTime != 0){
            int temp = onlineCacheTime;
            Response response1 = response.newBuilder()
                    .header("Cache-Control","public, max-age="+temp)
                    .removeHeader("Pragma")
                    .build();
            onlineCacheTime = 0;
            return response1;
        }else {
            Response response1 = response.newBuilder()
                    .header("Cache-Control","no-cache")
                    .removeHeader("Pragma")
                    .build();
            return response1;
        }
    }
}
