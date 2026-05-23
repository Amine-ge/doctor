package com.ruoyi.ai.service.impl;

import java.util.List;

import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiSymptomReport;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.vo.AiSymptomReportVo;
import com.ruoyi.ai.domain.vo.FaceVo;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.mapper.AiSymptomReportMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.service.IAiSymptomReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 症状自述（用户主观症状记录 + 可选AI总结/复核信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Service
public class AiSymptomReportServiceImpl implements IAiSymptomReportService
{
    @Autowired
    private AiSymptomReportMapper aiSymptomReportMapper;
    @Autowired
    private AiFaceRecordMapper aiFaceRecordMapper;
    @Autowired
    private AiNailRecordMapper aiNailRecordMapper;
    @Autowired
    private AiTongueRecordMapper aiTongueRecordMapper;



    /**
     * 查询症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param id 症状自述（用户主观症状记录 + 可选AI总结/复核信息）主键
     * @return 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     */
    @Override
    public AiSymptomReport selectAiSymptomReportById(Long id)
    {
        return aiSymptomReportMapper.selectAiSymptomReportById(id);
    }

    /**
     * 查询症状自述（用户主观症状记录 + 可选AI总结/复核信息）列表
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     */
    @Override
    public List<AiSymptomReport> selectAiSymptomReportList(AiSymptomReport aiSymptomReport)
    {
        return aiSymptomReportMapper.selectAiSymptomReportList(aiSymptomReport);
    }

    /**
     * 新增症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 结果
     */
    @Override
    public int insertAiSymptomReport(AiSymptomReport aiSymptomReport)
    {
        return aiSymptomReportMapper.insertAiSymptomReport(aiSymptomReport);
    }

    /**
     * 修改症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param aiSymptomReport 症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * @return 结果
     */
    @Override
    public int updateAiSymptomReport(AiSymptomReport aiSymptomReport)
    {
        return aiSymptomReportMapper.updateAiSymptomReport(aiSymptomReport);
    }

    /**
     * 批量删除症状自述（用户主观症状记录 + 可选AI总结/复核信息）
     * 
     * @param ids 需要删除的症状自述（用户主观症状记录 + 可选AI总结/复核信息）主键
     * @return 结果
     */
    @Override
    public int deleteAiSymptomReportByIds(Long[] ids)
    {
        return aiSymptomReportMapper.deleteAiSymptomReportByIds(ids);
    }

    /**
     * 删除症状自述（用户主观症状记录 + 可选AI总结/复核信息）信息
     * 
     * @param id 症状自述（用户主观症状记录 + 可选AI总结/复核信息）主键
     * @return 结果
     */
    @Override
    public int deleteAiSymptomReportById(Long id)
    {
        return aiSymptomReportMapper.deleteAiSymptomReportById(id);
    }

    @Override
    public List<AiSymptomReportVo> listByUser(Long userId) {
        AiSymptomReport query = new AiSymptomReport();
        query.setUserId(userId);
        List<AiSymptomReport> list = aiSymptomReportMapper.selectAiSymptomReportList(query);

        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
                .map(this::toVo)
                .toList();
    }

    @Override
    public AiSymptomReportVo getDetail(Long userId, Long id) {
        AiSymptomReport report = aiSymptomReportMapper.selectAiSymptomReportById(id);
        if (report == null || !report.getUserId().equals(userId)) {
            return null;
        }

        AiSymptomReportVo vo = toVo(report);
        vo.setAiDiagnosis(report.getAiDiagnosis());

        AiFaceRecord faceRecord = null;
        if (report.getFaceRecordId() != null) {
            faceRecord = aiFaceRecordMapper.selectAiFaceRecordById(report.getFaceRecordId());
        }

        AiTongueRecord tongueRecord = null;
        if (report.getTongueRecordId() != null) {
            tongueRecord = aiTongueRecordMapper.selectAiTongueRecordById(report.getTongueRecordId());
        }

        AiNailRecord nailRecord = null;
        if (report.getNailRecordId() != null) {
            nailRecord = aiNailRecordMapper.selectAiNailRecordById(report.getNailRecordId());
        }

        vo.setTongueImageUrl(tongueRecord != null ? tongueRecord.getImageUrl() : null);
        vo.setFaceImageUrl(faceRecord != null ? faceRecord.getImageUrl() : null);
        vo.setNailImageUrl(nailRecord != null ? nailRecord.getImageUrl() : null);

        return vo;
    }

    private AiSymptomReportVo toVo(AiSymptomReport r) {
        AiSymptomReportVo vo = new AiSymptomReportVo();
        vo.setId(r.getId());
        vo.setMainSymptom(r.getMainSymptom());
        vo.setCreatedAt(r.getCreatedAt());

        vo.setTongueRecordId(r.getTongueRecordId());
        vo.setFaceRecordId(r.getFaceRecordId());
        vo.setNailRecordId(r.getNailRecordId());

        vo.setHasTongue(r.getTongueRecordId() != null);
        vo.setHasFace(r.getFaceRecordId() != null);
        vo.setHasNail(r.getNailRecordId() != null);
        vo.setHasAiDiagnosis(r.getAiDiagnosis() != null && !r.getAiDiagnosis().isEmpty());

        return vo;
    }

}
