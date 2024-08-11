package com.uestc.crowd.util;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrowdUtil {
    // ture 是 ajax 请求
    public static boolean judgeRequestType(HttpServletRequest request) {
        // 1. 获取请求消息头
        String accept = request.getHeader("Accept");
        String XRequested = request.getHeader("X-Requested-With");
        // 2.判断
        return accept != null && accept.contains("application/json")
                ||
                (XRequested != null && XRequested.contains("XMLHttpRequest"));
    }

    /**
     * 使用md5加密字符
     * @param source 待加密字符
     * @return 加密结果
     */
    public static String md5(String source) {
        // 1. 判断字符串是否有效
        if (source == null || source.isEmpty()) {
            // 2. 抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALID_DATE);
        }
        // 3. 获取MessageDigest对象
        String algorithm = "md5";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4. 获取明文字符串对应的字节数组
            byte[] sourceBytes = source.getBytes();
            // 5. 执行加密
            byte[] output = messageDigest.digest(sourceBytes);
            // 6. 创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);
            // 7. 按照16进制将bigInteger的值转换为字符串
            int radix = 16;
            return bigInteger.toString(radix);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
