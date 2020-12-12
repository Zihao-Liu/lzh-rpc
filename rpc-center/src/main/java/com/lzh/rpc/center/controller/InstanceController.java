package com.lzh.rpc.center.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.reflect.TypeToken;
import com.lzh.rpc.center.service.InstanceService;
import com.lzh.rpc.common.model.center.RegisterHeader;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Liuzihao
 * Created on 2020-11-06
 */
@Slf4j
@RestController
public class InstanceController {

    @Resource
    private InstanceService instanceService;

    private static final TypeToken<RegisterHeader> HEADER_TYPE = new TypeToken<RegisterHeader>() {
    };

    @PostMapping("/register")
    public Boolean register(@RequestBody ProviderInstance instance, @RequestHeader Map<String, Object> headers) {
        RegisterHeader header = buildHeader(headers);
        return instanceService.register(instance, header);
    }

    @PostMapping("/destroy")
    public Boolean destroy(@RequestBody ProviderInstance instance, @RequestHeader Map<String, Object> headers) {
        RegisterHeader header = buildHeader(headers);
        return instanceService.destroy(instance, header);
    }

    @GetMapping("/discover")
    public List<ProviderInstance> discover(@RequestParam("appName") String appName,
            @RequestHeader Map<String, Object> headers) {
        RegisterHeader header = buildHeader(headers);
        return instanceService.discover(appName, header);
    }

    @GetMapping("/forbid")
    public Boolean forbid(@RequestParam("appName") String appName,
            @RequestParam("address") String address,
            @RequestParam("operate") Integer operate) {
        return instanceService.manualForbid(appName, address, operate);
    }

    private RegisterHeader buildHeader(Map<String, Object> headers) {
        String header = String.valueOf(headers.get("rpc-meta"));
        return JsonUtil.fromJson(header, HEADER_TYPE);
    }
}
