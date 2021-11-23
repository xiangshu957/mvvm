package com.zrx.mvvmbase.retrofiitwithrxjava.downloadutils;

import androidx.lifecycle.MutableLiveData;

import com.zrx.mvvmbase.base.bean.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/21
 * DES:
 */
public class DownFileUtils {

    //这里是非断点下载，可以理解为正常下载
    public static File saveFile(ResponseBody responseBody, String destFileDir, String destFileName, MutableLiveData liveData) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength();
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                liveData.postValue(Resource.progress((int) (finalSum * 100 / total), total));
            }
            fos.flush();
            return file;
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static File saveFile(ResponseBody responseBody, String destFileDir, String destFileName, long currentLength, MutableLiveData liveData) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength() + currentLength;
            long sum = currentLength;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file, true);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                liveData.postValue(Resource.progress((int) (finalSum * 100 / total), total));
            }
            fos.flush();
            return file;
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

}
