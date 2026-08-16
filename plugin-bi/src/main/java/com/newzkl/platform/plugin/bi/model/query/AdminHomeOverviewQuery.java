package com.newzkl.platform.plugin.bi.model.query;

import cn.hutool.core.date.DateTime;
import com.newzkl.platform.base.common.ddd.model.query.QuerySupport;
import lombok.Data;

@Data
public class AdminHomeOverviewQuery extends QuerySupport {
	
	private Long userId;
	private DateTime startTime;
	private DateTime endTime;
	
	public DateTime getEndTime() {
		DateTime now = DateTime.now();
		if (endTime == null) return now;
		return now.isBefore(endTime) ? now : endTime;
	}
	

}
