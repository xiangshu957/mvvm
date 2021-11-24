package com.zrx.mvvmbase.base;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zrx.mvvmbase.base.bean.ParamBuilder;
import com.zrx.mvvmbase.base.bean.Resource;
import com.zrx.mvvmbase.base.bean.ResponseModel;
import com.zrx.mvvmbase.retrofiitwithrxjava.RetrofitApiService;
import com.zrx.mvvmbase.retrofiitwithrxjava.RetrofitManager;
import com.zrx.mvvmbase.retrofiitwithrxjava.downloadutils.DownFileUtils;
import com.zrx.mvvmbase.retrofiitwithrxjava.interceptor.NetCacheInterceptor;
import com.zrx.mvvmbase.retrofiitwithrxjava.interceptor.OfflineCacheInterceptor;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/21
 * DES:
 */
public class BaseModel {

    public LifecycleTransformer objectLifecycleTransformer;
    public ArrayList<String> onNetTags;

    public Object getApiService() {
        return RetrofitManager.getInstance().getRetrofitApiService();
    }

    public void setObjectLifecycleTransformer(LifecycleTransformer objectLifecycleTransformer) {
        this.objectLifecycleTransformer = objectLifecycleTransformer;
    }

    public void setOnNetTags(ArrayList<String> onNetTags) {
        this.onNetTags = onNetTags;
    }

    public <T> MutableLiveData<T> observeGo(Observable observable, final MutableLiveData<T> liveData) {
        return observe(observable, liveData, null);
    }

    public <T> MutableLiveData<T> observeGo(Observable observable, final MutableLiveData<T> liveData, ParamBuilder paramBuilder) {
        if (paramBuilder == null) {
            paramBuilder = ParamBuilder.build();
        }
        int retryCount = paramBuilder.getRetryCount();
        if (retryCount > 0) {
            return observeWithRetry(observable, liveData, paramBuilder);
        } else {
            return observe(observable, liveData, paramBuilder);
        }
    }

    private <T> MutableLiveData<T> observe(Observable observable, MutableLiveData<T> liveData, ParamBuilder paramBuilder) {
        if (paramBuilder == null) {
            paramBuilder = ParamBuilder.build();
        }
        boolean showDialog = paramBuilder.isShowDialog();
        String loadingMessage = paramBuilder.getLoadingMessage();
        int onlineCacheTime = paramBuilder.getOnlineCacheTime();
        int offlineCacheTime = paramBuilder.getOfflineCacheTime();

        if (onlineCacheTime > 0) {
            setOnlineCacheTime(onlineCacheTime);
        }
        if (offlineCacheTime > 0) {
            setOfflineCacheTime(offlineCacheTime);
        }
        String oneTag = paramBuilder.getOneTag();
        if (!TextUtils.isEmpty(oneTag)) {
            if (onNetTags.contains(oneTag)) {
                return liveData;
            }
        }
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.add(oneTag);
                    }
                    if (showDialog) {
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .doFinally(() -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.remove(oneTag);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    liveData.postValue((T) Resource.response((ResponseModel<T>) o));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });
        return liveData;
    }

    private <T> MutableLiveData<T> observeWithRetry(Observable observable, MutableLiveData<T> liveData, ParamBuilder paramBuilder) {

        if (paramBuilder == null) {
            paramBuilder = ParamBuilder.build();
        }
        boolean showDialog = paramBuilder.isShowDialog();
        String loadingMessage = paramBuilder.getLoadingMessage();
        int onlineCacheTime = paramBuilder.getOnlineCacheTime();
        int offlineCacheTime = paramBuilder.getOfflineCacheTime();

        if (onlineCacheTime > 0) {
            setOnlineCacheTime(onlineCacheTime);
        }
        if (offlineCacheTime > 0) {
            setOfflineCacheTime(offlineCacheTime);
        }

        String oneTag = paramBuilder.getOneTag();
        if (!TextUtils.isEmpty(oneTag)) {
            if (onNetTags.contains(oneTag)) {
                return liveData;
            }
        }

        final int maxCount = paramBuilder.getRetryCount();
        final int[] currentCount = {0};

        observable.subscribeOn(Schedulers.io())
                .retryWhen(throwableObservable -> {
                    if (currentCount[0] <= maxCount) {
                        currentCount[0]++;
                        return Observable.just(1).delay(5000, TimeUnit.MILLISECONDS);
                    } else {
                        return Observable.error(new Throwable("重连次数已达到最高，请求超时"));
                    }
                })
                .doOnSubscribe(disposable -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.add(oneTag);
                    }
                    if (showDialog) {
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .doFinally(() -> {
                    if (!TextUtils.isEmpty(oneTag)) {
                        onNetTags.remove(oneTag);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    liveData.postValue((T) Resource.response((ResponseModel<Object>) o));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });

        return liveData;
    }

    //设置在线网络缓存
    private void setOfflineCacheTime(int offlineCacheTime) {
        OfflineCacheInterceptor.getInstance().setOfflineCacheTime(offlineCacheTime);
    }

    //设置离线网络缓存
    private void setOnlineCacheTime(int time) {
        NetCacheInterceptor.getInstance().setOnlineCacheTime(time);
    }

    //正常下载（重新先从0开始下载）
    public <T> MutableLiveData<T> downloadFile(Observable observable, MutableLiveData<T> liveData, final String destDir, final String fileName) {
        return downloadFile(observable, liveData, destDir, fileName, 0);
    }

    //断点下载，如果下载到一半，可以从一半开始下载
    public <T> MutableLiveData<T> downloadFile(Observable observable, final MutableLiveData liveData, final String destDir, final String fileName, long currentLength) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(requestBody -> {
                    if (currentLength == 0) {
                        return DownFileUtils.saveFile((ResponseBody) requestBody, destDir, fileName, liveData);
                    } else {
                        return DownFileUtils.saveFile((ResponseBody) requestBody, destDir, fileName, currentLength, liveData);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    liveData.postValue((T) Resource.success(file));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });
        return liveData;
    }

    //上传文件只有2个参数，showDialog：是否显示dialog；loadmessage：showDialog显示的文字
    public <T> MutableLiveData<T> uploadFile(Observable observable, MutableLiveData<T> liveData) {
        return uploadFile(observable, liveData, null);
    }

    //上传文件
    public <T> MutableLiveData<T> uploadFile(Observable observable, MutableLiveData<T> liveData, ParamBuilder paramBuilder) {
        if (paramBuilder == null) {
            paramBuilder = ParamBuilder.build();
        }
        boolean showDialog = paramBuilder.isShowDialog();
        String loadingMessage = paramBuilder.getLoadingMessage();

        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (showDialog) {
                        liveData.postValue((T) Resource.loading(loadingMessage));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    liveData.postValue((T) Resource.success("成功了"));
                }, throwable -> {
                    liveData.postValue((T) Resource.error((Throwable) throwable));
                });
        return liveData;
    }

}
