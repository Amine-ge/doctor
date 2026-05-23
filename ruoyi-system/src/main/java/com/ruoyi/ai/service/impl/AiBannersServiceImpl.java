package com.ruoyi.ai.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.AiBannersMapper;
import com.ruoyi.ai.domain.AiBanners;
import com.ruoyi.ai.service.IAiBannersService;

/**
 * 轮播图管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
@Service
public class AiBannersServiceImpl implements IAiBannersService 
{
    @Autowired
    private AiBannersMapper aiBannersMapper;

    /**
     * 查询轮播图管理
     * 
     * @param id 轮播图管理主键
     * @return 轮播图管理
     */
    @Override
    public AiBanners selectAiBannersById(Long id)
    {
        return aiBannersMapper.selectAiBannersById(id);
    }

    /**
     * 查询轮播图管理列表
     * 
     * @param aiBanners 轮播图管理
     * @return 轮播图管理
     */
    @Override
    public List<AiBanners> selectAiBannersList(AiBanners aiBanners)
    {
        return aiBannersMapper.selectAiBannersList(aiBanners);
    }

    /**
     * 新增轮播图管理
     * 
     * @param aiBanners 轮播图管理
     * @return 结果
     */
    @Override
    public int insertAiBanners(AiBanners aiBanners)
    {
        return aiBannersMapper.insertAiBanners(aiBanners);
    }

    /**
     * 修改轮播图管理
     * 
     * @param aiBanners 轮播图管理
     * @return 结果
     */
    @Override
    public int updateAiBanners(AiBanners aiBanners)
    {
        return aiBannersMapper.updateAiBanners(aiBanners);
    }

    /**
     * 批量删除轮播图管理
     * 
     * @param ids 需要删除的轮播图管理主键
     * @return 结果
     */
    @Override
    public int deleteAiBannersByIds(Long[] ids)
    {
        return aiBannersMapper.deleteAiBannersByIds(ids);
    }

    /**
     * 删除轮播图管理信息
     * 
     * @param id 轮播图管理主键
     * @return 结果
     */
    @Override
    public int deleteAiBannersById(Long id)
    {
        return aiBannersMapper.deleteAiBannersById(id);
    }
}
