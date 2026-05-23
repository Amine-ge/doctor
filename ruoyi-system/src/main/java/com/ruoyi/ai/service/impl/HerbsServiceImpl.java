package com.ruoyi.ai.service.impl;

import java.util.List;

import com.ruoyi.ai.service.IHerbsService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.HerbsMapper;
import com.ruoyi.ai.domain.Herbs;

/**
 * 中医药材主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@Service
public class HerbsServiceImpl implements IHerbsService
{
    @Autowired
    private HerbsMapper herbsMapper;

    /**
     * 查询中医药材主
     * 
     * @param id 中医药材主主键
     * @return 中医药材主
     */
    @Override
    public Herbs selectHerbsById(Long id)
    {
        return herbsMapper.selectHerbsById(id);
    }

    /**
     * 查询中医药材主列表
     * 
     * @param herbs 中医药材主
     * @return 中医药材主
     */
    @Override
    public List<Herbs> selectHerbsList(Herbs herbs)
    {
        return herbsMapper.selectHerbsList(herbs);
    }

    /**
     * 新增中医药材主
     * 
     * @param herbs 中医药材主
     * @return 结果
     */
    @Override
    public int insertHerbs(Herbs herbs)
    {
        herbs.setCreateTime(DateUtils.getNowDate());
        return herbsMapper.insertHerbs(herbs);
    }

    /**
     * 修改中医药材主
     * 
     * @param herbs 中医药材主
     * @return 结果
     */
    @Override
    public int updateHerbs(Herbs herbs)
    {
        herbs.setUpdateTime(DateUtils.getNowDate());
        return herbsMapper.updateHerbs(herbs);
    }

    /**
     * 批量删除中医药材主
     * 
     * @param ids 需要删除的中医药材主主键
     * @return 结果
     */
    @Override
    public int deleteHerbsByIds(Long[] ids)
    {
        return herbsMapper.deleteHerbsByIds(ids);
    }

    /**
     * 删除中医药材主信息
     * 
     * @param id 中医药材主主键
     * @return 结果
     */
    @Override
    public int deleteHerbsById(Long id)
    {
        return herbsMapper.deleteHerbsById(id);
    }
}
