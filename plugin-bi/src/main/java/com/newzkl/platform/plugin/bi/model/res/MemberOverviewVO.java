package com.newzkl.platform.plugin.bi.model.res;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;
import java.util.List;
@Data
public class MemberOverviewVO {
    private Long memberCount;
    private Integer todayNew;
    private Long verifiedCount;
    private String verifiedRate;
    private String repurchaseRate;
    private String repurchaseMoM;
    private Money balanceAmount;
    private String balancePoints;
    private List<LevelItem> levelList;
    @Data
    public static class LevelItem {
        private String levelName;
        private Integer count;
    }
}