package com.ruoyi.ai.service.impl;

import com.ruoyi.ai.service.DoubaoVisionService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.consts.AiPrompts;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemImage;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.model.responses.content.OutputContentItem;
import com.volcengine.ark.runtime.model.responses.content.OutputContentItemText;
import com.volcengine.ark.runtime.model.responses.item.BaseItem;
import com.volcengine.ark.runtime.model.responses.item.ItemOutputMessage;
import com.volcengine.ark.runtime.model.responses.item.ItemEasyMessage;
import com.volcengine.ark.runtime.model.responses.item.MessageContent;
import com.volcengine.ark.runtime.model.responses.request.CreateResponsesRequest;
import com.volcengine.ark.runtime.model.responses.request.ResponsesInput;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoubaoVisionServiceImpl implements DoubaoVisionService {

    private static final String DEFAULT_BASE_URL = "https://ark.cn-beijing.volces.com/api/v3";
    private static final String DEFAULT_MODEL = "doubao-seed-2-0-lite-260215";

    @Override
    public String analyzeImage(String systemPrompt, String userPrompt, String imageUrl) {
        return analyzeImage(null, systemPrompt, userPrompt, imageUrl);
    }

    @Override
    public String analyzeImage(String model, String systemPrompt, String userPrompt, String imageUrl) {
        checkImageUrl(imageUrl);

        ArkService arkService = buildArkService();
        try {
            MessageContent content = MessageContent.builder()
                    .addListItem(InputContentItemText.builder()
                            .text(userPrompt == null ? "" : userPrompt)
                            .build())
                    .addListItem(InputContentItemImage.builder()
                            .imageUrl(imageUrl)
                            .detail("high")
                            .build())
                    .build();

            ResponsesInput input = ResponsesInput.builder()
                    .addListItem(ItemEasyMessage.builder()
                            .role("user")
                            .content(content)
                            .build())
                    .build();

            CreateResponsesRequest request = CreateResponsesRequest.builder()
                    .model(resolveModel(model))
                    .instructions(systemPrompt)
                    .input(input)
                    .build();

            ResponseObject response = arkService.createResponse(request);
            return extractOutputText(response);
        } finally {
            arkService.shutdownExecutor();
        }
    }

    @Override
    public String analyzeTongue(String imageUrl) {
        return analyzeImage(
                AiPrompts.SYS_TONGUE_VISION,
                "分析这张舌头图片URL，仅按系统提示输出严格JSON：\n" + imageUrl,
                imageUrl
        );
    }

    @Override
    public String analyzeFace(String imageUrl) {
        return analyzeImage(
                AiPrompts.SYS_FACE_VISION,
                "分析这张面部图片URL，仅按系统提示输出严格JSON：\n" + imageUrl,
                imageUrl
        );
    }

    @Override
    public String analyzeNail(String imageUrl) {
        return analyzeImage(
                AiPrompts.SYS_NAIL_VISION,
                "分析这张指甲图片URL，仅按系统提示输出严格JSON：\n" + imageUrl,
                imageUrl
        );
    }

    @Override
    public String analyzeMedicalReport(String imageUrl) {
        return analyzeImage(
                AiPrompts.SYS_MEDICAL_REPORT_VISION,
                "请识别这张检查报告图片，并严格按系统提示输出 JSON：\n" + imageUrl,
                imageUrl
        );
    }

    @Override
    public String analyzeMedicine(String imageUrl) {
        return analyzeImage(
                AiPrompts.SYS_MEDICINE_VISION,
                "请识别这张药盒、说明书或处方图片，并严格按系统提示输出 JSON：\n" + imageUrl,
                imageUrl
        );
    }

    private ArkService buildArkService() {
        String apiKey = readConfig("ARK_API_KEY", null);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("ARK_API_KEY 未配置，请使用环境变量或 JVM 参数 -DARK_API_KEY=xxx");
        }

        return ArkService.builder()
                .apiKey(apiKey.trim())
                .baseUrl(readConfig("ARK_BASE_URL", DEFAULT_BASE_URL))
                .build();
    }

    private String resolveModel(String model) {
        if (model != null && !model.isBlank()) {
            return model.trim();
        }
        return readConfig("DOUBAO_VISION_MODEL", DEFAULT_MODEL);
    }

    private String readConfig(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }

    private void checkImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.startsWith("http")) {
            throw new ServiceException("图片URL必须为公网可访问的 http/https 链接");
        }
    }

    private String extractOutputText(ResponseObject response) {
        if (response == null || response.getOutput() == null || response.getOutput().isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        List<BaseItem> output = response.getOutput();
        for (BaseItem item : output) {
            if (item instanceof ItemOutputMessage) {
                ItemOutputMessage message = (ItemOutputMessage) item;
                appendContentText(builder, message.getContent());
            }
        }

        String text = builder.toString().trim();
        return text.isEmpty() ? response.toString() : text;
    }

    private void appendContentText(StringBuilder builder, List<OutputContentItem> content) {
        if (content == null || content.isEmpty()) {
            return;
        }
        for (OutputContentItem item : content) {
            if (item instanceof OutputContentItemText) {
                OutputContentItemText text = (OutputContentItemText) item;
                if (text.getText() != null) {
                    builder.append(text.getText());
                }
            }
        }
    }
}
