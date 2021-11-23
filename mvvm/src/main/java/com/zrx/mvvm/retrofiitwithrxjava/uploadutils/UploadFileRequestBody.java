package com.zrx.mvvm.retrofiitwithrxjava.uploadutils;

import androidx.lifecycle.MutableLiveData;

import com.zrx.mvvm.base.bean.Resource;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:
 */
public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    MutableLiveData liveData;

    public UploadFileRequestBody(File file, MutableLiveData liveData) {
        this.mRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        this.liveData = liveData;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            if (liveData != null) {
                liveData.postValue(Resource.progress((int) (bytesWritten * 100 / contentLength()), contentLength()));
            }
        }
    }
}
