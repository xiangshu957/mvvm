package com.zrx.mvvm.bean;

import com.zrx.mvvmbase.base.bean.ResponseModel;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/24
 * DES:
 */
public class ResponseModelCustom<T> extends ResponseModel<T> {

    private int ret;
    private T dataInfo;
    private int errorType;
    private String remark;

    public void setDataInfo(T dataInfo) {
        this.dataInfo = dataInfo;
    }

    public T getDataInfo() {
        return getData();
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }

    @Override
    public T getData() {
        if (dataInfo != null) {
            return dataInfo;
        }
        return super.getData();
    }
}
