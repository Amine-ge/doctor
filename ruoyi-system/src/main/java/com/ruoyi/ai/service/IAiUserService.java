package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.dto.UserDTO;
import com.ruoyi.ai.domain.vo.LatestHealthDataVo;
import com.ruoyi.ai.domain.vo.UserVo;

import java.util.List;

/**
 * 用户信息Service接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
public interface IAiUserService 
{
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    public AiUser selectAiUserById(Long id);

    /**
     * 查询用户信息列表
     * 
     * @param aiUser 用户信息
     * @return 用户信息集合
     */
    public List<AiUser> selectAiUserList(AiUser aiUser);

    /**
     * 新增用户信息
     * 
     * @param aiUser 用户信息
     * @return 结果
     */
    public int insertAiUser(AiUser aiUser);

    /**
     * 修改用户信息
     * 
     * @param aiUser 用户信息
     * @return 结果
     */
    public int updateAiUser(AiUser aiUser);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteAiUserByIds(Long[] ids);

    /**
     * 删除用户信息信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteAiUserById(Long id);

    AiUser wxLogin(UserDTO userDto);

    UserVo getUserInfo(Long userId);

    LatestHealthDataVo getLatestHealthData(Long userId);

    void updateUser(AiUser user);
}
