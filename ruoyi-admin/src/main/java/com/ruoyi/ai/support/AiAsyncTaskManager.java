package com.ruoyi.ai.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AiAsyncTaskManager {

    private static final Logger log = LoggerFactory.getLogger(AiAsyncTaskManager.class);
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    private final Map<String, Map<String, Object>> taskStore = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public String submit(TaskBody taskBody) {
        String taskId = UUID.randomUUID().toString();
        taskStore.put(taskId, processingPayload(taskId));
        try {
            executor.execute(() -> complete(taskId, taskBody));
        } catch (Exception e) {
            log.error("AI async task submit failed, taskId={}", taskId, e);
            taskStore.put(taskId, failedPayload(taskId, safeMessage(e)));
        }
        return taskId;
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }

    public Map<String, Object> get(String taskId) {
        return taskStore.get(taskId);
    }

    public Map<String, Object> processingPayload(String taskId) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", taskId);
        data.put("status", STATUS_PROCESSING);
        return data;
    }

    private void complete(String taskId, TaskBody taskBody) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", taskId);
        try {
            data.put("status", STATUS_SUCCESS);
            data.put("result", taskBody.run());
        } catch (Exception e) {
            log.error("AI async task failed, taskId={}", taskId, e);
            data.put("status", STATUS_FAILED);
            data.put("message", safeMessage(e));
        }
        taskStore.put(taskId, data);
    }

    private Map<String, Object> failedPayload(String taskId, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskId", taskId);
        data.put("status", STATUS_FAILED);
        data.put("message", message);
        return data;
    }

    private String safeMessage(Exception e) {
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }

    @FunctionalInterface
    public interface TaskBody {
        Object run();
    }
}
