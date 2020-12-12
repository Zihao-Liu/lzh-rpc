package com.lzh.rpc.demo.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzh.rpc.core.annotation.RpcClient;
import com.lzh.rpc.demo.facade.TestFacade;

@RestController
public class TestController {
    @RpcClient(reference = "server_test")
    private TestFacade testFacade;

    @GetMapping(value = "/")
    public String test() {
        return testFacade.testMethod1();
    }
}
