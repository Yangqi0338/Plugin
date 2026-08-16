package com.newzkl.platform.plugin.bi.infrastructure.entity.fact;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员事件事实表(每事件一行)
 *
 * <p>会员中心总数/今日新增/等级分布 count 的数据源。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class FactMemberDO extends BaseDO {

    /** 会员ID
     */
    private Long memberId;

    /** 事件类型(注册/注销/确权)
     */
    private BiEventType eventType;

    /** 所属端
     */
    private CommonEnum.Client client;

    /** 会员等级
     */
    private String level;
    
}
