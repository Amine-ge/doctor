package com.ruoyi.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.ai.domain.AiUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-12
 */
@Mapper
public interface AiUserMapper extends BaseMapper<AiUser>
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
     * 删除用户信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteAiUserById(Long id);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiUserByIds(Long[] ids);

    @Select("select * from ai_user where openid = #{openid}")
    AiUser selectAiUserByOpenid(@Param("openid") String openid);

}
