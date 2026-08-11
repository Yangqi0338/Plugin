package com.newzkl.platform.plugin.openapi.model.entity;

import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import lombok.Data;

/**
* 开发者
* @author fang
*/
@Data
public class Developer{
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 开发者ID
	 */
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

	public void init() {
		this.id = SnowflakeGenerator.getSnowflakeId();
	}
}