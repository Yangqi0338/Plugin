package com.newzkl.platform.plugin.openapi.model.util;

import com.alibaba.fastjson.JSON;
import com.newzkl.platform.plugin.openapi.model.constants.Constants;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 开放平台签名工具
 *
 * <p>入站验签({@code SignatureFilter})与出站通知签名共用同一套算法: 参数名升序拼 {@code k=v&}
 * 跳过签名字段与空值 尾部直接拼明文密钥(无 {@code key=} 前缀) 最后取小写 md5</p>
 *
 * <p>待签串样例
 * {@code appId=1001&outOrderNo=mt_1234567892112&randomNumber=Dqb9wnQZoke7miAW6wvtfHt2JduQ75fG&spuId=506971694252422&timeStamp=1706234862633&{appSecret}}</p>
 *
 * @author fang
 */
public class SignatureUtil {

    /**
     * 生成签名 若含 sign_type 字段须与 signType 参数一致
     *
     * @param data      参与签名的参数 值为空串的键自动跳过
     * @param key       开发者密钥 拼在待签串尾部
     * @param signField 签名字段名 该键本身不参与签名
     * @return 小写 md5 签名
     */
    public static String sign(final Map<String, Object> data, String key, String signField) {
        return getSign(getWaitSignString(data, key, signField));
    }

    /**
     * 生成出站通知签名
     *
     * <p>body 先经 fastjson round-trip 再签: 嵌套 List/对象必须以 JSON 文本形态参与签名 才能与入站
     * {@code SignatureFilter.buildParam} 逐字节对齐 —— 直接把 Java 对象塞进 Map 会拼出 Lombok
     * toString 与带空格的 {@code [1, 2]} 开发者无从复现</p>
     *
     * @param requestBody  出站 body 的 JSON 文本
     * @param appId        开发者 appId
     * @param secret       开发者密钥
     * @param timeStamp    时间戳 必须与请求头同值
     * @param randomNumber 随机数 必须与请求头同值
     * @return 小写 md5 签名
     */
    public static String notifySign(String requestBody, String appId, String secret, String timeStamp, String randomNumber) {
        Map<String, Object> params = JSON.parseObject(requestBody);
        params.put(Constants.APP_ID, appId);
        params.put(Constants.TIME_STAMP, timeStamp);
        params.put(Constants.RANDOM_NUMBER, randomNumber);
        return sign(params, secret, Constants.SIGN);
    }

    /**
     * 拼待签串
     *
     * <p>⚠ 返回值尾部就是明文密钥 任何情况下不得打日志或返回给调用方以外的地方</p>
     *
     * @param data      参与签名的参数
     * @param key       开发者密钥
     * @param signField 不参与签名的键名
     * @return 待签串 形如 {@code a=1&b=2&{appSecret}}
     */
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

    /**
     * 待签串取小写 md5
     *
     * @param waitSignString 待签串
     * @return 小写 md5 签名
     */
    public static String getSign(String waitSignString) {
        return DigestUtils.md5Hex(waitSignString.getBytes(StandardCharsets.UTF_8));
    }
}
