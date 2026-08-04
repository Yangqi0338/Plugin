package com.newzkl.platform.plugin.audit.worktable.constant;

/**
 * 工单执行常量
 *
 * <p>硬线1: worktable 包禁 import base.biz, 故把 biz-finance SpuEnum/NotifyEnums 等所需 code 本地固化</p>
 *
 * @author KC
 */
public final class WorktableConst {

    private WorktableConst() {
    }

    /** 操作目标: SPU 基础信息, 对齐 SpuEnum.OperateTarget.SPU_BASE */
    public static final int TARGET_SPU_BASE = 1;

    /** 操作目标: 销售属性 */
    public static final int TARGET_SALE_ATTRIBUTE = 2;

    /** 操作目标: 参数属性 */
    public static final int TARGET_PARAM_ATTRIBUTE = 3;

    /** 操作目标: SKU 基础信息 */
    public static final int TARGET_SKU_BASE = 4;

    /** 操作目标: SPU 状态 */
    public static final int TARGET_SPU_STATE = 5;

    /** 操作类型: 修改 */
    public static final int TYPE_UPDATE = 1;

    /** 操作类型: 新增 */
    public static final int TYPE_ADD = 2;

    /** 操作类型: 删除 */
    public static final int TYPE_DELETE = 3;

    /** SPU 状态: 平台下架, 对齐 SpuEnum.State.PLATFORM_DOWN */
    public static final int STATE_PLATFORM_DOWN = 1;

    /** SPU 状态: 在售 */
    public static final int STATE_SALE = 2;

    /** 开关: 关 */
    public static final int SWITCH_OFF = 0;

    /** 开关: 开 */
    public static final int SWITCH_ON = 1;

    /** 是否: 是, 对齐 CommonEnum.YesOrNo.YES */
    public static final int YES = 0;

    /** 通知服务类型: 商品, 对齐 NotifyEnums.ServiceType.GOODS */
    public static final int NOTIFY_SERVICE_GOODS = 1;

    /** 通知业务类型: SKU 规格删除, 对齐 NotifyEnums.GoodsType.DELETE_SKU */
    public static final int NOTIFY_DELETE_SKU = 2;

    /** 通知业务类型: SPU 基本信息, 对齐 NotifyEnums.GoodsType.UPDATE_SPU */
    public static final int NOTIFY_UPDATE_SPU = 3;

    /** 通知业务类型: SKU 规格变更, 对齐 NotifyEnums.GoodsType.UPDATE_SKU */
    public static final int NOTIFY_UPDATE_SKU = 4;

    /** 通知业务类型: SPU 上下架, 对齐 NotifyEnums.GoodsType.UPDATE_spu_STATE(源码常量名小写 spu, 值 5) */
    public static final int NOTIFY_UPDATE_SPU_STATE = 5;

    /** 操作日志类型: SPU 销售价变动, 对齐 ExecuteEnum.Type.SPU_SALE_PRICE */
    public static final int EXECUTE_TYPE_SPU_SALE_PRICE = 0;
}
