package com.ruoyi.ai.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserVo {

    private Long id;

    private String nickname;
    private Long age;

    private String avatarUrl;
    //手机号
    private String phone;
    private Integer gender;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    //token
    private String token;
}
