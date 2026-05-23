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
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.service.IAiUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@RestController
@RequestMapping("/ai/user")
public class AiUserController extends BaseController
{
    @Autowired
    private IAiUserService aiUserService;

    /**
     * 查询用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiUser aiUser)
    {
        startPage();
        List<AiUser> list = aiUserService.selectAiUserList(aiUser);
        return getDataTable(list);
    }

    /**
     * 导出用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:user:export')")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AiUser aiUser)
    {
        List<AiUser> list = aiUserService.selectAiUserList(aiUser);
        ExcelUtil<AiUser> util = new ExcelUtil<AiUser>(AiUser.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    /**
     * 获取用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(aiUserService.selectAiUserById(id));
    }

    /**
     * 新增用户信息
     */
    @PreAuthorize("@ss.hasPermi('ai:user:add')")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiUser aiUser)
    {
        return toAjax(aiUserService.insertAiUser(aiUser));
    }

    /**
     * 修改用户信息
     */
    @PreAuthorize("@ss.hasPermi('ai:user:edit')")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiUser aiUser)
    {
        return toAjax(aiUserService.updateAiUser(aiUser));
    }

    /**
     * 删除用户信息
     */
    @PreAuthorize("@ss.hasPermi('ai:user:remove')")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(aiUserService.deleteAiUserByIds(ids));
    }
}
