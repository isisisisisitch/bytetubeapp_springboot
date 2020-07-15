package com.bytetube.util;

import com.alibaba.fastjson.JSONObject;
import com.bytetube.dto.ResultBean;

public class WxUtils {
    public static void main(String[] args) throws Exception {
        String longUrl = "https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login";
//        System.out.println(getToken("wx0796c4dd537e5428","b9a76133d6565c39a4fc8073e20128e8"));

        ResultBean<String> stringResultBean = wxLongUrl2Short("wx0796c4dd537e5428", "b9a76133d6565c39a4fc8073e20128e8", longUrl);
        System.out.println(stringResultBean.getData());
    }

    /**
     * 微信根据APPID和APPSECRET获取token
     * @param appid
     * @param appsrcret
     * @return 返回转换后的短链接
     */
    public static String getToken(String appid, String appsrcret) throws Exception {
        //访问地址
        String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String APPID = appid;
        String APPSECRET = appsrcret;
        String request_url = TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = HttpUtils.httpsUtil(request_url,"GET",null);
        if(null != jsonObject) {
            String access_token = jsonObject.getString("access_token");
            return access_token;
        }
        return null;
    }

    /**
     * 将长链接转为短链接(调用的微信短网址API)
     * 需要token
     * @param longUrl 需要转换的长链接url
     * @param appid
     * @param appsrcret
     * @return 返回转换后的短链接
     */
    public static ResultBean<String> wxLongUrl2Short(String appid, String appsrcret, String longUrl) throws Exception {
        ResultBean<String> result = new ResultBean<>();
        String token = getToken(appid, appsrcret);
        String uri = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        String url = uri.replace("ACCESS_TOKEN", token);
        String param = "{\"action\":\"long2short\","
                + "\"long_url\":\""+longUrl+"\"}";
        // 调用接口创建菜单
        JSONObject jsonObject = HttpUtils.httpRequest(url, "POST", param);
        System.out.println(jsonObject);
        if (null != jsonObject) {
            result.setCode(jsonObject.getInteger("errcode"));
            result.setData(jsonObject.getString("short_url"));
            return result;
        }else{
            return null;
        }
    }

}
