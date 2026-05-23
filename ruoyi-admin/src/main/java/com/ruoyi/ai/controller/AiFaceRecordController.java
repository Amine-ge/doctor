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
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.service.IAiFaceRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 脸诊分析记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/FaceRecord")
public class AiFaceRecordController extends BaseController
{
    @Autowired
    private IAiFaceRecordService aiFaceRecordService;

    /**
     * 查询脸诊分析记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiFaceRecord aiFaceRecord)
    {
        startPage();
        List<AiFaceRecord> list = aiFaceRecordService.selectAiFaceRecordList(aiFaceRecord);
        return getDataTable(list);
    }

    /**
     * 导出脸诊分析记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:export')")
    @Log(title = "脸诊分析记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiFaceRecord aiFaceRecord)
    {
        List<AiFaceRecord> list = aiFaceRecordService.selectAiFaceRecordList(aiFaceRecord);
        ExcelUtil<AiFaceRecord> util = new ExcelUtil<AiFaceRecord>(AiFaceRecord.class);
        util.exportExcel(response, list, "脸诊分析记录数据");
    }

    /**
     * 获取脸诊分析记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiFaceRecordService.selectAiFaceRecordById(id));
    }

    /**
     * 新增脸诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:add')")
    @Log(title = "脸诊分析记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiFaceRecord aiFaceRecord)
    {
        return toAjax(aiFaceRecordService.insertAiFaceRecord(aiFaceRecord));
    }

    /**
     * 修改脸诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:edit')")
    @Log(title = "脸诊分析记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiFaceRecord aiFaceRecord)
    {
        return toAjax(aiFaceRecordService.updateAiFaceRecord(aiFaceRecord));
    }

    /**
     * 删除脸诊分析记录
     */
    @PreAuthorize("@ss.hasPermi('ai:FaceRecord:remove')")
    @Log(title = "脸诊分析记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiFaceRecordService.deleteAiFaceRecordByIds(ids));
    }
}
