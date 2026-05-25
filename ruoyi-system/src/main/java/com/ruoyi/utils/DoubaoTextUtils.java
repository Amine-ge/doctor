package com.ruoyi.utils;

import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.model.responses.content.OutputContentItem;
import com.volcengine.ark.runtime.model.responses.content.OutputContentItemText;
import com.volcengine.ark.runtime.model.responses.item.BaseItem;
import com.volcengine.ark.runtime.model.responses.item.ItemEasyMessage;
import com.volcengine.ark.runtime.model.responses.item.ItemOutputMessage;
import com.volcengine.ark.runtime.model.responses.item.MessageContent;
import com.volcengine.ark.runtime.model.responses.request.CreateResponsesRequest;
import com.volcengine.ark.runtime.model.responses.request.ResponsesInput;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.List;

public class DoubaoTextUtils {

    private static final String DEFAULT_BASE_URL = "https://ark.cn-beijing.volces.com/api/v3";
    private static final String DEFAULT_MODEL = "doubao-seed-2-0-lite-260215";

    private DoubaoTextUtils() {
    }

    public static String chat(String systemPrompt, String question) {
        ArkService arkService = buildArkService();
        try {
            MessageContent content = MessageContent.builder()
                    .addListItem(InputContentItemText.builder()
                            .text(question == null ? "" : question)
                            .build())
                    .build();

            ResponsesInput input = ResponsesInput.builder()
                    .addListItem(ItemEasyMessage.builder()
                            .role("user")
                            .content(content)
                            .build())
                    .build();

            CreateResponsesRequest request = CreateResponsesRequest.builder()
                    .model(resolveModel())
                    .instructions(systemPrompt)
                    .input(input)
                    .build();

            ResponseObject response = arkService.createResponse(request);
            return extractOutputText(response);
        } finally {
            arkService.shutdownExecutor();
        }
    }

    private static ArkService buildArkService() {
        String apiKey = readConfig("ARK_API_KEY", null);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("ARK_API_KEY is not configured");
        }

        return ArkService.builder()
                .apiKey(apiKey.trim())
                .baseUrl(readConfig("ARK_BASE_URL", DEFAULT_BASE_URL))
                .build();
    }

    private static String resolveModel() {
        return readConfig("DOUBAO_LLM_MODEL", readConfig("DOUBAO_VISION_MODEL", DEFAULT_MODEL));
    }

    private static String readConfig(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    private static String extractOutputText(ResponseObject response) {
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

    private static void appendContentText(StringBuilder builder, List<OutputContentItem> content) {
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
