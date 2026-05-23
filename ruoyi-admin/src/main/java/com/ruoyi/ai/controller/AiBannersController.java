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
import com.ruoyi.ai.domain.AiBanners;
import com.ruoyi.ai.service.IAiBannersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 轮播图管理Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/banners")
public class AiBannersController extends BaseController
{
    @Autowired
    private IAiBannersService aiBannersService;

    /**
     * 查询轮播图管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiBanners aiBanners)
    {
        startPage();
        List<AiBanners> list = aiBannersService.selectAiBannersList(aiBanners);
        return getDataTable(list);
    }

    /**
     * 导出轮播图管理列表
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:export')")
    @Log(title = "轮播图管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiBanners aiBanners)
    {
        List<AiBanners> list = aiBannersService.selectAiBannersList(aiBanners);
        ExcelUtil<AiBanners> util = new ExcelUtil<AiBanners>(AiBanners.class);
        util.exportExcel(response, list, "轮播图管理数据");
    }

    /**
     * 获取轮播图管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiBannersService.selectAiBannersById(id));
    }

    /**
     * 新增轮播图管理
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:add')")
    @Log(title = "轮播图管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiBanners aiBanners)
    {
        return toAjax(aiBannersService.insertAiBanners(aiBanners));
    }

    /**
     * 修改轮播图管理
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:edit')")
    @Log(title = "轮播图管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiBanners aiBanners)
    {
        return toAjax(aiBannersService.updateAiBanners(aiBanners));
    }

    /**
     * 删除轮播图管理
     */
    @PreAuthorize("@ss.hasPermi('ai:banners:remove')")
    @Log(title = "轮播图管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiBannersService.deleteAiBannersByIds(ids));
    }
}
