package com.ruoyi.ai.domain.dto;

import lombok.Data;

@Data
public class AskDTO {
    private String question;
    private Long tongueId;
    private Long faceId;
    private Long nailId;
}
