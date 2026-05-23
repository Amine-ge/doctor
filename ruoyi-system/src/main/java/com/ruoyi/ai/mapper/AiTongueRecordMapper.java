package com.ruoyi.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.HealthDailyScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Mapper
public interface AiTongueRecordMapper  extends BaseMapper<AiTongueRecord>
{
    /**
     * 查询舌诊分析记录
     *
     * @param id 舌诊分析记录主键
     * @return 舌诊分析记录
     */
    public AiTongueRecord selectAiTongueRecordById(Long id);

    /**
     * 查询舌诊分析记录列表
     *
     * @param aiTongueRecord 舌诊分析记录
     * @return 舌诊分析记录集合
     */
    public List<AiTongueRecord> selectAiTongueRecordList(AiTongueRecord aiTongueRecord);

    /**
     * 新增舌诊分析记录
     *
     * @param aiTongueRecord 舌诊分析记录
     * @return 结果
     */
    public int insertAiTongueRecord(AiTongueRecord aiTongueRecord);

    /**
     * 修改舌诊分析记录
     *
     * @param aiTongueRecord 舌诊分析记录
     * @return 结果
     */
    public int updateAiTongueRecord(AiTongueRecord aiTongueRecord);

    /**
     * 删除舌诊分析记录
     *
     * @param id 舌诊分析记录主键
     * @return 结果
     */
    public int deleteAiTongueRecordById(Long id);

    /**
     * 批量删除舌诊分析记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiTongueRecordByIds(Long[] ids);

    // 舌诊
    AiTongueRecord selectLatestByUserAfter(@Param("userId") Long userId, @Param("since") Date since);

    @Select("SELECT * FROM ai_tongue_record WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    AiTongueRecord selectLatestByUserId(@Param("userId") Long userId);


    @Update("UPDATE ai_tongue_record SET ai_diagnosis = #{diagnosis} WHERE id = #{id}")
    void updateDiagnosis(@Param("id") Long id, @Param("diagnosis") String diagnosis);

    List<HealthDailyScoreDO> selectDailyTongueScore(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
