package com.newzkl.platform.plugin.hdh.model;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import com.newzkl.platform.plugin.hdh.HuiDingHuoApiUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.common.utils.JsonUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Component
public class HdhCallBackFilter extends OncePerRequestFilter {

    public static void main(String[] args) throws IOException {
        // do gzip
        String json = "{\"id\":4316690775068636991,\"brandId\":7171200588067688978,\"brandName\":\"圣罗兰\",\"groupId\":6502,\"firstCateName\":\"时尚鞋靴\",\"secondCateName\":\"男鞋2\",\"thirdCateName\":\"皮鞋\",\"firstCateId\":135,\"secondCateId\":136,\"thirdCateId\":161,\"currency\":\"CNY\",\"name\":\"Ysl/圣罗兰 逆龄女神粉底液 600ML\",\"originalName\":null,\"shopName\":\"测试2店铺\",\"modifier\":\"青鸟飞鱼\",\"desc\":\"Ysl/圣罗兰 逆龄女神粉底液 30ML\",\"richDesc\":\"<p><strong>产品属性</strong></p><p>尺码: 身高(cm)</p><p>160: 150-160</p><p>150: 140-150</p><p>120: 110-120</p><p>140: 130-140</p><p>170: 160-170</p><p>130: 120-130</p><p>105: 90-100</p><p>110: 100-110</p><p><img src=\\\"https://gkip.img.apiunion.com/i/fc08cd59b81805397a8c98635ae728d2_750X185_bW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/1ccc18e998de56ae06343eb41cfca4b0_750X1185_3zW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/93b0650d83556282c786916c448c1304_750X874_6iW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/10d291888f79c3c3ee9298cff876984e_750X832_1oW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/7b41508ca11a263c443868c97d4a1593_750X704_2bW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/aa8cbbc3d6b6b849bdb70f574db8077f_750X1394_4zW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/c67d1cdaf812aeb254f02078ce4c140e_750X968_39W.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/ec696988e97bc2b8d3728ee3fd588ae9_750X1128_44W.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/896a273b8ec6a987b71d2d68beef9b6d_750X1133_49W.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/3f7eecb437a0b74515370860c254ec4c_750X764_34W.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/bda2e4d4f619d72bedc9958a3609ceef_750X660_2eW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/53da169becf60a5f20c7e067d986768f_750X496_1uW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/85b784aa549c5a81880918a56be36c20_750X979_6oW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/e9c8e19f2598866798543950398c73ca_750X928_6bW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/e29d17ebad94a192765fbc4373cbd6d5_750X911_5cW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/797c57ec1d9803f2213d8c5c264278a4_750X942_7lW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"> <img src=\\\"https://gkip.img.apiunion.com/i/84c4829c22114f0e65260b2b42244ca7_750X738_1xW.jpg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"></p><p><img src=\\\"https://img.picing.com/7051860835006694019_800X746_70W.png?imageMogr2/quality/93/thumbnail/800x/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"><img src=\\\"https://img.picing.com/3834540495084393461_800X915_7qW.png?imageMogr2/quality/93/thumbnail/800x/format/jpeg\\\" style=\\\"width:100%;height:auto;display:block;\\\"></p>\",\"sellingPoints\":[\"卖点1\",\"卖点2\",\"卖点3\"],\"gift\":0,\"presell\":0,\"shelfStatus\":1,\"isDelete\":0,\"imageList\":[\"https://timg.apiunion.com/i/de4e5b145cf872a284d2e7b73c28b84f_800X800_7lW.jpeg?imageView2/2/w/800/interlace/1/q/93/format/jpeg\"],\"skuNameValues\":[{\"skuName\":\"颜色\",\"skuValues\":[{\"skuValue\":\"黑白黄\",\"imageUrlList\":[\"https://timg.apiunion.com/i/954ae5757ae5065e6395d3c511248de3_180X180_4W.png?imageView2/2/w/800/interlace/1/q/93/format/jpeg\"]}]}],\"skuList\":[{\"skuId\":8949340734183407051,\"itemId\":4316690775068636991,\"weight\":1.27,\"weightG\":1270,\"unit\":\"件\",\"unitQuantity\":1,\"upcList\":[\"1274\"],\"upc\":\"1274\",\"totalStock\":49,\"skuValues\":[{\"skuName\":\"颜色\",\"skuValue\":\"黑白黄\",\"imageUrl\":\"https://timg.apiunion.com/i/954ae5757ae5065e6395d3c511248de3_180X180_4W.png?imageView2/2/w/800/interlace/1/q/93/format/jpeg\"}],\"skuChannels\":[{\"channelName\":\"澳洲直邮\",\"channelType\":1301,\"price\":102.9,\"originalPrice\":1000,\"actId\":null,\"actPrice\":null,\"actEndTime\":null,\"actTotalBuyNum\":null,\"stock\":49,\"itemCode\":\"5JHC16HDP03AP\",\"salesStatus\":1,\"expressTemplateId\":\"451A903AP\",\"minBuyNum\":null,\"maxBuyNum\":null,\"materials\":null}]}],\"itemLimitCondition\":null}";
        // 1. 初始化字节输出流和Gzip输出流
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOut = new GZIPOutputStream(baos, 6)) {

            // 2. 将JSON字符串按UTF-8编码写入Gzip流（避免中文乱码）
            gzipOut.write(json.getBytes(StandardCharsets.UTF_8));
            gzipOut.finish(); // 强制刷新压缩数据，确保完整

            // 3. 返回Gzip压缩后的字节数组
            String str = Base64.getEncoder().encodeToString(baos.toByteArray());
            System.out.println(str);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            try {
                //获取参数Map
                InputStream is = request.getInputStream();

                String contentEncoding = request.getHeader("CONTENT_ENCODING");
                String bodyInfo;
                if (StrUtil.equalsIgnoreCase(contentEncoding, "gzip")) {
                    log.info("请求体为 gzip 压缩格式，开始解压");
                    bodyInfo = IOUtils.toString(new GZIPInputStream(is), "utf-8");
                    setBody(request, bodyInfo);
                } else {
                    bodyInfo = IOUtils.toString(is, "utf-8");
                }

                // 1. 签名验证
                String sign = request.getHeader("SIGN");
                if (StrUtil.isBlank(sign)) {
                    log.warn("回调签名为空，外部订单号：{}");
                    failResponse(response);
                    return;
                }
                boolean signValid = HuiDingHuoApiUtils.verifySign(bodyInfo, sign);
                if (!signValid) {
                    log.warn("回调签名验证失败，外部订单号：{}");
                    failResponse(response);
                    return;
                }
            } catch (Exception e) {
                log.error("会订货回调处理异常，请求参数：{}", e);
                failResponse(response);
            }
        } else {
            ThrowsException.exception(BaseErrorCode.REMOTE, "仅支持POST请求");
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 设置压缩包数据
     *
     * @param request
     * @throws IOException
     */
    private void setBody(HttpServletRequest request, String body) throws IOException {
        try {
            // 调用方必须引入common-developer
            ReflectUtil.invoke(request, "setBody", body);
        } catch (Exception ignored) {
        }
    }

    /**
     * 异常响应
     *
     * @param servletResponse
     * @throws IOException
     */
    private void failResponse(HttpServletResponse servletResponse) throws IOException {
        ServletOutputStream out = servletResponse.getOutputStream();
        out.write(JsonUtils.toJson(PlatformResult.fail()).getBytes());
        out.flush();
        out.close();
    }
}
