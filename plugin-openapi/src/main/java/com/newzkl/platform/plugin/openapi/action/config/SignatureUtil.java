package com.newzkl.platform.plugin.openapi.action.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author fang
 */
@Slf4j
public class SignatureUtil {
    private static final String CHARSET = "UTF-8";

    private static final String SIGN_TYPE = "MD5";


    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String sign(final Map<String, Object> data, String key, String signField) {
        return getSign(getWaitSignString(data, key, signField));
    }
    public static String getWaitSignString(final Map<String, Object> data, String key, String signField) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(signField)) {
                continue;
            }
            //参数值为空，则不参与签名
            if (String.valueOf(data.get(k)).trim().length() > 0) {
                sb.append(k).append("=").append(String.valueOf(data.get(k)).trim()).append("&");
            }
        }
        sb.append(key);
        String waitSignString = sb.toString();
        return waitSignString;
    }
    public static String getSign(String waitSignString){
        log.warn("xxxxxxxxxxxx- waitSignString:" + waitSignString);
        String result = DigestUtils.md5Hex(waitSignString.getBytes(StandardCharsets.UTF_8));
        log.warn("xxxxxxxxxxxx- sign:" + result);
        return result;
    }
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    private static String MD5(String data)  {
        try {
            MessageDigest md = MessageDigest.getInstance(SIGN_TYPE);
            byte[] array = md.digest(data.getBytes(CHARSET));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String wait = "appId=1001&outOrderNo=mt_1234567892112&randomNumber=Dqb9wnQZoke7miAW6wvtfHt2JduQ75fG&spuId=506971694252422&timeStamp=1706234862633&mvqajecoyurff1haj7am33l5rq9mnfi1";
        System.out.println(getSign(wait));
    }
}
