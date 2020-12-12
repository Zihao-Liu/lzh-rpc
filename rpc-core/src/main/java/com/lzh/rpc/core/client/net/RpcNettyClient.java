package com.lzh.rpc.core.client.net;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_ID;
import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_INTERVAL;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.collect.Maps;
import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.JsonUtil;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;
import com.lzh.rpc.core.serialize.strategy.AbstractSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author Liuzihao
 */
public class RpcNettyClient extends SimpleChannelInboundHandler<RpcResponse<? extends Serializable>> {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(RpcNettyClient.class);

    private final EventLoopGroup workerGroup;

    private final ChannelFuture channelFuture;

    private static final Map<String, CompletableFuture<RpcResponse<? extends Serializable>>> RESPONSE_MAP =
            Maps.newConcurrentMap();
    private static final RpcRequest HEART_BEAT_REQUEST = RpcRequest.hearBeat();

    RpcNettyClient(ClientProperty clientProperty, ProviderInstance sourceProperty) throws RpcException {
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(getSocketChannel(clientProperty))
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientProperty.getTimeout());
            channelFuture = bootstrap.connect(sourceProperty.getHost(), sourceProperty.getPort()).sync();
        } catch (Exception e) {
            LOGGER.error("netty client bind error,", e);
            workerGroup.shutdownGracefully();
            throw RpcException.error(RpcErrorEnum.NETTY_ERROR);
        }
    }

    RpcResponse<? extends Serializable> send(RpcRequest request, int timeout) {
        try {
            CompletableFuture<RpcResponse<? extends Serializable>> future = new CompletableFuture<>();
            RESPONSE_MAP.put(request.getTraceInfo().toHashString(), future);
            LOGGER.info("write channel, request: {}", JsonUtil.toJson(request));
            channelFuture.channel().writeAndFlush(request).sync();
            LOGGER.info("write channel end, request: {}", JsonUtil.toJson(request));
            return timeout > 0
                   ? future.get(timeout, TimeUnit.MICROSECONDS)
                   : future.get();
        } catch (InterruptedException | TimeoutException e) {
            LOGGER.error("client send msg timeout,", e);
            return RpcResponse.error(RpcErrorEnum.TIME_OUT);
        } catch (Exception e) {
            LOGGER.error("client send msg error,", e);
            return RpcResponse.error(RpcException.error(e.getMessage()));
        }
    }

    private ChannelInitializer<SocketChannel> getSocketChannel(ClientProperty clientProperty) throws Exception {
        SerializeStrategy serializeStrategy = AbstractSerializeStrategy.getStrategy(clientProperty);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 0, HEART_BEAT_INTERVAL, TimeUnit.SECONDS))
                        .addLast(serializeStrategy.getEncoder(RpcRequest.class))
                        .addLast(serializeStrategy.getDecoder(RpcResponse.class))
                        .addLast(RpcNettyClient.this);
            }
        };
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
            RpcResponse<? extends Serializable> rpcResponse) {
        LOGGER.info("client get request result,{}", rpcResponse);
        if (HEART_BEAT_ID.equals(rpcResponse.getTraceInfo().getTraceId())) {
            return;
        }
        CompletableFuture<RpcResponse<? extends Serializable>> future =
                RESPONSE_MAP.get(rpcResponse.getTraceInfo().toHashString());
        if (future.isDone() || future.isCancelled()) {
            future = new CompletableFuture<>();
            future.complete(rpcResponse);
            RESPONSE_MAP.put(rpcResponse.getTraceInfo().toHashString(), future);
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
