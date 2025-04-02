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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServiceTests {

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


                            // 判断业务类型
                            String loginResp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                    "<Msg>\n" +
                                    "<AppHdr>\n" +
                                    "<CharSet>UTF-8</CharSet>\n" +
                                    "<Fr>\n" +
                                    "<AppIdr>DCOMNW</AppIdr>\n" +
                                    "<UsrIdr>CSDCSZ</UsrIdr>\n" +
                                    "</Fr>\n" +
                                    "<To>\n" +
                                    "<AppIdr>TEST</AppIdr>\n" +
                                    "<UsrIdr>ZJB0001</UsrIdr>\n" +
                                    "</To>\n" +
                                    "<BizMsgIdr>M20150813LIRP00000000001</BizMsgIdr>\n" +
                                    "<MsgDefIdr>V2.0</MsgDefIdr>\n" +
                                    "<BizSvc>LIRP</BizSvc>\n" +
                                    "<CreDt>2015-08-13T12:00:34</CreDt>\n" +
                                    "<Rltd>XXXX</Rltd>\n" +
                                    "</AppHdr>\n" +
                                    "<Document>\n" +
                                    "<UserName>TEST</UserName>\n" +
                                    "<VldtRst>0000</VldtRst>\n" +
                                    "<Desc>处理成功</Desc>\n" +
                                    "</Document>\n" +
                                    "</Msg>";


                            String logoutResp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                    "<Msg>\n" +
                                    "<AppHdr>\n" +
                                    "<CharSet>UTF-8</CharSet>\n" +
                                    "<Fr>\n" +
                                    "<AppIdr>DCOMNW</AppIdr>\n" +
                                    "<UsrIdr>CSDCSZ</UsrIdr>\n" +
                                    "</Fr>\n" +
                                    "<To>\n" +
                                    "<AppIdr>TEST</AppIdr>\n" +
                                    "<UsrIdr>ZJB0001</UsrIdr>\n" +
                                    "</To>\n" +
                                    "<BizMsgIdr>M20150813LORP00000000035</BizMsgIdr>\n" +
                                    "<MsgDefIdr>V2.0</MsgDefIdr>\n" +
                                    "<BizSvc>LORP</BizSvc>\n" +
                                    "<CreDt>2015-08-13T13:43:14</CreDt>\n" +
                                    "<Rltd>XXXX</Rltd>\n" +
                                    "</AppHdr>\n" +
                                    "<Document>\n" +
                                    "<UserName>TEST</UserName>\n" +
                                    "<VldtRst>0000</VldtRst>\n" +
                                    "<Desc>处理成功</Desc>\n" +
                                    "</Document>\n" +
                                    "</Msg>";


                            Optional<String> bizSvc = XMLUtil.parse2Optional(s, "//BizSvc");
                            if (bizSvc.isPresent()) {
                                if (bizSvc.get().equals("LIRQ")) {

                                    String bizMsgIdr = s1.get();
                                    ctx.channel().writeAndFlush(loginResp.replace("XXXX", bizMsgIdr));
                                    return;
                                }

                                if (bizSvc.get().equals("LORQ")) {
                                    String bizMsgIdr = s1.get();
                                    ctx.channel().writeAndFlush(logoutResp.replace("XXXX", bizMsgIdr));
                                    return;
                                }
                            }


                            // 登录 发送数据
                            s1.ifPresent(value -> ctx.channel().writeAndFlush("<Msg><BizSvc>LIRP</BizSvc><Rltd>" + value + "</Rltd></Msg>"));
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
