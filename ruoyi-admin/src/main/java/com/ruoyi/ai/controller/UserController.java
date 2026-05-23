package com.ruoyi.ai.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;

import com.ruoyi.ai.Enum.VisionType;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.dto.UserDTO;
import com.ruoyi.ai.domain.vo.AiSymptomReportVo;
import com.ruoyi.ai.domain.vo.HealthTrendRangeVO;
import com.ruoyi.ai.domain.vo.LatestHealthDataVo;
import com.ruoyi.ai.domain.vo.UserVo;
import com.ruoyi.ai.service.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.consts.JwtClaimsConstant;
import com.ruoyi.properties.WecomProps;
import com.ruoyi.utils.MyJwtUtil;
import com.ruoyi.utils.UserHold;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
     private  final IAiUserService userService;
    private final WecomProps wecomProps;
    private final StringRedisTemplate stringRedisTemplate;
    private final IAiTongueRecordService tongueRecordService;
    private final IAiFaceRecordService faceRecordService;
    private final IAiNailRecordService nailRecordService;
    private final IAiSymptomReportService aiSymptomReportService;
    private final HealthTrendService trendService;

    @PostMapping("/wxLogin")
    public R login(@RequestBody UserDTO userDto) {
        // 调用 Service 中的 wxLogin 方法完成登录和注册
        AiUser u =  userService.wxLogin(userDto);
        // 生成Token 返回用户vo给前端
        Map<String, Object> claims = new HashMap<>();
        String userJson = JSONUtil.toJsonStr(u);
        claims.put(JwtClaimsConstant.USER, userJson);
        long ttlMillis = wecomProps.getTtl();
        String token = MyJwtUtil.createJWT(wecomProps.getSecret(), ttlMillis, claims);

        String redisKey = JwtClaimsConstant.REDIS_TOKEN_PREFIX + u.getId();
        stringRedisTemplate.opsForValue().set(redisKey, token,  wecomProps.getTtl(), TimeUnit.MINUTES);
        UserVo userVo = BeanUtil.copyProperties(u, UserVo.class);
        // 设置token
        userVo.setToken(token);
        return R.ok(userVo);
    }


    //  修改用户信息controller
    @PostMapping("/update")
    public R update(@RequestBody AiUser user) {
        Long id = UserHold.get().getId();
        if (id == null) return R.fail("用户不存在");
        if (!id.equals(user.getId())) return R.fail("用户不存在");
        userService.updateUser(user);
        return R.ok();
    }


   //  获取用户信息controller
    @GetMapping("/getInfo")
    public R getInfo() {
        Long userId = UserHold.get().getId();
        UserVo user =  userService.getUserInfo(userId);
        // 从redis当中取出token
        String token = stringRedisTemplate.opsForValue().get(JwtClaimsConstant.REDIS_TOKEN_PREFIX + userId);
        user.setToken(token);
        return R.ok(user);
    }
   @GetMapping("/records")
   public AjaxResult list(@RequestParam("type") String type) {
       Long userId = UserHold.get().getId();

       VisionType visionType = VisionType.fromCode(type);

       switch (visionType) {
           case TONGUE:
               return AjaxResult.success(tongueRecordService.listByUser(userId));
           case FACE:
               return AjaxResult.success(faceRecordService.listByUser(userId));
           case NAIL:
               return AjaxResult.success(nailRecordService.listByUser(userId));
           default:
               return AjaxResult.error("不支持的记录类型");
       }
   }

    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam("type") String type,
                             @RequestParam("id") Long id) {
        VisionType visionType = VisionType.fromCode(type);

        switch (visionType) {
            case TONGUE:
                return AjaxResult.success(tongueRecordService.getDetail(id));
            case FACE:
                return AjaxResult.success(faceRecordService.getDetail(id));
            case NAIL:
                return AjaxResult.success(nailRecordService.getDetail(id));
            default:
                return AjaxResult.error("不支持的记录类型");
        }
    }


    @GetMapping("/symptom/list")
    public R<List<AiSymptomReportVo>> list() {
        Long userId = UserHold.get().getId();
        return R.ok(aiSymptomReportService.listByUser(userId));
    }

    @GetMapping("/symptom/detail/{id}")
    public R<AiSymptomReportVo> detail(@PathVariable Long id) {
        Long userId = UserHold.get().getId();
        AiSymptomReportVo vo = aiSymptomReportService.getDetail(userId, id);
        if (vo == null) {
            return R.fail("记录不存在");
        }
        return R.ok(vo);
    }

    @GetMapping("/latest")
    public R<LatestHealthDataVo> latest() {
        Long userId = UserHold.get().getId();
        LatestHealthDataVo vo = userService.getLatestHealthData(userId);
        return R.ok(vo);
    }

    @GetMapping("/trend")
    public AjaxResult getTrend() {
        Long userId = UserHold.get().getId();

        Map<String, HealthTrendRangeVO> map = new HashMap<>();
        map.put("7d", trendService.getHealthTrend("7d", userId));
        map.put("1m", trendService.getHealthTrend("1m", userId));
        map.put("6m", trendService.getHealthTrend("6m", userId));

        return AjaxResult.success(map);
    }

}
