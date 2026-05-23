package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.vo.NailVo;

import java.util.List;

/**
 * 指甲检测记录Service接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
public interface IAiNailRecordService 
{
    /**
     * 查询指甲检测记录
     * 
     * @param id 指甲检测记录主键
     * @return 指甲检测记录
     */
    public AiNailRecord selectAiNailRecordById(Long id);

    /**
     * 查询指甲检测记录列表
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 指甲检测记录集合
     */
    public List<AiNailRecord> selectAiNailRecordList(AiNailRecord aiNailRecord);

    /**
     * 新增指甲检测记录
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 结果
     */
    public int insertAiNailRecord(AiNailRecord aiNailRecord);

    /**
     * 修改指甲检测记录
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 结果
     */
    public int updateAiNailRecord(AiNailRecord aiNailRecord);

    /**
     * 批量删除指甲检测记录
     * 
     * @param ids 需要删除的指甲检测记录主键集合
     * @return 结果
     */
    public int deleteAiNailRecordByIds(Long[] ids);

    /**
     * 删除指甲检测记录信息
     * 
     * @param id 指甲检测记录主键
     * @return 结果
     */
    public int deleteAiNailRecordById(Long id);

    List<NailVo> listByUser(Long userId);

    NailVo getDetail(Long id);
}
