package com.newzkl.platform.plugin.bi.action.home;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.plugin.bi.application.service.HomeBiService;
import com.newzkl.platform.plugin.bi.model.res.HomeOverviewVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTodoVO;
import com.newzkl.platform.plugin.bi.model.res.HomeTrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页工作台统计控制器(打样)
 *
 * <p>对应用户首页工作台三个部分:
 * <ul>
 *   <li>{@code /bi/home/overview} 实时概况</li>
 *   <li>{@code /bi/home/todo} 待办事项</li>
 *   <li>{@code /bi/home/trend} 近7日交易趋势</li>
 * </ul>
 */
@RestController("biHomeController")
@RequestMapping("/bi/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeBiService homeBiService;

    /**
     * 实时概况: 今日 + 本月核心指标
     */
    @PostMapping("/overview")
    public PlatformResult<HomeOverviewVO> overview() {
        return PlatformResult.success(homeBiService.overview());
    }

    /**
     * 待办事项: 各状态待处理数量
     */
    @PostMapping("/todo")
    public PlatformResult<HomeTodoVO> todo() {
        return PlatformResult.success(homeBiService.todo());
    }

    /**
     * 近7日交易趋势: 订单量 + GMV 双曲线
     */
    @PostMapping("/trend")
    public PlatformResult<HomeTrendVO> trend() {
        return PlatformResult.success(homeBiService.trend());
    }
}
