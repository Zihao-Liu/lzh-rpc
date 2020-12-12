package com.lzh.rpc.center.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lzh.rpc.center.model.entity.AppMetaInfoEntity;
import com.lzh.rpc.center.service.AppService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
@Slf4j
@RestController
@RequestMapping("/v1/app")
public class AppController {

    @Resource
    private AppService appService;

    @PostMapping("/open")
    public Integer openApp(@RequestParam("appName") String appName,
            @RequestParam("appDesc") String appDesc) {
        return appService.openApp(appName, appDesc);
    }

    @GetMapping("/{appId}")
    public AppMetaInfoEntity getApp(@PathVariable("appId") Integer appId) {
        return appService.getAppById(appId);
    }
}
