package com.zrx.mvvm.utils;

import java.math.BigDecimal;

/**
 * @Author: ZhangRuixiang
 * Date: 2021/11/13
 * DES:
 */
public class MathUtils {

    public static double round(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = null == v ? new BigDecimal("0.0") : new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
