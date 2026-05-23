package com.ruoyi.ai.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.vo.TongguVo;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.service.IAiTongueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Service
public class AiTongueRecordServiceImpl extends ServiceImpl<AiTongueRecordMapper, AiTongueRecord> implements  IAiTongueRecordService
{
    @Autowired
    private AiTongueRecordMapper aiTongueRecordMapper;

    /**
     * 查询舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param id 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键
     * @return 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     */
    @Override
    public AiTongueRecord selectAiTongueRecordById(Long id)
    {
        return aiTongueRecordMapper.selectAiTongueRecordById(id);
    }

    /**
     * 查询舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）列表
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     */
    @Override
    public List<AiTongueRecord> selectAiTongueRecordList(AiTongueRecord aiTongueRecord)
    {
        return aiTongueRecordMapper.selectAiTongueRecordList(aiTongueRecord);
    }

    /**
     * 新增舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 结果
     */
    @Override
    public int insertAiTongueRecord(AiTongueRecord aiTongueRecord)
    {
        return aiTongueRecordMapper.insertAiTongueRecord(aiTongueRecord);
    }

    /**
     * 修改舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param aiTongueRecord 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * @return 结果
     */
    @Override
    public int updateAiTongueRecord(AiTongueRecord aiTongueRecord)
    {
        return aiTongueRecordMapper.updateAiTongueRecord(aiTongueRecord);
    }

    /**
     * 批量删除舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）
     * 
     * @param ids 需要删除的舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键
     * @return 结果
     */
    @Override
    public int deleteAiTongueRecordByIds(Long[] ids)
    {
        return aiTongueRecordMapper.deleteAiTongueRecordByIds(ids);
    }

    /**
     * 删除舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）信息
     * 
     * @param id 舌诊分析记录（包含质量检测、舌象特征、AI诊断结果与图片URL）主键
     * @return 结果
     */
    @Override
    public int deleteAiTongueRecordById(Long id)
    {
        return aiTongueRecordMapper.deleteAiTongueRecordById(id);
    }

    @Override
    public List<TongguVo> listByUser(Long userId) {
        List<AiTongueRecord> list = aiTongueRecordMapper.selectAiTongueRecordList(new AiTongueRecord(){{setUserId(userId);}});

        return list.stream()
                .map(this::toVo)
                .toList();
    }

    @Override
    public TongguVo getDetail(Long id) {
        AiTongueRecord record =aiTongueRecordMapper.selectAiTongueRecordById(id);
        if (record == null) {
            return null;
        }
        TongguVo vo = toVo(record);
        vo.setAiDiagnosis(record.getAiDiagnosis());
        return vo;
    }
    private TongguVo toVo(AiTongueRecord r) {
        TongguVo vo = new TongguVo();
        vo.setId(r.getId());
        vo.setImageUrl(r.getImageUrl());

        // -------- 字段翻译示例 --------
        vo.setTongueColor(r.getTongueColor());
        vo.setCoatingColor(r.getCoatingColor());
        vo.setCoatingThickness(r.getCoatingThickness());
        vo.setMoisture(r.getMoisture());
        vo.setFissures(r.getFissures());
        vo.setTeethMarks(r.getTeethMarks());
        vo.setSurfaceTexture(r.getSurfaceTexture());
        vo.setCreatedAt(r.getCreatedAt());
        vo.setHealthScore(r.getHealthScore());

        BigDecimal score = r.getTqQualityScore(); // BigDecimal

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
