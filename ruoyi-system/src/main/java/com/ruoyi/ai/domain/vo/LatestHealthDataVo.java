package com.ruoyi.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

// 最近健康数据 VO
@Data
public class LatestHealthDataVo {

    private Item tongue; // 舌诊
    private Item face;   // 面诊
    private Item nail;   // 指诊/指甲

    @Data
    public static class Item {
        private Long id;

        // 最近一次记录时间
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date createdAt;

        // 状态文案：良好 / 正常 / 未检测 等
        private String statusText;
    }
}
