package top.frium.websocket.netty;

import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.frium.common.MyException;
import top.frium.common.StatusCodeEnum;
import top.frium.pojo.LoginUser;
import top.frium.uitls.ChannelContextUtil;
import top.frium.uitls.JwtUtil;

import java.util.Objects;

import static top.frium.context.CommonConstant.LOGIN_USER;
import static top.frium.context.CommonConstant.USER_ID;

/**
 *
 * @date 2024-08-29 14:17:38
 * @description
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.name}")
    private String tokenName;

    @Autowired
    ChannelContextUtil channelContextUtil;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 判断消息是否为心跳包
        if ("HEARTBEAT".equals(textWebSocketFrame.text())) {
            System.out.println("Received heartbeat message from client");
            // 处理心跳消息，比如记录客户端在线状态
        }
        Channel channel = channelHandlerContext.channel();
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        log.info("收到消息:{},用户id{}", textWebSocketFrame.text(), userId);

    }

    //通道就绪后调用,一般用于用户初始化
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的连接加入");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接断开");
        channelContextUtil.removeContext( ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("开始握手");
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete complete) {
            log.info("握手成功");
            String userId;
            try {
                String token = complete.requestHeaders().get(tokenName);
                Claims claims = JwtUtil.parseToken(secretKey, token);
                String id = Objects.requireNonNull(claims.get(USER_ID)).toString();
                LoginUser loginUser = (LoginUser) Objects.requireNonNull(redisTemplate.opsForValue().get(LOGIN_USER + id));
                userId = loginUser.getUserId();
            } catch (Exception e) {
                ctx.channel().close();
                throw new MyException(StatusCodeEnum.NOT_LOGIN);
            }
            channelContextUtil.addContext(userId, ctx.channel());
        }
    }
}
