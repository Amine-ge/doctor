package com.ruoyi.ai.domain.dto;

import javax.validation.constraints.NotBlank;

public class TongueAnalyzeReq {
    @NotBlank
    private String url;        // 若依 /common/upload 返回的 url
    private String model = "qwen-vl-max";
    private AnalyzeExtras extras = new AnalyzeExtras();
    // getter/setter
}
