package com.sangu.apptongji.main.address;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-16.
 */

public class GetPhoneNumberFromMobile {
    public static List<PhoneInfo> getPhoneNumberFromMobile(Context context) {
        List<PhoneInfo> list = new ArrayList<PhoneInfo>();
        Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI,
                null, null, null, null);
//moveToNext方法返回的是一个boolean类型的数据
        if (cursor!=null) {
            while (cursor.moveToNext()) {
//读取通讯录的姓名
                String namePain = cursor.getString(cursor
                        .getColumnIndex(Phone.DISPLAY_NAME));
                String name;
                if (namePain != null) {
                     name = namePain.trim();
                } else {
                    name = null;
                }
//读取通讯录的号码
                String number = cursor.getString(cursor
                        .getColumnIndex(Phone.NUMBER)).trim().replace(" ","").replace("-","").replace("+86","");
                if (TextUtils.isEmpty(number))
                    continue;
                if (isMobile(number)) {
                    PhoneInfo phoneInfo = new PhoneInfo(name, number);
                    list.add(phoneInfo);
                }
            }
            cursor.close();
        }
        return list;
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
}
