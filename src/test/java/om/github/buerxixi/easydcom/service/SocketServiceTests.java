package om.github.buerxixi.easydcom.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import om.github.buerxixi.easydcom.hander.MessageEncoder;
import om.github.buerxixi.easydcom.hander.ProtocolFrameDecoder;
import om.github.buerxixi.easydcom.util.XMLUtil;
import org.junit.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class SocketServiceTests {
    // 定义登录响应的 XML 模板
    private static final String LOGIN_RESP_TEMPLATE;
    // 定义登出响应的 XML 模板
    private static final String LOGOUT_RESP_TEMPLATE;

    static {
        try {
            // 读取登录响应 XML 文件内容
            LOGIN_RESP_TEMPLATE = new String(Files.readAllBytes(Paths.get(
                    SocketServiceTests.class.getClassLoader().getResource("login_resp_template.xml").toURI())), StandardCharsets.UTF_8);
            // 读取登出响应 XML 文件内容
            LOGOUT_RESP_TEMPLATE = new String(Files.readAllBytes(Paths.get(
                    SocketServiceTests.class.getClassLoader().getResource("logout_resp_template.xml").toURI())), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load response templates", e);
        }
    }

    @Test
    public void run() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    // 入站解密码器
                    ch.pipeline().addLast(new ProtocolFrameDecoder(), new StringDecoder(StandardCharsets.UTF_8));
                    // 出站编码器
                    ch.pipeline().addLast(new MessageEncoder());
                    // 异常和close
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        // 当连接断开时触发 inactive 事件
                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) {
                            // channel断开
                            System.out.println("channel断开" + ctx.channel());
                        }

                        // 异常断开
                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            System.out.println("异常断开" + cause.getMessage());
                        }
                    });

                    // 数据打印
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, String s) {
                            System.out.println(s);
                            Optional<String> s1 = XMLUtil.parse2Optional(s, "//BizMsgIdr");
                            Optional<String> bizSvc = XMLUtil.parse2Optional(s, "//BizSvc");

                            bizSvc.ifPresent(service -> {
                                String bizMsgIdr = s1.orElse("");
                                switch (service) {
                                    case "LIRQ":
                                        ctx.channel().writeAndFlush(LOGIN_RESP_TEMPLATE.replace("XXXX", bizMsgIdr));
                                        break;
                                    case "LORQ":
                                        ctx.channel().writeAndFlush(LOGOUT_RESP_TEMPLATE.replace("XXXX", bizMsgIdr));
                                        break;
                                    default:
                                        s1.ifPresent(value -> ctx.channel().writeAndFlush("<Msg><BizSvc>LIRP</BizSvc><Rltd>" + value + "</Rltd></Msg>"));
                                        break;
                                }
                            });
                        }
                    });
                }
            });
            Channel channel = serverBootstrap.bind(7231).sync().channel();
            System.out.println("服务端启动成功");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}