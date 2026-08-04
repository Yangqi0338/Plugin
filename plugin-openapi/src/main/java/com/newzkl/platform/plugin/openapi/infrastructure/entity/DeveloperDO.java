package com.newzkl.platform.plugin.openapi.infrastructure.entity;

//import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
//import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
//import com.gitee.sunchenbin.mybatis.actable.annotation.TableComment;
//import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
//import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.zkl.scm.mybatis.model.BaseDO;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/1510:06
 */
@Data
//@Table(name = "developer")
//@TableComment("用户账号")
public class DeveloperDO extends BaseDO {

    //@Column(type = MySqlTypeConstant.BIGINT,isKey = true,isAutoIncrement = false, comment = "appId、开发者ID")
    private Long id;
    //@Unique
    //@Column(type = MySqlTypeConstant.VARCHAR, length = 32 , comment = "appId")
    private String appId;
    //@Column(type = MySqlTypeConstant.VARCHAR, length = 32 , comment = "开发者名称")
    private String appName;
    //@Column(type = MySqlTypeConstant.VARCHAR, length = 32 , comment = "开发者密钥")
    private String secret;
    //@Column(type = MySqlTypeConstant.BIGINT, comment = "账号ID")
    private Long accountId;
    //@Column(type = MySqlTypeConstant.VARCHAR, length = 32 , comment = "备注")
    private String remark;
    /**
     * 通知地址
     */
    private String notifyAddress;
}
