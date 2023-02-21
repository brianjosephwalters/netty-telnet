package com.bjw.nettytelnet.domains;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class User {

    public static final AttributeKey<User> USER_ATTRIBUTE_KEY = AttributeKey.newInstance("USER");

    private final String username;
    private final Channel channel;

    public static User of(@NonNull String message, @NonNull Channel channel) {
        return new User(message.split(" ")[1], channel);
    }
}
