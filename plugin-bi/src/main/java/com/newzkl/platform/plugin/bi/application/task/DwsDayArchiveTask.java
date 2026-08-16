package com.newzkl.platform.plugin.bi.application.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * BI 日宽表归档任务
 *
 * <p>每日 00:30 执行: 把实时宽表(当日补偿增量)复制进日宽表(bizDate=昨天), 然后清空实时宽表。
 * 日宽表与实时宽表共享主体(补偿增量), 区别仅时间维度(biz_date)。</p>
 *
 * <p>宽表清单随 {@code @BITableName} 实体新增而扩充, 每个实时/日宽表各归档一次。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DwsDayArchiveTask {

    private final BiArchiveService biArchiveService;

    @XxlJob("biDayArchive")
    public void archive() {
        biArchiveService.archiveAll();
    }
}
