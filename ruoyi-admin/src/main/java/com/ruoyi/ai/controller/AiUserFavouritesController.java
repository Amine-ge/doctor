package com.ruoyi.ai.controller;

import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.UserFavorites;
import com.ruoyi.ai.service.IUserFavoritesService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.utils.UserHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ai/userFavorites")
public class AiUserFavouritesController {
    @Autowired
    private IUserFavoritesService userFavoritesService;

    /**
     * 切换收藏状态（收藏 / 取消）
     */
    @PostMapping("/toggle")
    public AjaxResult toggleFavorite(@RequestBody UserFavorites dto) {
        AiUser user = UserHold.get();
        if (user.getId() == null) {
            return AjaxResult.error("请先登录");
        }
        dto.setUserId(user.getId());
        // 查询是否已收藏
        UserFavorites query = new UserFavorites();
        query.setUserId(user.getId());
        query.setHerbId(dto.getHerbId());
        List<UserFavorites> exists = userFavoritesService.selectUserFavoritesList(query);

        if (!exists.isEmpty()) {
            // 已存在：更新 status
            UserFavorites existing = exists.get(0);
            existing.setStatus(dto.getStatus()); // 1=收藏, 0=取消
            userFavoritesService.updateUserFavorites(existing);
        } else {
            // 不存在：新增
            dto.setStatus(1L); // 默认收藏
            userFavoritesService.insertUserFavorites(dto);
        }

        return AjaxResult.success();
    }

    /**
     * 查询当前用户的收藏列表（用于“我的收藏”页面）
     */
    @GetMapping("/list")
    public AjaxResult getMyFavorites() {
        AiUser user = UserHold.get();
        if (user.getId() == null) {
            return AjaxResult.error("请先登录");
        }

        UserFavorites query = new UserFavorites();
        query.setUserId(user.getId());
        query.setStatus(1L);

        List<UserFavorites> list = userFavoritesService.selectUserFavoritesList(query);
        return AjaxResult.success(list);
    }

    @GetMapping("/my-favorite-ids")
    public AjaxResult getMyFavoriteHerbIds() {
        AiUser user = UserHold.get();
        if (user.getId() == null) {
            return AjaxResult.error("请先登录");
        }

        List<Long> herbIds = userFavoritesService.selectFavoriteHerbIdsByUserId(user.getId());
        return AjaxResult.success(herbIds);
    }
}
