package com.mapure.amap.contest.util;

public class StringUtils {

    //空構造器，防止惡意生成對象
    private StringUtils() {

    }

    /**
     * 計算字符串長度
     * 非ASCII字符，每個長度算爲2
     * 否則算爲1
     *
     * @param paramString
     * @return 字符串長度（微博，人人）
     */
    public static int getWordCount(String paramString) {
        int length = 0;
        for (int i = 0; i < paramString.length(); i++) {
            if (paramString.substring(i, i + 1).matches("[Α-￥]"))
                length += 2;
            else
                length++;
        }

        if (length % 2 > 0) {
            length = 1 + length / 2;
        } else {
            length = length / 2;
        }

        return length;

    }
}
