package com.ruoyi.ai.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthTrendRangeVO {
    private List<String> times;
    private List<Integer> total;
    private List<Integer> face;
    private List<Integer> tongue;
    private List<Integer> nail;

    /** 新增：趋势建议（规则+LLM润色） */
    private TrendAdviceVO advice;
}
