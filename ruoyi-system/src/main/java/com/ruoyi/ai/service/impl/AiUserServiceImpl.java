package com.ruoyi.ai.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.dto.UserDTO;
import com.ruoyi.ai.domain.vo.LatestHealthDataVo;
import com.ruoyi.ai.domain.vo.UserVo;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.mapper.AiUserMapper;
import com.ruoyi.ai.service.IAiUserService;
import com.ruoyi.properties.WeChatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
/**
 * 用户信息Service业务层处理
 */
@Service
public class AiUserServiceImpl extends ServiceImpl<AiUserMapper, AiUser> implements IAiUserService {

    @Autowired
    private AiUserMapper aiUserMapper;
    @Autowired
    private AiTongueRecordMapper aiTongueRecordMapper;
    @Autowired
    private AiFaceRecordMapper aiFaceRecordMapper;
    @Autowired
    private AiNailRecordMapper aiNailRecordMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public AiUser selectAiUserById(Long id) {
        return aiUserMapper.selectAiUserById(id);
    }

    @Override
    public List<AiUser> selectAiUserList(AiUser aiUser) {
        return aiUserMapper.selectAiUserList(aiUser);
    }

    @Override
    public int insertAiUser(AiUser aiUser) {
        return aiUserMapper.insertAiUser(aiUser);
    }

    @Override
    public int updateAiUser(AiUser aiUser) {
        return aiUserMapper.updateAiUser(aiUser);
    }

    @Override
    public int deleteAiUserByIds(Long[] ids) {
        return aiUserMapper.deleteAiUserByIds(ids);
    }

    @Override
    public int deleteAiUserById(Long id) {
        return aiUserMapper.deleteAiUserById(id);
    }


    @Override
    public AiUser wxLogin(UserDTO userDto) {
        // 1. 用 code 换 openid
        String openid = getOpenid(userDto);

        // 2. 用我们自己写的 Mapper 方法查
        AiUser user = aiUserMapper.selectAiUserByOpenid(openid);

        if (user == null) {
            user = AiUser.builder()
                    .openid(openid)
                    .nickname(UUID.randomUUID().toString())
                    .createdAt(DateTime.now())
                    .updatedAt(DateTime.now())
                    .build();

            if (userDto.getNickName() != null) {
                user.setNickname(userDto.getNickName());
            }
            if (userDto.getAvatarUrl() != null) {
                user.setAvatarUrl(userDto.getAvatarUrl());
            }

            aiUserMapper.insertAiUser(user);
        }

        return user;
    }

    @Override
    public UserVo getUserInfo(Long userId) {
        AiUser user = aiUserMapper.selectAiUserById(userId);
        if (user == null) {
            return null;
        }

        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setNickname(user.getNickname());
        userVo.setAvatarUrl(user.getAvatarUrl());
        userVo.setPhone(user.getPhone());
        userVo.setAge(user.getAge());
       userVo.setGender(user.getGender());
        userVo.setStatus(user.getStatus());
        userVo.setUpdatedAt(user.getUpdatedAt());
        userVo.setCreatedAt(user.getCreatedAt());
        return userVo;
    }

    @Override
    public LatestHealthDataVo getLatestHealthData(Long userId) {
        LatestHealthDataVo vo = new LatestHealthDataVo();

        AiTongueRecord tongue = aiTongueRecordMapper.selectLatestByUserId(userId);
        AiFaceRecord face = aiFaceRecordMapper.selectLatestByUserId(userId);
        AiNailRecord nail = aiNailRecordMapper.selectLatestByUserId(userId);

        vo.setTongue(buildTongueItem(tongue));
        vo.setFace(buildFaceItem(face));
        vo.setNail(buildNailItem(nail));
        return vo;
    }

    @Override
    public void updateUser(AiUser user) {
//        user.setUpdatedAt(DateTime.now());
        aiUserMapper.updateAiUser( user);
    }


    private String getOpenid(UserDTO userLoginDTO) {
        String url = org.springframework.web.util.UriComponentsBuilder
                .fromHttpUrl(WX_LOGIN)
                .queryParam("appid", weChatProperties.getAppid())
                .queryParam("secret", weChatProperties.getSecret())
                .queryParam("js_code", userLoginDTO.getCode())
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();

        String json = restTemplate.getForObject(url, String.class);

        System.out.println("微信 jscode2session 返回：" + json);

        JSONObject jsonObject = JSON.parseObject(json);

        if (jsonObject == null) {
            throw new RuntimeException("微信登录失败：微信接口返回为空");
        }

        if (jsonObject.containsKey("errcode")) {
            throw new RuntimeException("微信登录失败：" + jsonObject.toJSONString());
        }

        String openid = jsonObject.getString("openid");

        if (openid == null || openid.trim().isEmpty()) {
            throw new RuntimeException("微信登录失败：openid为空，微信返回：" + json);
        }

        return openid;
    }
    private LatestHealthDataVo.Item buildTongueItem(AiTongueRecord r) {
        LatestHealthDataVo.Item item = new LatestHealthDataVo.Item();
        if (r == null) {
            item.setStatusText("未检测");
            return item;
        }

        item.setId(r.getId());
        item.setCreatedAt(r.getCreatedAt());

        BigDecimal score = r.getTqQualityScore(); // BigDecimal

        if (score == null) {
            item.setStatusText("未检测");
        } else if (score.compareTo(new BigDecimal("0.3")) < 0) {
            item.setStatusText("图像较差");
        } else if (score.compareTo(new BigDecimal("0.6")) < 0) {
            item.setStatusText("正常");
        } else {
            item.setStatusText("良好");
        }

        return item;
    }


    private LatestHealthDataVo.Item buildFaceItem(AiFaceRecord r) {
        LatestHealthDataVo.Item item = new LatestHealthDataVo.Item();
        if (r == null) {
            item.setStatusText("未检测");
            return item;
        }

        item.setId(r.getId());
        item.setCreatedAt(r.getCreatedAt());

        BigDecimal score = r.getFqQualityScore(); // 改成你的字段

        if (score == null) {
            item.setStatusText("未检测");
        } else if (score.compareTo(new BigDecimal("0.3")) < 0) {
            item.setStatusText("图像较差");
        } else if (score.compareTo(new BigDecimal("0.6")) < 0) {
            item.setStatusText("正常");
        } else {
            item.setStatusText("良好");
        }

        return item;
    }

    private LatestHealthDataVo.Item buildNailItem(AiNailRecord r) {
        LatestHealthDataVo.Item item = new LatestHealthDataVo.Item();
        if (r == null) {
            item.setStatusText("未检测");
            return item;
        }

        item.setId(r.getId());
        item.setCreatedAt(r.getCreatedAt());

        BigDecimal score = r.getNqQualityScore(); // 改成你的字段 nailQualityScore

        if (score == null) {
            item.setStatusText("未检测");
        } else if (score.compareTo(new BigDecimal("0.3")) < 0) {
            item.setStatusText("图像较差");
        } else if (score.compareTo(new BigDecimal("0.6")) < 0) {
            item.setStatusText("正常");
        } else {
            item.setStatusText("良好");
        }

        return item;
    }

}
