package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.UserFavorites;

/**
 * 用户收藏Service接口
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public interface IUserFavoritesService 
{
    /**
     * 查询用户收藏
     * 
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    public UserFavorites selectUserFavoritesById(Long id);

    /**
     * 查询用户收藏列表
     * 
     * @param userFavorites 用户收藏
     * @return 用户收藏集合
     */
    public List<UserFavorites> selectUserFavoritesList(UserFavorites userFavorites);

    /**
     * 新增用户收藏
     * 
     * @param userFavorites 用户收藏
     * @return 结果
     */
    public int insertUserFavorites(UserFavorites userFavorites);

    /**
     * 修改用户收藏
     * 
     * @param userFavorites 用户收藏
     * @return 结果
     */
    public int updateUserFavorites(UserFavorites userFavorites);

    /**
     * 批量删除用户收藏
     * 
     * @param ids 需要删除的用户收藏主键集合
     * @return 结果
     */
    public int deleteUserFavoritesByIds(Long[] ids);

    /**
     * 删除用户收藏信息
     * 
     * @param id 用户收藏主键
     * @return 结果
     */
    public int deleteUserFavoritesById(Long id);
    /**
     * 根据用户ID获取收藏的药材ID列表
     *
     * @param userId 用户ID
     * @return 收藏的药材ID列表
     */
    List<Long> selectFavoriteHerbIdsByUserId(Long userId);
}
