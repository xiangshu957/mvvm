package com.zrx.mvvm.utils;

import android.util.Log;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/13
 * DES:
 */
public class LogUtils {

    private static boolean isEnableLog = true;
    private static int LOG_MEACLENGTH = 4000;
    public static final String TAG = LogUtils.class.getSimpleName();
    private static final String TOP_DIVIDER =
            "┌────────────────────────────────────────────────────────";
    private static final String BOTTOM_DIVIDER =
            "└────────────────────────────────────────────────────────";
    private static final String MIDDLE_DIVIDER = "----------- 换行 ------------\n";

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (isEnableLog) {
            if (msg == null) {
                Log.v(tag, "null");
                return;
            }
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MEACLENGTH;
            if (strLength > end) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("\n").append(TOP_DIVIDER).append(msg);
                String trueMsg = sbf.toString();
                strLength = trueMsg.length();
                while (strLength > end) {
                    if (start == 0) {
                        Log.v(tag, trueMsg.substring(0, end));
                    } else {
                        end = end - MIDDLE_DIVIDER.length();
                        Log.v(tag, MIDDLE_DIVIDER + trueMsg.substring(start, end));
                    }
                    start = end;
                    end = end + LOG_MEACLENGTH;
                }
                Log.v(tag, trueMsg.substring(start, strLength));
                Log.v(tag, "\n" + BOTTOM_DIVIDER);
            }else {
                Log.v(tag, msg);
            }
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (isEnableLog) {
            if (msg == null) {
                Log.d(tag, "null");
                return;
            }
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MEACLENGTH;
            if (strLength > end) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("\n").append(TOP_DIVIDER).append(msg);
                String trueMsg = sbf.toString();
                strLength = trueMsg.length();
                while (strLength > end) {
                    if (start == 0) {
                        Log.d(tag, trueMsg.substring(0, end));
                    } else {
                        end = end - MIDDLE_DIVIDER.length();
                        Log.d(tag, MIDDLE_DIVIDER + trueMsg.substring(start, end));
                    }
                    start = end;
                    end = end + LOG_MEACLENGTH;
                }
                Log.d(tag, trueMsg.substring(start, strLength));
                Log.d(tag, "\n" + BOTTOM_DIVIDER);
            }else {
                Log.d(tag, msg);
            }
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (isEnableLog) {
            if (msg == null) {
                Log.i(tag, "null");
                return;
            }
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MEACLENGTH;
            if (strLength > end) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("\n").append(TOP_DIVIDER).append(msg);
                String trueMsg = sbf.toString();
                strLength = trueMsg.length();
                while (strLength > end) {
                    if (start == 0) {
                        Log.i(tag, trueMsg.substring(0, end));
                    } else {
                        end = end - MIDDLE_DIVIDER.length();
                        Log.i(tag, MIDDLE_DIVIDER + trueMsg.substring(start, end));
                    }
                    start = end;
                    end = end + LOG_MEACLENGTH;
                }
                Log.i(tag, trueMsg.substring(start, strLength));
                Log.i(tag, "\n" + BOTTOM_DIVIDER);
            }else {
                Log.i(tag, msg);
            }
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (isEnableLog) {
            if (msg == null) {
                Log.w(tag, "null");
                return;
            }
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MEACLENGTH;
            if (strLength > end) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("\n").append(TOP_DIVIDER).append(msg);
                String trueMsg = sbf.toString();
                strLength = trueMsg.length();
                while (strLength > end) {
                    if (start == 0) {
                        Log.w(tag, trueMsg.substring(0, end));
                    } else {
                        end = end - MIDDLE_DIVIDER.length();
                        Log.w(tag, MIDDLE_DIVIDER + trueMsg.substring(start, end));
                    }
                    start = end;
                    end = end + LOG_MEACLENGTH;
                }
                Log.w(tag, trueMsg.substring(start, strLength));
                Log.w(tag, "\n" + BOTTOM_DIVIDER);
            }else {
                Log.w(tag, msg);
            }
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (isEnableLog) {
            if (msg == null) {
                Log.e(tag, "null");
                return;
            }
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MEACLENGTH;
            if (strLength > end) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("\n").append(TOP_DIVIDER).append(msg);
                String trueMsg = sbf.toString();
                strLength = trueMsg.length();
                while (strLength > end) {
                    if (start == 0) {
                        Log.e(tag, trueMsg.substring(0, end));
                    } else {
                        end = end - MIDDLE_DIVIDER.length();
                        Log.e(tag, MIDDLE_DIVIDER + trueMsg.substring(start, end));
                    }
                    start = end;
                    end = end + LOG_MEACLENGTH;
                }
                Log.e(tag, trueMsg.substring(start, strLength));
                Log.e(tag, "\n" + BOTTOM_DIVIDER);
            }else {
                Log.e(tag, msg);
            }
        }
    }
}
