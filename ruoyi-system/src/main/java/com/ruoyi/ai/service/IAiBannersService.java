package com.ruoyi.ai.service;

import java.util.List;
import com.ruoyi.ai.domain.AiBanners;

/**
 * 轮播图管理Service接口
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public interface IAiBannersService 
{
    /**
     * 查询轮播图管理
     * 
     * @param id 轮播图管理主键
     * @return 轮播图管理
     */
    public AiBanners selectAiBannersById(Long id);

    /**
     * 查询轮播图管理列表
     * 
     * @param aiBanners 轮播图管理
     * @return 轮播图管理集合
     */
    public List<AiBanners> selectAiBannersList(AiBanners aiBanners);

    /**
     * 新增轮播图管理
     * 
     * @param aiBanners 轮播图管理
     * @return 结果
     */
    public int insertAiBanners(AiBanners aiBanners);

    /**
     * 修改轮播图管理
     * 
     * @param aiBanners 轮播图管理
     * @return 结果
     */
    public int updateAiBanners(AiBanners aiBanners);

    /**
     * 批量删除轮播图管理
     * 
     * @param ids 需要删除的轮播图管理主键集合
     * @return 结果
     */
    public int deleteAiBannersByIds(Long[] ids);

    /**
     * 删除轮播图管理信息
     * 
     * @param id 轮播图管理主键
     * @return 结果
     */
    public int deleteAiBannersById(Long id);
}
