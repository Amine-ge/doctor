package com.ruoyi.ai.service;

public interface DoubaoVisionService {

    String analyzeImage(String systemPrompt, String userPrompt, String imageUrl);

    String analyzeImage(String model, String systemPrompt, String userPrompt, String imageUrl);

    String analyzeTongue(String imageUrl);

    String analyzeFace(String imageUrl);

    String analyzeNail(String imageUrl);

    String analyzeMedicalReport(String imageUrl);

    String analyzeMedicine(String imageUrl);
}
