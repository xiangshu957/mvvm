package com.zrx.mvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/21
 * DES:
 */
public abstract class BaseViewModel<T extends BaseModel> extends AndroidViewModel {

    private T repository;
    private ArrayList<String> onNetTags;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        createRepository();
        onNetTags = new ArrayList<>();
        repository.setOnNetTags(onNetTags);
    }

    public void setObjectLifecycleTransformer(LifecycleTransformer objectLifecycleTransformer){
        if (repository != null){
            repository.setObjectLifecycleTransformer(objectLifecycleTransformer);
        }
    }

    public void createRepository(){
        if (repository == null){
            repository = (T) new BaseModel();
        }
    }

    public T getRepository() {
        return repository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
