package top.frium.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2024-08-29 14:02:17
 * @description
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketStarter implements Runnable {
    @PostConstruct
    public void start() {
        // 创建一个新的线程来运行 WebSocket 服务器
        new Thread(this).start();
    }

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workGroup = new NioEventLoopGroup();
    @Autowired
    HandlerWebSocket handlerWebSocket;
    @Autowired
    HandlerHeartBeat handlerHeartBeat;

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpServerCodec())//对http协议的支持,使用http编码器,解码器
                                    .addLast(new HttpObjectAggregator(64 * 1024))//聚合解码器,将多个消息转换为单一的FullHttpRequest或FullHttpResponse对象
                                    .addLast(new IdleStateHandler(60, 0, 0))//心跳检测
                                    .addLast(handlerHeartBeat)//自定义心跳检测
                                    .addLast(new WebSocketServerProtocolHandler("/ws"))//将http协议升级为ws协议,对websocket支持
                                    .addLast(handlerWebSocket);//自定义处理器(业务处理器
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(5051).sync();
            log.info("启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动失败");
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
