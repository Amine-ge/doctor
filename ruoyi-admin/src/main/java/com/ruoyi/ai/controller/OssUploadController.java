package com.ruoyi.ai.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.utils.AliOssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/oss")
@Slf4j
@RequiredArgsConstructor
public class OssUploadController {

   final private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file){
        log.info("文件上传 ：{}",file);
        //

        try {

            // 防止文件重命到时文件覆盖，需要使用UUID，将文件重命名
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件的后缀名
            String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名称
            String objectName = UUID.randomUUID().toString() + extention;
            // 文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return R.ok(filePath);
        } catch (IOException e) {
            log.error("文件上传失败 ：" ,e);
        }
         return R.fail("文件上传失败");
    }
}