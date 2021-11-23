package com.zrx.mvvm.base.bean;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/21
 * DES:
 */
public class Resource<T> {

    //状态 0表示加载中；1表示成功；2表示联网失败；3接口调通，但是返回值有问题
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int FAIL = 3;
    public static final int PROGRESS = 4;//注意只有瞎子啊文件和上传文件时才会有
    public static final int OTHERLOGIN = 5;//但设备登录
    public int state;

    public String errorMsg;
    public T data;
    public Throwable error;
    //单独为fail状态下：可能需要区分错误使用
    public int errorCode;

    public int percentage;//下载百分比
    public long total;//文件总大小

    public Resource(int state, String errorMsg, T data) {
        this.state = state;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public Resource(int state, Throwable error) {
        this.state = state;
        this.error = error;
    }

    public Resource(int state, int percentage, long total) {
        this.state = state;
        this.percentage = percentage;
        this.total = total;
    }

    public Resource(int state, String errorMsg, int errorCode) {
        this.state = state;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public static <T> Resource<T> loading(String showMsg) {
        return new Resource<>(LOADING, showMsg, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, null, data);
    }

    public static <T> Resource<T> response(ResponseModel<T> data) {
        if (data != null) {
            if (data.isSuccess()) {
                return new Resource<>(SUCCESS, null, data.getData());
            }
            return new Resource<>(FAIL, data.getErrorMsg(), data.getErrorCode());
        }
        return new Resource<>(ERROR, null, null);
    }

    public static <T> Resource<T> failure(String msg) {
        return new Resource<>(ERROR, msg, null);
    }

    public static <T> Resource<T> error(Throwable t) {
        return new Resource<>(ERROR, t);
    }

    public static <T> Resource<T> progress(int percentage, long total) {
        return new Resource<>(PROGRESS, percentage, total);
    }

    public void handler(OnHandlerCallback callback) {
        handlerUnCloseDialog(callback);
        if (state != LOADING) {
            callback.onCompleted();
        }
    }

    private void handlerUnCloseDialog(OnHandlerCallback callback) {
        switch (state) {
            case LOADING:
                callback.onLoading(errorMsg);
                break;
            case SUCCESS:
                callback.onSuccess(data);
                break;
            case FAIL:
                callback.onFailure(errorCode, errorMsg);
                break;
            case ERROR:
                callback.onError(error);
                break;
            case PROGRESS:
                callback.onProgress(percentage, total);
                break;
            case OTHERLOGIN:
                callback.onOtherLogin(errorMsg);
                break;
        }
    }

    public interface OnHandlerCallback<T> {
        void onLoading(String showMessage);

        void onSuccess(T data);

        void onFailure(int errorCOde, String msg);

        void onError(Throwable throwable);

        void onCompleted();

        void onProgress(int percentage, long total);

        void onOtherLogin(String msg);
    }

}
