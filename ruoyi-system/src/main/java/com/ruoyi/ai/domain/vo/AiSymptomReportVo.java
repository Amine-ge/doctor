package com.ruoyi.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AiSymptomReportVo {
    private Long id;
    private String mainSymptom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    // 关联情况
    private Long tongueRecordId;
    private Long faceRecordId;
    private Long nailRecordId;

    private String tongueImageUrl;
    private String faceImageUrl;
    private String nailImageUrl;
    private String aiDiagnosis;


    // 方便前端直接用的布尔字段
    private boolean hasTongue;
    private boolean hasFace;
    private boolean hasNail;
    private boolean hasAiDiagnosis;

}
