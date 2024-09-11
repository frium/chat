package top.frium.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @date 2024-08-29 14:11:20
 * @description
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HandlerHeartBeat extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent e) {
            if (e.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                String userId = attribute.get();
                log.info("用户{}心跳超时", userId);
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) log.info("写超时");
            else if (e.state() == IdleState.ALL_IDLE) log.info("读写超时");
        }
    }
}
