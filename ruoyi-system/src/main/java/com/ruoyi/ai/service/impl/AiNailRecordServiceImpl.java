package com.ruoyi.ai.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.vo.NailVo;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.service.IAiNailRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 指甲检测记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Service
public class AiNailRecordServiceImpl extends ServiceImpl<AiNailRecordMapper,AiNailRecord> implements IAiNailRecordService
{
    @Autowired
    private AiNailRecordMapper aiNailRecordMapper;

    /**
     * 查询指甲检测记录
     * 
     * @param id 指甲检测记录主键
     * @return 指甲检测记录
     */
    @Override
    public AiNailRecord selectAiNailRecordById(Long id)
    {
        return aiNailRecordMapper.selectAiNailRecordById(id);
    }

    /**
     * 查询指甲检测记录列表
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 指甲检测记录
     */
    @Override
    public List<AiNailRecord> selectAiNailRecordList(AiNailRecord aiNailRecord)
    {
        return aiNailRecordMapper.selectAiNailRecordList(aiNailRecord);
    }

    /**
     * 新增指甲检测记录
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 结果
     */
    @Override
    public int insertAiNailRecord(AiNailRecord aiNailRecord)
    {
        return aiNailRecordMapper.insertAiNailRecord(aiNailRecord);
    }

    /**
     * 修改指甲检测记录
     * 
     * @param aiNailRecord 指甲检测记录
     * @return 结果
     */
    @Override
    public int updateAiNailRecord(AiNailRecord aiNailRecord)
    {
        return aiNailRecordMapper.updateAiNailRecord(aiNailRecord);
    }

    /**
     * 批量删除指甲检测记录
     * 
     * @param ids 需要删除的指甲检测记录主键
     * @return 结果
     */
    @Override
    public int deleteAiNailRecordByIds(Long[] ids)
    {
        return aiNailRecordMapper.deleteAiNailRecordByIds(ids);
    }

    /**
     * 删除指甲检测记录信息
     * 
     * @param id 指甲检测记录主键
     * @return 结果
     */
    @Override
    public int deleteAiNailRecordById(Long id)
    {
        return aiNailRecordMapper.deleteAiNailRecordById(id);
    }

    @Override
    public List<NailVo> listByUser(Long userId) {
        AiNailRecord query = new AiNailRecord();
        query.setUserId(userId);
        List<AiNailRecord> records = aiNailRecordMapper.selectAiNailRecordList(query);
        return records.stream().map(this::toVo).toList();
    }

    @Override
    public NailVo getDetail(Long id) {
        AiNailRecord byId = aiNailRecordMapper.selectAiNailRecordById(id);
        if (byId != null) {
            NailVo vo = toVo(byId);
            vo.setAiDiagnosis(byId.getAiDiagnosis());
            return vo;
        }
        return null;
    }
    private NailVo toVo(AiNailRecord r) {
        NailVo vo = new NailVo();
        vo.setId(r.getId());
        vo.setImageUrl(r.getImageUrl());

        // -------- 字段翻译示例 --------
        vo.setNailColor(r.getNailColor());
        vo.setNailTexture(r.getNailTexture());
        vo.setNailShape(r.getNailShape());
        vo.setNailShape(r.getLunulaVisibility());
        vo.setBreakability(r.getBreakability());
        vo.setNotes(r.getNotes());
        vo.setCreatedAt(r.getCreatedAt());

        BigDecimal score = r.getNqQualityScore(); // BigDecimal

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
