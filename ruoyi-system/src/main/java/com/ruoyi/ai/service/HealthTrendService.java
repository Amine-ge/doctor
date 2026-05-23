package com.ruoyi.ai.service;


import com.ruoyi.ai.domain.vo.HealthTrendRangeVO;

public interface HealthTrendService {
    HealthTrendRangeVO getHealthTrend(String s, Long userId);
}
