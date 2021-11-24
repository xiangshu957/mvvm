package com.zrx.mvvm.ui.main;

import com.zrx.mvvm.R;
import com.zrx.mvvm.databinding.ActivityMainBinding;
import com.zrx.mvvmbase.base.BaseActivity;
import com.zrx.mvvmbase.utils.GsonUtils;
import com.zrx.mvvmbase.utils.LogUtils;


public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void processLogic() {
        mViewModel.getData().observe(this, listResource -> {
            String ser = GsonUtils.ser(listResource);
            LogUtils.e("zrx", ser);
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