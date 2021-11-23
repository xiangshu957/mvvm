package com.zrx.mvvm.retrofiitwithrxjava.uploadutils;

import android.os.Handler;
import android.os.Looper;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/21
 * DES:
 */
public abstract class FileUploadObserver<T> extends DefaultObserver<T> {

    Handler mDelivery = new Handler(Looper.getMainLooper());

    @Override
    public void onNext(@NonNull T t) {
        mDelivery.post(() -> {
            onUploadSuccess(t);
        });
    }

    @Override
    public void onError(@NonNull Throwable e) {
        mDelivery.post(() -> {
            onUploadFail(e);
        });
    }

    @Override
    public void onComplete() {

    }

    public void onProgressChange(final long bytesWritten, final long contentLength) {
        mDelivery.post(() -> {
            onProgress((int) (bytesWritten * 100 / contentLength));
        });
    }

    //上传成功的回调
    public abstract void onUploadSuccess(T t);

    //上传失败的回调
    public abstract void onUploadFail(Throwable e);

    //上传进度回调
    public abstract void onProgress(int progress);
}
