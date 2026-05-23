package com.ruoyi.ai.controller;

import com.ruoyi.ai.domain.Herbs;
import com.ruoyi.ai.service.IHerbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序药材百科接口
 */
@RestController
@RequestMapping("/api")
public class AicherbController {

    @Autowired
    private IHerbsService herbsService;

    /**
     * 获取所有药材数据（供微信小程序使用）
     * 访问路径: GET /prod-api/api/Aicherb
     */
    @GetMapping("/Aicherb")
    public List<Herbs> listAllHerbs() {
        return herbsService.selectHerbsList(new Herbs());
    }
    @GetMapping("/Aicherb/{id}")
    public Herbs getHerbById(@PathVariable Long id) {
        return herbsService.selectHerbsById(id);
    }
}