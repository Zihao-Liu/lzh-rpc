package com.lzh.rpc.demo.provider;

import org.springframework.stereotype.Service;

import com.lzh.rpc.core.annotation.RpcServer;
import com.lzh.rpc.demo.facade.TestFacade;

/**
 * @author Liuzihao
 */
@RpcServer(types = {TestFacade.class})
@Service
public class TestProviderServiceImpl implements TestFacade {
    public String testMethod1() {
        return "test1";
    }

    public String testMethod1(String str) {
        return str;
    }

    public void testMethod2() {
        System.out.print("test2");
    }
}
