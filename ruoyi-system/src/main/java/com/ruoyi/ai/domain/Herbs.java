package com.ruoyi.ai.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 中医药材主对象 herbs
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class Herbs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 药材中文名（如：人参） */
    @Excel(name = "药材中文名", readConverterExp = "如=：人参")
    private String name;

    /** 别名（多个用逗号分隔） */
    @Excel(name = "别名", readConverterExp = "多=个用逗号分隔")
    private String alias;

    /** 分类（如：补气药、清热药） */
    @Excel(name = "分类", readConverterExp = "如=：补气药、清热药")
    private String category;

    /** 主要功效描述（如：大补元气，复脉固脱） */
    @Excel(name = "主要功效描述", readConverterExp = "如=：大补元气，复脉固脱")
    private String effect;

    /** 来源（产地、植物部位等） */
    @Excel(name = "来源", readConverterExp = "产=地、植物部位等")
    private String origin;

    /** 注意事项/禁忌 */
    @Excel(name = "注意事项/禁忌")
    private String warning;

    /** 药材图片URL */
    @Excel(name = "药材图片URL")
    private String imageUrl;

    /** 简介或科普内容 */
    @Excel(name = "简介或科普内容")
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAlias(String alias) 
    {
        this.alias = alias;
    }

    public String getAlias() 
    {
        return alias;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setEffect(String effect) 
    {
        this.effect = effect;
    }

    public String getEffect() 
    {
        return effect;
    }

    public void setOrigin(String origin) 
    {
        this.origin = origin;
    }

    public String getOrigin() 
    {
        return origin;
    }

    public void setWarning(String warning) 
    {
        this.warning = warning;
    }

    public String getWarning() 
    {
        return warning;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("alias", getAlias())
            .append("category", getCategory())
            .append("effect", getEffect())
            .append("origin", getOrigin())
            .append("warning", getWarning())
            .append("imageUrl", getImageUrl())
            .append("description", getDescription())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
