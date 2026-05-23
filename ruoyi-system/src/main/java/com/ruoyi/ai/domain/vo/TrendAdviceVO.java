package com.ruoyi.ai.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendAdviceVO {
    /** good / normal / warn */
    private String level;

    /** 向好 / 平稳 / 下降 / 数据不足 */
    private String levelText;

    /** 概述（1-2句） */
    private String summary;

    /** 建议列表（3-5条） */
    private List<String> suggestions;

    /** 备注提示 */
    private String tip;

    /** 可选：用于解释/调试 */
    private Integer latestScore;
    private Integer diff;
    private Double volatility;
}
