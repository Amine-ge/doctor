package com.ruoyi.ai.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.service.IAiTongueRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 舌诊分析记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/TongueRecord")
public class AiTongueRecordController extends BaseController
{
    @Autowired
    private IAiTongueRecordService aiTongueRecordService;

    /**
     * 查询舌诊分析记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiTongueRecord aiTongueRecord)
    {
        startPage();
        List<AiTongueRecord> list = aiTongueRecordService.selectAiTongueRecordList(aiTongueRecord);
        return getDataTable(list);
    }

    /**
     * 导出舌诊分析记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:export')")
    @Log(title = "舌诊分析记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiTongueRecord aiTongueRecord)
    {
        List<AiTongueRecord> list = aiTongueRecordService.selectAiTongueRecordList(aiTongueRecord);
        ExcelUtil<AiTongueRecord> util = new ExcelUtil<AiTongueRecord>(AiTongueRecord.class);
        util.exportExcel(response, list, "舌诊分析记录数据");
    }

    /**
     * 获取舌诊分析记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiTongueRecordService.selectAiTongueRecordById(id));
    }

    /**
     * 新增舌诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:add')")
    @Log(title = "舌诊分析记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiTongueRecord aiTongueRecord)
    {
        return toAjax(aiTongueRecordService.insertAiTongueRecord(aiTongueRecord));
    }

    /**
     * 修改舌诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:edit')")
    @Log(title = "舌诊分析记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiTongueRecord aiTongueRecord)
    {
        return toAjax(aiTongueRecordService.updateAiTongueRecord(aiTongueRecord));
    }

    /**
     * 删除舌诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:TongueRecord:remove')")
    @Log(title = "舌诊分析记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiTongueRecordService.deleteAiTongueRecordByIds(ids));
    }
}
