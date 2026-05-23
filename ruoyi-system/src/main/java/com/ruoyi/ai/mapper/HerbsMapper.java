package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.Herbs;

/**
 * 中医药材主Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public interface HerbsMapper 
{
    /**
     * 查询中医药材主
     * 
     * @param id 中医药材主主键
     * @return 中医药材主
     */
    public Herbs selectHerbsById(Long id);

    /**
     * 查询中医药材主列表
     * 
     * @param herbs 中医药材主
     * @return 中医药材主集合
     */
    public List<Herbs> selectHerbsList(Herbs herbs);

    /**
     * 新增中医药材主
     * 
     * @param herbs 中医药材主
     * @return 结果
     */
    public int insertHerbs(Herbs herbs);

    /**
     * 修改中医药材主
     * 
     * @param herbs 中医药材主
     * @return 结果
     */
    public int updateHerbs(Herbs herbs);

    /**
     * 删除中医药材主
     * 
     * @param id 中医药材主主键
     * @return 结果
     */
    public int deleteHerbsById(Long id);

    /**
     * 批量删除中医药材主
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHerbsByIds(Long[] ids);
}
