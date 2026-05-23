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
import com.ruoyi.ai.domain.Herbs;
import com.ruoyi.ai.service.IHerbsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 中医药材主Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/herbs")
public class HerbsController extends BaseController
{
    @Autowired
    private IHerbsService herbsService;

    /**
     * 查询中医药材主列表
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:list')")
    @GetMapping("/list")
    public TableDataInfo list(Herbs herbs)
    {
        startPage();
        List<Herbs> list = herbsService.selectHerbsList(herbs);
        return getDataTable(list);
    }

    /**
     * 导出中医药材主列表
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:export')")
    @Log(title = "中医药材主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Herbs herbs)
    {
        List<Herbs> list = herbsService.selectHerbsList(herbs);
        ExcelUtil<Herbs> util = new ExcelUtil<Herbs>(Herbs.class);
        util.exportExcel(response, list, "中医药材主数据");
    }

    /**
     * 获取中医药材主详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(herbsService.selectHerbsById(id));
    }

    /**
     * 新增中医药材主
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:add')")
    @Log(title = "中医药材主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Herbs herbs)
    {
        return toAjax(herbsService.insertHerbs(herbs));
    }

    /**
     * 修改中医药材主
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:edit')")
    @Log(title = "中医药材主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Herbs herbs)
    {
        return toAjax(herbsService.updateHerbs(herbs));
    }

    /**
     * 删除中医药材主
     */
    @PreAuthorize("@ss.hasPermi('ai:herbs:remove')")
    @Log(title = "中医药材主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(herbsService.deleteHerbsByIds(ids));
    }
}
