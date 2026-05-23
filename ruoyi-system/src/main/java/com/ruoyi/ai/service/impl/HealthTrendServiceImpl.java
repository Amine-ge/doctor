package com.ruoyi.ai.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.domain.HealthDailyScoreDO;
import com.ruoyi.ai.domain.vo.HealthTrendRangeVO;
import com.ruoyi.ai.domain.vo.TrendAdviceVO;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.mapper.AiSymptomReportMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.service.HealthTrendService;
import com.ruoyi.ai.service.LlmService;
import com.ruoyi.ai.service.TrendAdviceRuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.ruoyi.ai.service.TrendAdvicePolisher.extractFirstJsonObject;
@Slf4j
@Service
public class HealthTrendServiceImpl implements HealthTrendService {

    @Autowired
    private AiFaceRecordMapper faceMapper;
    @Autowired
    private AiTongueRecordMapper tongueMapper;
    @Autowired
    private AiNailRecordMapper nailMapper;
    @Autowired
    private AiSymptomReportMapper symptomMapper;
    @Autowired
    private LlmService llmService;

    @Override
    public HealthTrendRangeVO getHealthTrend(String range, Long userId) {

        LocalDate today = LocalDate.now();
        LocalDate startDate;

        switch (range) {
            case "1m":
                startDate = today.minusMonths(1);
                break;
            case "6m":
                startDate = today.minusMonths(6);
                break;
            case "7d":
            default:
                startDate = today.minusDays(6); // 含今天共 7 天
                break;
        }

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = today.atTime(LocalTime.MAX);

        // 1) 各表查询：按天聚合后的结果
        List<HealthDailyScoreDO> faceList = safeList(faceMapper.selectDailyFaceScore(userId, start, end));
        List<HealthDailyScoreDO> tongueList = safeList(tongueMapper.selectDailyTongueScore(userId, start, end));
        List<HealthDailyScoreDO> nailList = safeList(nailMapper.selectDailyNailScore(userId, start, end));
        List<HealthDailyScoreDO> totalList = safeList(symptomMapper.selectDailyTotalScore(userId, start, end));

        // 2) 转 Map：day -> score（null 防护）
        Map<LocalDate, BigDecimal> faceMap   = toDateScoreMap(faceList,   HealthDailyScoreDO::getFaceScore);
        Map<LocalDate, BigDecimal> tongueMap = toDateScoreMap(tongueList, HealthDailyScoreDO::getTongueScore);
        Map<LocalDate, BigDecimal> nailMap   = toDateScoreMap(nailList,   HealthDailyScoreDO::getNailScore);
        Map<LocalDate, BigDecimal> totalMap  = toDateScoreMap(totalList,  HealthDailyScoreDO::getTotalScore);

        // 3) 组装 VO（补齐日期）
        List<String> times = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        List<Integer> face = new ArrayList<>();
        List<Integer> tongue = new ArrayList<>();
        List<Integer> nail = new ArrayList<>();

        DateTimeFormatter fmt = "6m".equals(range)
                ? DateTimeFormatter.ofPattern("MM")
                : DateTimeFormatter.ofPattern("MM/dd");

        LocalDate cursor = startDate;
        while (!cursor.isAfter(today)) {
            times.add(cursor.format(fmt));

            BigDecimal f = faceMap.getOrDefault(cursor, BigDecimal.ZERO);
            BigDecimal t = tongueMap.getOrDefault(cursor, BigDecimal.ZERO);
            BigDecimal n = nailMap.getOrDefault(cursor, BigDecimal.ZERO);
            BigDecimal tot = totalMap.get(cursor); // 注意：这里不要 getOrDefault，保留 null 用来触发兜底平均

            face.add(f.intValue());
            tongue.add(t.intValue());
            nail.add(n.intValue());

            // 没有 symptom 总分（null 或 0）时，用三者非零平均兜底
            if (tot == null || tot.compareTo(BigDecimal.ZERO) == 0) {
                int cnt = 0;
                BigDecimal sum = BigDecimal.ZERO;

                if (f.compareTo(BigDecimal.ZERO) > 0) { sum = sum.add(f); cnt++; }
                if (t.compareTo(BigDecimal.ZERO) > 0) { sum = sum.add(t); cnt++; }
                if (n.compareTo(BigDecimal.ZERO) > 0) { sum = sum.add(n); cnt++; }

                tot = (cnt == 0)
                        ? BigDecimal.ZERO
                        : sum.divide(BigDecimal.valueOf(cnt), 2, RoundingMode.HALF_UP);
            }

            total.add(tot.intValue());
            cursor = cursor.plusDays(1);
        }

        // 4) 规则建议
        TrendAdviceVO advice = TrendAdviceRuleEngine.build(range, total);

        // 5) 半AI润色（失败自动回退规则版）
        try {
            // 数据不足就不润色，避免无数据也生成“很会说”的建议
            if (!"数据不足".equals(advice.getLevelText())) {
                JSONObject in = JSONUtil.createObj()
                        .set("range", range)
                        .set("level", advice.getLevel())
                        .set("levelText", advice.getLevelText())
                        .set("latestScore", advice.getLatestScore())
                        .set("diff", advice.getDiff())
                        .set("volatility", advice.getVolatility())
                        .set("summary", advice.getSummary())
                        .set("suggestions", advice.getSuggestions())
                        .set("tip", advice.getTip());

                String raw = llmService.polishTrendAdvice(in, userId);

                String json = extractFirstJsonObject(raw);
                log.debug("polishTrendAdvice raw={}, json={}", raw, json);
                JSONObject o = JSONUtil.parseObj(json);
                advice.setSummary(o.getStr("summary", advice.getSummary()));

                if (o.containsKey("suggestions") && o.get("suggestions") != null) {
                    advice.setSuggestions(o.getJSONArray("suggestions").toList(String.class));
                }
                advice.setTip(o.getStr("tip", advice.getTip()));
            }
        } catch (Exception e) {
             log.debug("polishTrendAdvice failed, fallback to rule advice. msg={}", e.getMessage());
        }

        return new HealthTrendRangeVO(times, total, face, tongue, nail, advice);
    }

    private static <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    private Map<LocalDate, BigDecimal> toDateScoreMap(
            List<HealthDailyScoreDO> list,
            Function<HealthDailyScoreDO, BigDecimal> scoreGetter) {

        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }

        return list.stream()
                .filter(d -> d != null && d.getDay() != null)
                .collect(Collectors.toMap(
                        HealthDailyScoreDO::getDay,
                        d -> {
                            BigDecimal v = scoreGetter.apply(d);
                            return v == null ? BigDecimal.ZERO : v;
                        },
                        // 同一天多条：保留第一条（你也可以改成 newV 覆盖或取 max）
                        (oldV, newV) -> oldV
                ));
    }
}
