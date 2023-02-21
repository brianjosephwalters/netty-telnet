package com.bjw.nettytelnet.handlers;

import com.bjw.nettytelnet.domains.User;
import com.bjw.nettytelnet.repositories.ChannelRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class LoginHandler extends ChannelInboundHandlerAdapter {

    private final ChannelRepository channelRepository;

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        String s = (String) message;
        if (s.startsWith("login")) {
            var channel = context.channel();
            var user = User.of(s, channel);
            channelRepository.put(user.getUsername(), user.getChannel());
            channel.attr(User.USER_ATTRIBUTE_KEY).set(user);
            context.writeAndFlush("Successfully logged in as %s.\r\n".formatted(user.getUsername()));
        }
    }
}
