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
import com.ruoyi.ai.domain.UserFavorites;
import com.ruoyi.ai.service.IUserFavoritesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户收藏Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/favorites")
public class UserFavoritesController extends BaseController
{
    @Autowired
    private IUserFavoritesService userFavoritesService;

    /**
     * 查询用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserFavorites userFavorites)
    {
        startPage();
        List<UserFavorites> list = userFavoritesService.selectUserFavoritesList(userFavorites);
        return getDataTable(list);
    }

    /**
     * 导出用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:export')")
    @Log(title = "用户收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFavorites userFavorites)
    {
        List<UserFavorites> list = userFavoritesService.selectUserFavoritesList(userFavorites);
        ExcelUtil<UserFavorites> util = new ExcelUtil<UserFavorites>(UserFavorites.class);
        util.exportExcel(response, list, "用户收藏数据");
    }

    /**
     * 获取用户收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userFavoritesService.selectUserFavoritesById(id));
    }

    /**
     * 新增用户收藏
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:add')")
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFavorites userFavorites)
    {
        return toAjax(userFavoritesService.insertUserFavorites(userFavorites));
    }

    /**
     * 修改用户收藏
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:edit')")
    @Log(title = "用户收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFavorites userFavorites)
    {
        return toAjax(userFavoritesService.updateUserFavorites(userFavorites));
    }

    /**
     * 删除用户收藏
     */
    @PreAuthorize("@ss.hasPermi('ai:favorites:remove')")
    @Log(title = "用户收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userFavoritesService.deleteUserFavoritesByIds(ids));
    }
}
