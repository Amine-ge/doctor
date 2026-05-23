package com.ruoyi.ai.mapper;

import com.ruoyi.ai.domain.AiSymptomReport;
import com.ruoyi.ai.domain.HealthDailyScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 症状自述（用户主观症状记录 + 可选AI总结/复核信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Mapper

public interface AiSymptomReportMapper 
{
    /**
     * 查询症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param id 症状自述（用户主观症状记录 + 可选AI总结/复核信息）主键
     * @return 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     */
    public AiSymptomReport selectAiSymptomReportById(Long id);

    /**
     * 查询症状自述（用户主观症状记录 + 可选AI总结/复核信息）列表
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 症状自述（用户主观症状记录 + 可选AI总结/复核信息）集合
     */
    public List<AiSymptomReport> selectAiSymptomReportList(AiSymptomReport aiSymptomReport);

    /**
     * 新增症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 结果
     */
    public int insertAiSymptomReport(AiSymptomReport aiSymptomReport);

    /**
     * 修改症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 结果
     */
    public int updateAiSymptomReport(AiSymptomReport aiSymptomReport);

    /**
     * 删除症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param id 症状自述（用户主观症状记录 + 可选AI总结/复核信息）主键
     * @return 结果
     */
    public int deleteAiSymptomReportById(Long id);

    /**
     * 批量删除症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiSymptomReportByIds(Long[] ids);
    List<HealthDailyScoreDO> selectDailyTotalScore(@Param("userId") Long userId,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);
}
