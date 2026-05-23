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
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.service.IAiNailRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 指甲检测记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/NailRecord")
public class AiNailRecordController extends BaseController
{
    @Autowired
    private IAiNailRecordService aiNailRecordService;

    /**
     * 查询指甲检测记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiNailRecord aiNailRecord)
    {
        startPage();
        List<AiNailRecord> list = aiNailRecordService.selectAiNailRecordList(aiNailRecord);
        return getDataTable(list);
    }

    /**
     * 导出指甲检测记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:export')")
    @Log(title = "指甲检测记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiNailRecord aiNailRecord)
    {
        List<AiNailRecord> list = aiNailRecordService.selectAiNailRecordList(aiNailRecord);
        ExcelUtil<AiNailRecord> util = new ExcelUtil<AiNailRecord>(AiNailRecord.class);
        util.exportExcel(response, list, "指甲检测记录数据");
    }

    /**
     * 获取指甲检测记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiNailRecordService.selectAiNailRecordById(id));
    }

    /**
     * 新增指甲检测记录
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:add')")
    @Log(title = "指甲检测记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiNailRecord aiNailRecord)
    {
        return toAjax(aiNailRecordService.insertAiNailRecord(aiNailRecord));
    }

    /**
     * 修改指甲检测记录
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:edit')")
    @Log(title = "指甲检测记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiNailRecord aiNailRecord)
    {
        return toAjax(aiNailRecordService.updateAiNailRecord(aiNailRecord));
    }

    /**
     * 删除指甲检测记录
     */
    @PreAuthorize("@ss.hasPermi('ai:NailRecord:remove')")
    @Log(title = "指甲检测记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiNailRecordService.deleteAiNailRecordByIds(ids));
    }
}
