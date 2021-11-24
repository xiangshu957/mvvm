package com.zrx.mvvmbase.retrofiitwithrxjava.interceptor;

import android.os.SystemClock;
import android.text.TextUtils;

import com.zrx.mvvmbase.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/22
 * DES:
 */
public class HttpLogInterceptor implements Interceptor {

    private static HashMap<String, String> headerIgnoreMap = new HashMap<>();

    static {
        headerIgnoreMap.put("Host", "");
        headerIgnoreMap.put("Connection", "");
        headerIgnoreMap.put("Accept-Ending", "");
    }

    protected void log(String message) {
        LogUtils.i("网络请求", message);
    }

    private boolean isPlainText(MediaType mediaType) {
        if (null != mediaType) {
            String mediaTypeString = (null != mediaType ? mediaType.toString() : null);
            if (!TextUtils.isEmpty(mediaTypeString)) {
                mediaTypeString = mediaTypeString.toLowerCase();
                if (mediaTypeString.contains("text") || mediaTypeString.contains("application/json")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = SystemClock.elapsedRealtime();
        Response response = chain.proceed(chain.request());
        long endTime = SystemClock.elapsedRealtime();
        long duration = endTime - startTime;

        String url = request.url().toString();
        log("┌───────Request Start────────────────────────────");
        log("请求方式 -->>" + request.method() + ": " + url);
        log("请求方式 -->>" + "Time:" + duration + " ms");
        Headers headers = request.headers();
        if (null != headers) {
            for (int i = 0, count = headers.size(); i < count; i++) {
                if (!headerIgnoreMap.containsKey(headers.name(i))) {
                    log("请求头部 -->>" + headers.name(i) + "：" + headers.value(i));
                }
            }
        }

        RequestBody requestBody = request.body();
        String paramString = readRequestParamString(requestBody);
        if (!TextUtils.isEmpty(paramString)) {
            log("请求参数 -->>" + paramString);
        }
        ResponseBody responseBody = response.body();
        String responseString = "";
        if (null != responseBody) {
            if (isPlainText(responseBody.contentType())) {
                responseString = readContent(response);
            } else {
                responseString = "other-type=" + responseBody.contentType();
            }
        }
        log("请求返回 -->>" + responseString);
        log("└───────Request End─────────────────────────────" + "\n" + "-");
        return response;
    }

    private String readContent(Response response) {
        if (response == null) {
            return null;
        }
        try {
            return response.peekBody(Long.MAX_VALUE).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String readRequestParamString(RequestBody requestBody) {
        String paramString;
        if (requestBody instanceof MultipartBody) {
            StringBuilder sb = new StringBuilder();
            MultipartBody body = (MultipartBody) requestBody;
            List<MultipartBody.Part> parts = body.parts();
            RequestBody partBody;
            for (int i = 0, size = parts.size(); i < size; i++) {
                partBody = parts.get(i).body();
                if (null != partBody) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    if (isPlainText(partBody.contentType())) {
                        sb.append(readContent(partBody));
                    } else {
                        sb.append("other-param-type=").append(partBody.contentType());
                    }
                }
            }
            paramString = sb.toString();
        } else {
            paramString = readContent(requestBody);
        }
        return paramString;
    }

    private String readContent(RequestBody body) {
        if (body == null) {
            return "";
        }
        Buffer buffer = new Buffer();

        try {
            if (body.contentLength() <= 2 * 1024 * 1024) {
                body.writeTo(buffer);
            } else {
                return "content is more than 2M";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.readUtf8();
    }
}
