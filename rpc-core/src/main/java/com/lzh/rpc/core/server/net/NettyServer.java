package com.lzh.rpc.core.server.net;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_INTERVAL;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.DisposableBean;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.server.ServerProperty;
import com.lzh.rpc.core.serialize.strategy.AbstractSerializeStrategy;
import com.lzh.rpc.core.serialize.strategy.SerializeStrategy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * @author Liuzihao
 */
public class NettyServer implements DisposableBean {

    private static final AtomicBoolean SERVER_START = new AtomicBoolean(false);
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(NettyServer.class);

    private ExecutorService executorService;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerHandler serverHandler;

    public void startNetty(ServerProperty providerProperty) {
        if (providerProperty.getPort() <= 0) {
            LOGGER.error("rpc server start error, caused by: [server port bind error], port: [{}]",
                    providerProperty.getPort());
            throw RpcException.error("rpc server start error, caused by: [server port bind error]");
        }
        startOnlyOnce(providerProperty);
    }

    private void start(ServerProperty property) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ChannelFuture future = serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(getChannelInitializer(property))
                    //                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(property.getPort())
                    .sync();
            LOGGER.info("rpc server started success on port: [{}]", property.getPort());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("rpc server started error on port: [{}], ", property.getPort(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            executorService.shutdown();
        }
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer(ServerProperty property) throws Exception {
        SerializeStrategy serializeStrategy = AbstractSerializeStrategy.getStrategy(property);
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                serverHandler = new ServerHandler(property);
                socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 0, HEART_BEAT_INTERVAL * 3L, TimeUnit.SECONDS))
                        .addLast(serializeStrategy.getDecoder(RpcRequest.class))
                        .addLast(serializeStrategy.getEncoder(RpcResponse.class))
                        .addLast(serverHandler);
            }
        };
    }

    @Override
    public void destroy() {
        LOGGER.info("netty server destroy begin ...");
        serverHandler.shutdown();
        executorService.shutdown();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        LOGGER.info("netty server destroy success ...");
    }

    private void startOnlyOnce(ServerProperty property) {
        if (SERVER_START.compareAndSet(false, true)) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("netty-server-thread-%d").build();
            BlockingQueue<Runnable> queue = new SynchronousQueue<>(true);
            executorService = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.MICROSECONDS, queue, threadFactory);
            executorService.execute(() -> this.start(property));
        }
    }
}
