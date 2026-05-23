package com.ruoyi.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class FaceVo {
    private Long id;
    private String imageUrl;
    /** skin_tone：肤色/类型 */
    private String skinTone;
    /** oiliness_level：出油程度 */
    private String oilinessLevel;
    /** dark_circles_level：黑眼圈程度 */
    private String darkCirclesLevel;
    /** acne_level：痘痘程度（ */
    private String acneLevel;
    /** lip_color：唇色*/
    private String lipColor;
    /** notes：一行说明（可空） */
    private String notes;
    private String aiDiagnosis;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    // 状态文案：良好 / 正常 / 未检测 等
    private String statusText;
}
