package com.github.buerxixi.easydcom.service.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;
import com.github.buerxixi.easydcom.hander.ExceptionHandler;
import com.github.buerxixi.easydcom.hander.HeartbeatHandler;
import com.github.buerxixi.easydcom.hander.MessageEncoder;
import com.github.buerxixi.easydcom.hander.ProtocolFrameDecoder;
import com.github.buerxixi.easydcom.hander.ReadMessageHandler;
import com.github.buerxixi.easydcom.hander.ServerDisconnectionHandler;
import com.github.buerxixi.easydcom.service.IClientService;
import com.github.buerxixi.easydcom.util.DCOMConstant;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Log4j2
public class ClientServiceImpl implements IClientService {

    private NioSocketChannel channel = null;
    private NioEventLoopGroup group = null;

    @Override
    public void connect(String host, int port) {
        group = new NioEventLoopGroup();
        try {
            channel = (NioSocketChannel) new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DCOMConstant.SOCKET_TIMEOUT * 1000)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 服务器断开类
                            ch.pipeline().addLast(new ServerDisconnectionHandler());
                            // 打印日志
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            // 解码数据
                            ch.pipeline().addLast(new ProtocolFrameDecoder(), new StringDecoder(StandardCharsets.UTF_8));
                            // 编码数据
                            ch.pipeline().addLast(new MessageEncoder());
                            // 心跳检测
                            ch.pipeline().addLast(
                                    new IdleStateHandler(30, 10, 0, TimeUnit.SECONDS),
                                    new HeartbeatHandler()
                            );
                            // 读取消息报文
                            ch.pipeline().addLast(new ReadMessageHandler());
                            // 异常处理
                            ch.pipeline().addLast(new ExceptionHandler());
                        }
                    })
                    .connect(host, port)
                    // 同步等待连接成功
                    .sync().channel();
            // 等待关闭
            // 原代码：channel.closeFuture().addListener(future -> {
            //             group.shutdownGracefully();
            //         });
            // 修改后：
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (InterruptedException e) {
            log.error("连接失败", e);
        }
    }

    @Override
    public void send(String message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }

    @Override
    public void close() {
        if (channel != null && channel.isActive()) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                log.error("关闭失败", e);
            }
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    @Override
    public boolean isConnected() {
        return channel != null && channel.isActive();
    }
}
