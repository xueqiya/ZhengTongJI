package com.sangu.apptongji.utils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Administrator on 2018-04-18.
 */

public class CodeUtil {
    /*
    五个验证码  其中第一个是真实的验证码  第二个是用来做规则的
先判断code3是否等于"操作频繁"   如果是操作频繁就给个提示框
如果code3不是操作频繁 就开始做校验

第一步  根据code2的最后一位数看奇偶数
奇数就把code1   6位数按升序排序
偶数就把code1   6位数按降序排序

第二步  把做完排序后的6位数md5一下

第三步 123456 —— abcdef  0对应a  789——abc

意思是 code2  假设是123456   就在md5之后的串上拼接上abcdef

第四部把拼接之后的串再次md5一下

第五部 调用user/sendAuthCode
uId
deviceStr（第四部的字符串穿进去）
     */
    public static String getAuthCode(String code1, String code2) {
        if (isOddNum(code2)) {
            String shengxu = getshengxu(code1);
            //System.out.println("shengxu" + shengxu);
            shengxu = MD5.md5(shengxu);
            return MD5.md5(shengxu + code2ToString(code2));
        } else {
            String jiangxu = getjiangxu(code1);
            //System.out.println("jiangxu" + jiangxu);
            jiangxu = MD5.md5(jiangxu);
            return MD5.md5(jiangxu + code2ToString(code2));
        }
    }

    public static String code2ToString(String code2) {
        char[] codeString= code2.toCharArray();
        String finalCode = "";
        for (char code : codeString) {
            String codeStr = String.valueOf(code);
            if (codeStr.equalsIgnoreCase("0")) {
                finalCode = finalCode + "a";
            } else if (codeStr.equalsIgnoreCase("1")) {
                finalCode = finalCode + "a";
            } else if (codeStr.equalsIgnoreCase("2")) {
                finalCode = finalCode + "b";
            } else if (codeStr.equalsIgnoreCase("3")) {
                finalCode = finalCode + "c";
            } else if (codeStr.equalsIgnoreCase("4")) {
                finalCode = finalCode + "d";
            } else if (codeStr.equalsIgnoreCase("5")) {
                finalCode = finalCode + "e";
            } else if (codeStr.equalsIgnoreCase("6")) {
                finalCode = finalCode + "f";
            } else if (codeStr.equalsIgnoreCase("7")) {
                finalCode = finalCode + "a";
            } else if (codeStr.equalsIgnoreCase("8")) {
                finalCode = finalCode + "b";
            }else if (codeStr.equalsIgnoreCase("9")) {
                finalCode = finalCode + "c";
            }
        }
        return finalCode;
    }

    public static String getshengxu(String code) {
        char[] codeString= code.toCharArray();

        Integer[] codeInt = new Integer[codeString.length];

        for (int i = 0; i < codeString.length; i++) {
            String str = String.valueOf(codeString[i]);
            codeInt[i] = Integer.valueOf(str);
        }
        Arrays.sort(codeInt);
        String finalCode = "";
        for (Integer num : codeInt) {
            finalCode = finalCode + num;
        }
        return finalCode;

    }

    public static String getjiangxu(String code) {
        char[] codeString= code.toCharArray();
        Integer[] codeInt = new Integer[codeString.length];
        for (int i = 0; i < codeString.length; i++) {

            String str = String.valueOf(codeString[i]);
            codeInt[i] = Integer.valueOf(str);
        }
        Arrays.sort(codeInt, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;

            }
        });
        String finalCode = "";
        for (Integer num : codeInt) {
            finalCode = finalCode + num;
        }
        return finalCode;

    }

    //判断是否是奇数
    public static boolean isOddNum(String num) {
        num = num.substring(num.length() - 1, num.length());

        if (Integer.valueOf(num) % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }
}