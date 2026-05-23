package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.UserFavorites;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 用户收藏Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public interface UserFavoritesMapper 
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
     * 删除用户收藏
     * 
     * @param id 用户收藏主键
     * @return 结果
     */
    public int deleteUserFavoritesById(Long id);

    /**
     * 批量删除用户收藏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFavoritesByIds(Long[] ids);

    List<Long> selectFavoriteHerbIdsByUserId(@Param("userId") Long userId);
}
