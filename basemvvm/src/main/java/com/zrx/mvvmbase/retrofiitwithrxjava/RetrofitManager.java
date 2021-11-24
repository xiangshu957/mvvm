package com.zrx.mvvmbase.retrofiitwithrxjava;

import android.os.Environment;
import android.text.TextUtils;

import com.zrx.mvvmbase.retrofiitwithrxjava.interceptor.HttpLogInterceptor;
import com.zrx.mvvmbase.retrofiitwithrxjava.interceptor.NetCacheInterceptor;
import com.zrx.mvvmbase.retrofiitwithrxjava.interceptor.OfflineCacheInterceptor;
import com.zrx.mvvmbase.utils.LogUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:
 */
public class RetrofitManager {

    private static RetrofitManager retrofitManager;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private static String baseUrl;
    private static Class<?> retrofitApiServiceClass;
    private RetrofitApiService retrofitApiService;

    private RetrofitManager() {
        initOkHttpClient();
        ignoreSSLCheck();
        initRetrofit();
    }

    public static RetrofitManager getInstance(){
        if (TextUtils.isEmpty(baseUrl)){
            LogUtils.e(RetrofitManager.class.getSimpleName(),"baseUrl为空，无法初始化");
            return null;
        }
        if (retrofitManager == null){
            synchronized (RetrofitManager.class){
                if (retrofitManager == null){
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    public static void getInstance(String baseUrl, Class<?> clazz){
        if (retrofitManager == null){
            synchronized (RetrofitManager.class){
                if (retrofitManager == null){
                    RetrofitManager.baseUrl = baseUrl;
                    RetrofitManager.retrofitApiServiceClass = clazz;
                    retrofitManager = new RetrofitManager();
                }else {
                    retrofitManager = null;
                    getInstance(baseUrl, clazz);
                }
            }
        }else {
            retrofitManager = null;
            getInstance(baseUrl, clazz);
        }
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitApiService = (RetrofitApiService) retrofit.create(retrofitApiServiceClass);
    }

    private void ignoreSSLCheck() {
        //忽略ssl校验
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null,new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }},new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = (s, sslSession) -> true;

        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(okHttpClient,hv1);
            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(okHttpClient,sc.getSocketFactory());
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void initOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/okhttp_cache"), 50 * 1024 * 1024))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(new HttpLogInterceptor())
                .addInterceptor(OfflineCacheInterceptor.getInstance())
                .addNetworkInterceptor(NetCacheInterceptor.getInstance())
                .build();
    }

    public RetrofitApiService getRetrofitApiService() {
        if (retrofitManager == null){
            retrofitManager = getInstance();
        }
        assert retrofitManager != null;
        return retrofitManager.retrofitApiService;
    }
}
