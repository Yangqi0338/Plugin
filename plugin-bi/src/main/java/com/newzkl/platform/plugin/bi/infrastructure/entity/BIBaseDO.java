package com.newzkl.platform.plugin.bi.infrastructure.entity;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseIdDO;
import com.newzkl.platform.plugin.bi.domain.constant.BiEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * BI 宽表抽象基类
 *
 * <p>所有宽表实体继承此类, 通过 {@link com.newzkl.platform.plugin.bi.domain.annotation.BITableName}
 * 标注客户端类型, AutoTable 自动生成 dws_realtime_* / dws_day_* 两张表。
 * 日表自动增加 bizDate 字段(不在本类中, 由 AutoTable 生成时附加)。</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BIBaseDO extends BaseIdDO {

    /** 统计所属端
     */
    private CommonEnum.Client client;

    /** 用户ID(如果统计到用户粒度)
     */
    private Long userId;

    /** 事件类型(枚举)
     */
    private BiEventType eventType;

    /** 事件时间
     */
    private LocalDateTime eventTime;

    /** 对应事实表记录ID(用于明细追溯)
     */
    private Long factId;
}
