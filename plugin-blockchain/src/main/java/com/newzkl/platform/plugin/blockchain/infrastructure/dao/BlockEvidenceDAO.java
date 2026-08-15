package com.newzkl.platform.plugin.blockchain.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.plugin.blockchain.infrastructure.entity.BlockEvidenceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知链存证记录 DAO
 *
 * @author KC
 */
@Mapper
public interface BlockEvidenceDAO extends BaseMapper<BlockEvidenceDO> {
}
