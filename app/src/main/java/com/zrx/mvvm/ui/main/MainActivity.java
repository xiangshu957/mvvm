package com.zrx.mvvm.ui.main;

import com.zrx.mvvm.R;
import com.zrx.mvvm.databinding.ActivityMainBinding;
import com.zrx.mvvm.model.TestBean;
import com.zrx.mvvmbase.base.BaseActivity;
import com.zrx.mvvmbase.utils.GsonUtils;
import com.zrx.mvvmbase.utils.LogUtils;

import java.util.List;


public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void processLogic() {
        mViewModel.getData().observe(this, listResource -> {
            listResource.handler(new OnCallback<List<TestBean>>() {

                @Override
                public void onSuccess(List<TestBean> data) {
                    String ser = GsonUtils.ser(data);
                    LogUtils.e("zrx", ser);
                }

                @Override
                public void onFail(String msg) {
                    super.onFail(msg);
                    LogUtils.e("zrx", msg);
                }
            });

        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void showDialog(String msg) {

    }

    @Override
    protected void hideDialog() {

    }

    @Override
    protected void registerBroadCast() {

    }

    @Override
    protected void unRegisterBroadCast() {

    }
}