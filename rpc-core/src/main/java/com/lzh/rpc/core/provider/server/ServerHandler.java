package com.lzh.rpc.core.provider.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcEmptyResult;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.request.RpcTraceInfo;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.provider.factory.BaseProviderFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author Liuzihao
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(ServerHandler.class);

    private ExecutorService executorService;

    ServerHandler() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("server-handler-thread-%d").setDaemon(true).build();
        executorService = new ThreadPoolExecutor(1000, 1000, 5000L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) {
        LOGGER.debug("rpc provider accept request: {}", request);
        try {
            executorService.execute(() -> {
                RpcResponse<? extends Serializable> response;
                try {
                    response = RpcResponse.success(handle(request));
                    LOGGER.debug("rpc provider receive, request: {}, response: {}", request, response);
                } catch (RpcException e) {
                    LOGGER.error("rpc provider process request error, caused by: [{}]", e.getMessage());
                    response = RpcResponse.error(e);
                } catch (Exception e) {
                    LOGGER.error("rpc provider process request error, caused by: ", e);
                    response = RpcResponse.error(e);
                }
                RpcTraceInfo traceInfo = RpcTraceInfo.newTrace(request.getTraceInfo().getTraceId());
                response.setTraceInfo(traceInfo);
                ctx.writeAndFlush(response);
            });
        } catch (RejectedExecutionException e) {
            LOGGER.error("netty server reject error, ", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("netty server caught error, ", cause);
        ctx.close();
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> T handle(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String className = request.getClassName();
        Class<?> objClz = Class.forName(className);
        Object obj = BaseProviderFactory.getService(objClz);
        // 获取调用的方法名称。
        String methodName = request.getMethodName();
        // 参数类型
        Class<?>[] paramsTypes = request.getParamTypes();
        // 具体参数。
        Object[] params = request.getParams();
        // 调用实现类的指定的方法并返回结果。
        Method method = objClz.getMethod(methodName, paramsTypes);
        if (method.getReturnType() != null) {
            return (T) method.invoke(obj, params);
        }
        method.invoke(obj, params);
        return (T) new RpcEmptyResult();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
