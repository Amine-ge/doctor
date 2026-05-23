package com.ruoyi.ai.domain.dto;
import lombok.Data;

@Data
public class UserDTO {
    /**
     * 微信登录凭证
     */
    public String code;
    private String nickName;
    private String avatarUrl;
}
