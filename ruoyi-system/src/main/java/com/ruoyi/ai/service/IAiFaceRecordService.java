package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.vo.FaceVo;

import java.util.List;

/**
 * 脸诊分析记录Service接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
public interface IAiFaceRecordService 
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
     * 批量删除脸诊分析记录
     * 
     * @param ids 需要删除的脸诊分析记录主键集合
     * @return 结果
     */
    public int deleteAiFaceRecordByIds(Long[] ids);

    /**
     * 删除脸诊分析记录信息
     * 
     * @param id 脸诊分析记录主键
     * @return 结果
     */
    public int deleteAiFaceRecordById(Long id);

    List<FaceVo> listByUser(Long userId);

    FaceVo getDetail(Long id);
}
