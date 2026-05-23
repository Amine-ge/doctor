package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.vo.TrendAdviceVO;

import java.util.*;
import java.util.stream.Collectors;

public class TrendAdviceRuleEngine {

    public static TrendAdviceVO build(String range, List<Integer> total) {
        int days = rangeToDays(range);

        List<Integer> vals = (total == null) ? Collections.emptyList() :
                total.stream()
                        .filter(Objects::nonNull)
                        .filter(v -> v > 0) // 关键：0 视为缺失
                        .collect(Collectors.toList());

        if (vals.size() < 2) {
            return new TrendAdviceVO(
                    "normal",
                    "数据不足",
                    "当前记录较少，建议持续采集以形成稳定趋势。",
                    Arrays.asList("尽量固定采集时间与光线", "保持同一拍摄角度与距离", "持续记录以便形成趋势"),
                    "范围：" + days + "天",
                    vals.isEmpty() ? 0 : vals.get(vals.size() - 1),
                    0,
                    0.0
            );
        }

        int first = vals.get(0);
        int last = vals.get(vals.size() - 1);
        int diff = last - first;
        double volatility = avgAbsDiff(vals);

        String tip = "范围：" + days + "天；建议结合连续记录综合观察。";

        // 上升
        if (diff >= 6) {
            return new TrendAdviceVO(
                    "good",
                    "向好",
                    "评分呈上升趋势，近期状态在改善，建议继续保持规律节奏。",
                    Arrays.asList("保持当前作息节奏", "持续适度运动与拉伸", "固定条件采集以巩固趋势"),
                    tip,
                    last,
                    diff,
                    volatility
            );
        }

        // 下降
        if (diff <= -6) {
            return new TrendAdviceVO(
                    "warn",
                    "下降",
                    "评分有下降趋势，建议优先调整作息与日常节奏，并注意采集一致性。",
                    Arrays.asList("减少熬夜与高强度用眼", "饮食尽量清淡", "固定采集时间与环境"),
                    tip,
                    last,
                    diff,
                    volatility
            );
        }

        // 平稳：结合波动
        if (volatility >= 6.0) {
            return new TrendAdviceVO(
                    "normal",
                    "平稳",
                    "整体平均水平接近，但波动偏大，建议优先保证作息与采集条件稳定。",
                    Arrays.asList("固定拍摄光线与距离", "尽量在同一时间段采集", "保持规律作息减少波动"),
                    tip,
                    last,
                    diff,
                    volatility
            );
        }

        return new TrendAdviceVO(
                "normal",
                "平稳",
                "最近一段时间评分整体较稳定，可继续保持当前节奏。",
                Arrays.asList("保持规律作息", "日常饮水分次补充", "每周安排适度运动"),
                tip,
                last,
                diff,
                volatility
        );
    }

    private static int rangeToDays(String range) {
        switch (range) {
            case "1m": return 30;
            case "6m": return 180;
            case "7d":
            default: return 7;
        }
    }

    /** 平均相邻差值：衡量波动 */
    private static double avgAbsDiff(List<Integer> vals) {
        if (vals.size() < 2) return 0.0;
        double sum = 0;
        for (int i = 1; i < vals.size(); i++) {
            sum += Math.abs(vals.get(i) - vals.get(i - 1));
        }
        return sum / (vals.size() - 1);
    }
}
