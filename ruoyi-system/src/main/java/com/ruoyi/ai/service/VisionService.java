package com.ruoyi.ai.service;

import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;

public interface VisionService {
    AiTongueRecord tongue(String url, Long id);

    AiFaceRecord face(String url, Long id);

    AiNailRecord nail(String url, Long id);
}
