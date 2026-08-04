package com.newzkl.platform.plugin.openapi.model.vo;

import com.zkl.scm.model.web.BaseVO;
import lombok.Data;

/**
 * 开发者
 * @author fang
 */
@Data
public class DeveloperVO extends BaseVO {
     /**
     * ID
     */
     private Long id;
     private String appId;
     /**
     * 开发者名称
     */
     private String appName;
     /**
     * 开发者密钥
     */
     private String secret;
     /**
     * 账号ID
     */
     private Long accountId;
     /**
     * 备注
     */
     private String remark;
     /**
      * 通知地址
      */
     private String notifyAddress;
}