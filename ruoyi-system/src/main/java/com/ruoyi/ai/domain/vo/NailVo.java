package com.ruoyi.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class NailVo {
    private Long id;
    private String imageUrl;
    private String nailColor;
    /** nail_texture：表面质地（光滑/轻微纹理/明显纹理/凹陷/无法判断） */
    private String nailTexture;
    /** nail_shape：形状（正常/匙状/凹陷/凸起/无法判断） */
    private String nailShape;
    /** lunula_visibility：月牙可见性（无/少量/正常/无法判断） */
    private String lunulaVisibility;
    /** breakability：易碎程度（正常/偏脆/非常脆/无法判断） */
    private String breakability;
    /** notes：一行说明（可选） */
    private String notes;
    private String aiDiagnosis;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    // 状态文案：良好 / 正常 / 未检测 等
    private String statusText;
}
