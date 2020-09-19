package com.lzh.rpc.demo.provider;

import com.lzh.rpc.common.annotation.RpcProvider;
import com.lzh.rpc.demo.facade.TestFacade;
import org.springframework.stereotype.Service;

/**
 * @author Liuzihao
 */
@RpcProvider(TestFacade.class)
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
