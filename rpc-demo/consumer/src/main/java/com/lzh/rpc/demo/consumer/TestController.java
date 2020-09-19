package com.lzh.rpc.demo.consumer;

import com.lzh.rpc.common.annotation.RpcConsumer;
import com.lzh.rpc.demo.facade.TestFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RpcConsumer(reference = "test_service")
    private TestFacade testFacade;

    @GetMapping(value = "/")
    public String test() {
        return testFacade.testMethod1();
    }
}
