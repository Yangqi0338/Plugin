package com.newzkl.platform.plugin.bi.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员中心等级分布
 */
@Data
public class MemberLevelRes implements Serializable {

    /** 等级分布列表 */
    private List<LevelItem> levelList;

    /**
     * 等级项
     */
    @Data
    public static class LevelItem {

        /** 会员ID */
        private Long memberId;

        /** 等级 */
        private String level;
    }
}
