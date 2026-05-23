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
import com.ruoyi.ai.domain.AiSymptomReport;
import com.ruoyi.ai.service.IAiSymptomReportService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 症状自述Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/SymptomReport")
public class AiSymptomReportController extends BaseController
{
    @Autowired
    private IAiSymptomReportService aiSymptomReportService;

    /**
     * 查询症状自述列表
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiSymptomReport aiSymptomReport)
    {
        startPage();
        List<AiSymptomReport> list = aiSymptomReportService.selectAiSymptomReportList(aiSymptomReport);
        return getDataTable(list);
    }

    /**
     * 导出症状自述列表
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:export')")
    @Log(title = "症状自述", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiSymptomReport aiSymptomReport)
    {
        List<AiSymptomReport> list = aiSymptomReportService.selectAiSymptomReportList(aiSymptomReport);
        ExcelUtil<AiSymptomReport> util = new ExcelUtil<AiSymptomReport>(AiSymptomReport.class);
        util.exportExcel(response, list, "症状自述数据");
    }

    /**
     * 获取症状自述详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiSymptomReportService.selectAiSymptomReportById(id));
    }

    /**
     * 新增症状自述
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:add')")
    @Log(title = "症状自述", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiSymptomReport aiSymptomReport)
    {
        return toAjax(aiSymptomReportService.insertAiSymptomReport(aiSymptomReport));
    }

    /**
     * 修改症状自述
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:edit')")
    @Log(title = "症状自述", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiSymptomReport aiSymptomReport)
    {
        return toAjax(aiSymptomReportService.updateAiSymptomReport(aiSymptomReport));
    }

    /**
     * 删除症状自述
     */
    @PreAuthorize("@ss.hasPermi('ai:SymptomReport:remove')")
    @Log(title = "症状自述", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiSymptomReportService.deleteAiSymptomReportByIds(ids));
    }
}
