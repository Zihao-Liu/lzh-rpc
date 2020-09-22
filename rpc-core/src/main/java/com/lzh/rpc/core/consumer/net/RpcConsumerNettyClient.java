package com.lzh.rpc.core.consumer.net;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.provider.ProviderInstance;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.core.model.consumer.ConsumerProperty;
import com.lzh.rpc.core.serialize.strategy.AbstractSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_ID;
import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_INTERVAL;

;


/**
 * @author Liuzihao
 */
public class RpcConsumerNettyClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNettyClient.class);

    private EventLoopGroup workerGroup;

    private ChannelFuture channelFuture;

    private static final Map<String, CompletableFuture<RpcResponse>> RESPONSE_MAP = Maps.newConcurrentMap();
    private static final RpcRequest HEART_BEAT_REQUEST = RpcRequest.hearBeat();

    RpcConsumerNettyClient(ConsumerProperty consumerProperty, ProviderInstance sourceProperty) throws RpcException {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(getSocketChannel(consumerProperty))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, consumerProperty.getTimeout());
            channelFuture = bootstrap.connect(sourceProperty.getHost(), sourceProperty.getPort()).sync();
        } catch (Exception e) {
            LOGGER.error("netty client bind error,", e);
            workerGroup.shutdownGracefully();
            throw RpcException.error(RpcErrorEnum.NETTY_ERROR);
        }
    }

    RpcResponse send(RpcRequest request) {
        try {
            CompletableFuture<RpcResponse> future = new CompletableFuture<>();
            RESPONSE_MAP.put(request.getTraceInfo().getTraceId(), future);
            channelFuture.channel().writeAndFlush(request).sync();
            return future.get();
        } catch (Exception e) {
            LOGGER.error("client send msg error,", e);
            return RpcResponse.error(RpcException.error(e.getMessage()));
        }
    }

    private ChannelInitializer<SocketChannel> getSocketChannel(ConsumerProperty consumerProperty) throws Exception {
        SerializeStrategy serializeStrategy = AbstractSerializeStrategy.getStrategy(consumerProperty);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 0, HEART_BEAT_INTERVAL, TimeUnit.SECONDS))
                        .addLast(serializeStrategy.getEncoder(RpcRequest.class))
                        .addLast(serializeStrategy.getDecoder(RpcResponse.class))
                        .addLast(RpcConsumerNettyClient.this);
            }
        };
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("client get request result,{}", rpcResponse);
        }
        if (HEART_BEAT_ID.equals(rpcResponse.getTraceInfo().getTraceId())) {
            return;
        }
        CompletableFuture<RpcResponse> future = RESPONSE_MAP.get(rpcResponse.getTraceInfo().getTraceId());
        if (future.isDone() || future.isCancelled()) {
            future = new CompletableFuture<>();
            future.complete(rpcResponse);
            RESPONSE_MAP.put(rpcResponse.getTraceInfo().getTraceId(), future);
        } else {
            future.complete(rpcResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("netty client caught exception,", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            channelFuture.channel().writeAndFlush(HEART_BEAT_REQUEST).sync();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    boolean isValidate() {
        return this.channelFuture.channel() != null && this.channelFuture.channel().isActive();
    }

    void close() {
        LOGGER.info("netty client closed");
        this.channelFuture.channel().close();
        this.workerGroup.shutdownGracefully();
    }
}
