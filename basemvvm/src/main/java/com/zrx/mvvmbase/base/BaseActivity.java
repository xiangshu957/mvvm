package com.zrx.mvvmbase.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonSyntaxException;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.zrx.mvvmbase.base.bean.Resource;
import com.zrx.mvvmbase.utils.networks.NetWorkUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:
 */
public abstract class BaseActivity<VM extends BaseViewModel, VDB extends ViewDataBinding> extends RxFragmentActivity {

    //获取当前activity布局文件
    protected abstract int getContentViewId();

    //处理逻辑业务
    protected abstract void processLogic();

    //所有监听放这里
    protected abstract void setListener();

    protected abstract void showDialog(String msg);

    protected abstract void hideDialog();

    protected abstract void registerBroadCast();

    protected abstract void unRegisterBroadCast();

    protected VM mViewModel;
    protected VDB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        binding = DataBindingUtil.setContentView(this, getContentViewId());
        binding.setLifecycleOwner(this);
        createViewModel();
        setListener();
        processLogic();
    }

    public void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
            mViewModel.setObjectLifecycleTransformer(bindToLifecycle());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadCast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterBroadCast();
    }

    public Context getContext() {
        return this;
    }

    public abstract class OnCallback<T> implements Resource.OnHandlerCallback<T> {

        @Override
        public void onLoading(String showMessage) {
            showDialog(showMessage);
        }

        @Override
        public void onError(Throwable throwable) {
            if (!NetWorkUtils.isNetWorkConnected(getContext())) {
                onFail("网络不给力，请检查网络");
                return;
            }
            if (throwable instanceof ConnectException) {
                onFail("服务器连接失败，请稍后重试");
            } else if (throwable instanceof SocketTimeoutException) {
                onFail("服务器连接超时，请稍后重试");
            } else if (throwable instanceof JsonSyntaxException) {
                onFail("数据解析出错");
            } else {
                onFail("系统繁忙，请稍后再试");
            }
        }

        @Override
        public void onFailure(int errorCOde, String msg) {
            onFail(msg);
        }

        @Override
        public void onCompleted() {
            hideDialog();
        }

        @Override
        public void onProgress(int percentage, long total) {

        }

        @Override
        public void onOtherLogin(String msg) {

        }

        public void onFail(String msg) {

        }
    }

    public String getStringByUI(View view) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString().trim();
        }
        return "";
    }

}
