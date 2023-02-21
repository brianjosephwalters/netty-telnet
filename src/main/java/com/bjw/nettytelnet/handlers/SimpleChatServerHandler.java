package com.bjw.nettytelnet.handlers;

import com.bjw.nettytelnet.domains.User;
import com.bjw.nettytelnet.repositories.ChannelRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ChannelHandler.Sharable
public class SimpleChatServerHandler extends ChannelInboundHandlerAdapter {

    private final ChannelRepository channelRepository;

    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.fireChannelActive();
        var remoteAddress = context.channel().remoteAddress().toString();
        context.writeAndFlush("Your remote address is %s .\n\r".formatted(remoteAddress));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        final var s = ((String)message).trim();
        var channel = context.channel();

        if (s.startsWith("login")) {
            context.fireChannelRead(message);
            return;
        }

        String[] split = s.split("::");
        if (split.length == 2) {
            var targetName = split[0];
            var targetChannel = channelRepository.get(targetName);
            if (targetChannel == null) {
                channel.writeAndFlush("who is [%s]?\n\r".formatted(targetName));
                return;
            }
            targetChannel.write(channel.attr(User.USER_ATTRIBUTE_KEY).get().getUsername());
            targetChannel.write(">");
            targetChannel.writeAndFlush("%s\n\r".formatted(split[1]));
            context.channel().writeAndFlush("told [%s]: %s\n\r".formatted(targetName, split[1]));
        } else if (s.startsWith("help")) {
            channel.writeAndFlush("help: display help\n\rusers: list users\n\rUSER::message sends user a message\n\r");
        } else if (s.startsWith("users")) {
            channelRepository.getAllUsers().forEach(user -> channel.write(user + "\n\r"));
            channel.writeAndFlush("");
        } else {
            if (channel.attr(User.USER_ATTRIBUTE_KEY).get() != null) {
                channelRepository.getAllChannels()
                        .filter(c -> !c.equals(channel))
                        .forEach(c -> {
                            c.write(channel.attr(User.USER_ATTRIBUTE_KEY).get().getUsername());
                            c.write(">");
                            c.writeAndFlush("%s\n\r".formatted(s));
                        });
                channel.writeAndFlush("told [all]: %s\n\r".formatted(s));
            } else {
                channel.writeAndFlush("echo: %s\n\r".formatted(s));
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        var user = context.channel().attr(User.USER_ATTRIBUTE_KEY).get();
        if (user != null) {
            channelRepository.remove(user.getUsername());
        }
    }
}
