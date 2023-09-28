package com.heyongqiang.zyz.controller;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.service.UchartsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */

@RestController
@RequestMapping("charts")
public class UchartsController {

    @Resource
    private UchartsService uchartsService;

    /**
     * 查询该用户的所有图表数据
     * @param userName
     * @return
     */
    @GetMapping("all")
    public Result getAllCharts(@RequestParam String userName){
        return uchartsService.allCharts(userName);
    }


}
