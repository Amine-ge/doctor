package com.ruoyi.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.HealthDailyScoreDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 脸诊分析记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Mapper
public interface AiFaceRecordMapper  extends BaseMapper<AiFaceRecord>
{
    /**
     * 查询脸诊分析记录
     * 
     * @param id 脸诊分析记录主键
     * @return 脸诊分析记录
     */
    public AiFaceRecord selectAiFaceRecordById(Long id);

    /**
     * 查询脸诊分析记录列表
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 脸诊分析记录集合
     */
    public List<AiFaceRecord> selectAiFaceRecordList(AiFaceRecord aiFaceRecord);

    /**
     * 新增脸诊分析记录
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 结果
     */
    public int insertAiFaceRecord(AiFaceRecord aiFaceRecord);

    /**
     * 修改脸诊分析记录
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 结果
     */
    public int updateAiFaceRecord(AiFaceRecord aiFaceRecord);

    /**
     * 删除脸诊分析记录
     * 
     * @param id 脸诊分析记录主键
     * @return 结果
     */
    public int deleteAiFaceRecordById(Long id);

    /**
     * 批量删除脸诊分析记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiFaceRecordByIds(Long[] ids);

    AiFaceRecord selectLatestByUserAfter(@Param("userId") Long userId, @Param("since") Date sinceFace);

    @Select("SELECT * FROM ai_face_record WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    AiFaceRecord selectLatestByUserId(@Param("userId") Long userId);

    @Update("UPDATE ai_face_record SET ai_diagnosis = #{text} WHERE id = #{id}")
    void updateDiagnosis(@Param("id") Long id,@Param("text")  String text);

    List<HealthDailyScoreDO> selectDailyFaceScore(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
