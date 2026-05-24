package com.ruoyi.ai.controller;

import com.ruoyi.ai.domain.AiBanners;
import com.ruoyi.ai.service.IAiBannersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

@RestController
@RequestMapping("/api/banners")
public class PublicBanners extends BaseController {

    @PostConstruct
    public void init() {
        System.out.println("PublicBanners Controller loaded");
    }

    @GetMapping("/test")
    public String test() {
        return "banners ok";
    }
   @Autowired
   private IAiBannersService aiBannersService;
    @GetMapping("/list")
    public TableDataInfo list(AiBanners aiBanners)
    {
        startPage();
        List<AiBanners> list = aiBannersService.selectAiBannersList(aiBanners);
        return getDataTable(list);
    }
}
