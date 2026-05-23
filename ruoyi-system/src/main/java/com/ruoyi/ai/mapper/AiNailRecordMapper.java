package com.ruoyi.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.HealthDailyScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 指甲检测记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Mapper

public interface AiNailRecordMapper extends BaseMapper<AiNailRecord>
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
     * 删除指甲检测记录
     * 
     * @param id 指甲检测记录主键
     * @return 结果
     */
    public int deleteAiNailRecordById(Long id);

    /**
     * 批量删除指甲检测记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiNailRecordByIds(Long[] ids);

    AiNailRecord selectLatestByUserAfter(@Param("userId") Long userId, @Param("since")  Date sinceNail);

    @Select("SELECT * FROM ai_nail_record WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    AiNailRecord selectLatestByUserId(@Param("userId") Long userId);

    @Update("UPDATE ai_nail_record SET ai_diagnosis = #{text} WHERE id = #{id}")
    void updateDiagnosis(@Param("id") Long id, @Param("text") String text);

    List<HealthDailyScoreDO> selectDailyNailScore(@Param("userId") Long userId, @Param("start") LocalDateTime start,@Param("end") LocalDateTime end);
}
