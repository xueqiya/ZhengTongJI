package com.sangu.apptongji.main.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.sangu.apptongji.main.FXConstant;
import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.utils.EaseCommonUtils;

/**
 * Created by lishao on 2016/7/5.\
 * QQ:84543217
 */
public class JSONUtil {
    static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static LatLng convertGPSToBaidu(LatLng srLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(srLatLng);
        return converter.convert();
    }

    public static double[] bd09togcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[] { gg_lng, gg_lat };
    }

    public static EaseUser Json2User(JSONObject userJson) {
        EaseUser easeUser = new EaseUser(userJson.getString(FXConstant.JSON_KEY_HXID));
        easeUser.setNick(userJson.getString(FXConstant.JSON_KEY_LOID));
        easeUser.setAvatar(userJson.getString(FXConstant.JSON_KEY_AVATAR));
        easeUser.setUserInfo(userJson.toJSONString());
        EaseCommonUtils.setUserInitialLetter(easeUser);
        return easeUser;
    }

    public static JSONObject User2Json(EaseUser user) {
        JSONObject jsonObject = new JSONObject();
        String userInfo = user.getUserInfo();
        try {
            if (userInfo != null) {

                jsonObject = JSONObject.parseObject(userInfo);
            }
        } catch (JSONException e) {

              Log.d("JSONUtil----->>","User2Json error");
        }

        return jsonObject;

    }


}

