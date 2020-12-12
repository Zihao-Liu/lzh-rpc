package com.lzh.rpc.core.server.net;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_ID;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcEmptyResult;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.processor.ProcessorMeta;
import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.processor.ProcessorFactory;
import com.lzh.rpc.core.server.factory.BaseServerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author Liuzihao
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(ServerHandler.class);

    private final ExecutorService executorService;
    private final ServerProperty serverProperty;
    private static final RpcResponse<RpcEmptyResult> HEART_BEAT_RESPONSE = RpcResponse.hearBeat();

    ServerHandler(ServerProperty serverProperty) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("server-handler-thread-%d")
                .setDaemon(true)
                .build();
        Integer threadNumber = serverProperty.getThreadNum();
        executorService = new ThreadPoolExecutor(threadNumber, threadNumber, 5000L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        this.serverProperty = serverProperty;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) {
        LOGGER.info("rpc provider accept request: {}", request);
        if (HEART_BEAT_ID.equals(request.getTraceInfo().getTraceId())) {
            ctx.writeAndFlush(HEART_BEAT_RESPONSE);
            return;
        }
        try {
            executorService.execute(() -> {
                RpcResponse<? extends Serializable> response = RpcResponse.success(null);
                ProcessorMeta meta = new ProcessorMeta(serverProperty, request, response);
                try {
                    ProcessorFactory.doPreResponse(meta);
                    handle(request, response);
                    ProcessorFactory.doPostResponse(meta);
                    LOGGER.info("rpc provider receive, request: {}, response: {}", request, response);
                } catch (RpcException e) {
                    LOGGER.error("rpc provider process request error, caused by: [{}]", e.getMessage());
                    response.setError(e);
                    ProcessorFactory.doPostResponse(meta);
                } catch (Exception e) {
                    LOGGER.error("rpc provider process request error, caused by: ", e);
                    response.setError(e);
                    ProcessorFactory.doPostResponse(meta);
                }
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
    private <T extends Serializable> void handle(RpcRequest request, RpcResponse<T> response)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String className = request.getClassName();
        Class<?> objClz = Class.forName(className);
        Object obj = BaseServerFactory.getService(objClz);
        // 获取调用的方法名称。
        String methodName = request.getMethodName();
        // 参数类型
        Class<?>[] paramsTypes = request.getParamTypes();
        // 具体参数。
        Object[] params = request.getParams();
        // 调用实现类的指定的方法并返回结果。
        Method method = objClz.getMethod(methodName, paramsTypes);
        if (method.getReturnType() != void.class) {
            response.setResult((T) method.invoke(obj, params));
            return;
        }
        method.invoke(obj, params);
        response.setResult((T) new RpcEmptyResult());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void shutdown() {
        if (Objects.nonNull(executorService) && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
