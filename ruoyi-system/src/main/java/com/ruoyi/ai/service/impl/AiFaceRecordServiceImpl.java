package com.ruoyi.ai.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.vo.FaceVo;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.service.IAiFaceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 脸诊分析记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Service
public class AiFaceRecordServiceImpl extends ServiceImpl<AiFaceRecordMapper, AiFaceRecord> implements IAiFaceRecordService
{
    @Autowired
    private AiFaceRecordMapper aiFaceRecordMapper;

    /**
     * 查询脸诊分析记录
     * 
     * @param id 脸诊分析记录主键
     * @return 脸诊分析记录
     */
    @Override
    public AiFaceRecord selectAiFaceRecordById(Long id)
    {
        return aiFaceRecordMapper.selectAiFaceRecordById(id);
    }

    /**
     * 查询脸诊分析记录列表
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 脸诊分析记录
     */
    @Override
    public List<AiFaceRecord> selectAiFaceRecordList(AiFaceRecord aiFaceRecord)
    {
        return aiFaceRecordMapper.selectAiFaceRecordList(aiFaceRecord);
    }

    /**
     * 新增脸诊分析记录
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 结果
     */
    @Override
    public int insertAiFaceRecord(AiFaceRecord aiFaceRecord)
    {
        return aiFaceRecordMapper.insertAiFaceRecord(aiFaceRecord);
    }

    /**
     * 修改脸诊分析记录
     * 
     * @param aiFaceRecord 脸诊分析记录
     * @return 结果
     */
    @Override
    public int updateAiFaceRecord(AiFaceRecord aiFaceRecord)
    {
        return aiFaceRecordMapper.updateAiFaceRecord(aiFaceRecord);
    }

    /**
     * 批量删除脸诊分析记录
     * 
     * @param ids 需要删除的脸诊分析记录主键
     * @return 结果
     */
    @Override
    public int deleteAiFaceRecordByIds(Long[] ids)
    {
        return aiFaceRecordMapper.deleteAiFaceRecordByIds(ids);
    }

    /**
     * 删除脸诊分析记录信息
     * 
     * @param id 脸诊分析记录主键
     * @return 结果
     */
    @Override
    public int deleteAiFaceRecordById(Long id)
    {
        return aiFaceRecordMapper.deleteAiFaceRecordById(id);
    }

    @Override
    public List<FaceVo> listByUser(Long userId) {
        List<AiFaceRecord> list = aiFaceRecordMapper.selectAiFaceRecordList(new AiFaceRecord(){{setUserId(userId);}});
        return list.stream().map(this::toVo).toList();
    }

    @Override
    public FaceVo getDetail(Long id) {
        AiFaceRecord record = aiFaceRecordMapper.selectAiFaceRecordById(id);
        if (record != null) {
            FaceVo vo = toVo(record);
            vo.setAiDiagnosis(record.getAiDiagnosis());
            return vo;
        }
        return null;
    }

    private FaceVo toVo(AiFaceRecord r) {
        FaceVo vo = new FaceVo();
        vo.setId(r.getId());
        vo.setImageUrl(r.getImageUrl());

        // -------- 字段翻译示例 --------
        vo.setSkinTone(r.getSkinTone());
        vo.setOilinessLevel(r.getOilinessLevel());
        vo.setDarkCirclesLevel(r.getDarkCirclesLevel());
        vo.setAcneLevel(r.getAcneLevel());
        vo.setLipColor(r.getLipColor());
        vo.setNotes(r.getNotes());
        vo.setCreatedAt(r.getCreatedAt());
        BigDecimal score = r.getFqQualityScore(); // BigDecimal

        if (score == null) {
            vo.setStatusText("未检测");
        } else if (score.compareTo(new BigDecimal("0.3")) < 0) {
            vo.setStatusText("图像较差");
        } else if (score.compareTo(new BigDecimal("0.6")) < 0) {
            vo.setStatusText("正常");
        } else {
            vo.setStatusText("良好");
        }
        return vo;
    }
}
