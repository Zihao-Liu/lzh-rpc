package com.lzh.rpc.core.provider.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.provider.ProviderProperty;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcResponse;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.provider.factory.BaseProviderFactory;
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
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lzh.rpc.common.constant.CommonConstant.HEART_BEAT_INTERVAL;


/**
 * @author Liuzihao
 */
public class NettyServer implements DisposableBean {

    private static final AtomicBoolean SERVER_START = new AtomicBoolean(false);
    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(NettyServer.class);

    private ExecutorService executorService;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void startNetty() throws RpcException {
        ProviderProperty providerProperty = BaseProviderFactory.getProviderProperty();
        if (providerProperty.getPort() <= 0) {
            LOGGER.error("rpc server start error, caused by: [server port bind error], port: [{}]",
                    providerProperty.getPort());
            throw RpcException.error("rpc server start error, caused by: [server port bind error]");
        }
        startOnlyOnce(providerProperty);
    }

    private void start(ProviderProperty property) {
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

    private ChannelInitializer<SocketChannel> getChannelInitializer(ProviderProperty property) {
        SerializeStrategy serializeStrategy = AbstractSerializeStrategy.getStrategy(property.getSerialize());
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 0, HEART_BEAT_INTERVAL * 3, TimeUnit.SECONDS))
                        .addLast(serializeStrategy.getDecoder(RpcRequest.class))
                        .addLast(serializeStrategy.getEncoder(RpcResponse.class))
                        .addLast(new ServerHandler());
            }
        };
    }

    @Override
    public void destroy() {
        LOGGER.info("netty server destroy begin ...");
        executorService.shutdown();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        LOGGER.info("netty server destroy success ...");
    }

    private void startOnlyOnce(ProviderProperty property) {
        if (SERVER_START.compareAndSet(false, true)) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("netty-server-thread-%d").build();
            BlockingQueue<Runnable> queue = new SynchronousQueue<>(true);
            executorService = new ThreadPoolExecutor(1, 1, 0L,
                    TimeUnit.MICROSECONDS, queue, threadFactory);
            executorService.execute(() -> this.start(property));
        }
    }
}
