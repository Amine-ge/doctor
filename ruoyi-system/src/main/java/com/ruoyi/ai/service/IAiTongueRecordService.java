package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.vo.TongguVo;

import java.util.List;

/**
 * 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）Service接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
public interface IAiTongueRecordService 
{
    /**
     * 查询舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param id 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键
     * @return 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     */
    public AiTongueRecord selectAiTongueRecordById(Long id);

    /**
     * 查询舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）列表
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）集合
     */
    public List<AiTongueRecord> selectAiTongueRecordList(AiTongueRecord aiTongueRecord);

    /**
     * 新增舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 结果
     */
    public int insertAiTongueRecord(AiTongueRecord aiTongueRecord);

    /**
     * 修改舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 结果
     */
    public int updateAiTongueRecord(AiTongueRecord aiTongueRecord);

    /**
     * 批量删除舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param ids 需要删除的舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键集合
     * @return 结果
     */
    public int deleteAiTongueRecordByIds(Long[] ids);

    /**
     * 删除舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）信息
     * 
     * @param id 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键
     * @return 结果
     */
    public int deleteAiTongueRecordById(Long id);

    List<TongguVo> listByUser(Long userId);

    TongguVo getDetail(Long id);
}
