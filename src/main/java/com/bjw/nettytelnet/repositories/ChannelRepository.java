package com.bjw.nettytelnet.repositories;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@Component
public class ChannelRepository {
    private ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();

    public void put(String key, Channel value) {
        channels.put(key, value);
    }

    public Channel get(String key) {
        return channels.get(key);
    }

    public Stream<Channel> getAllChannels() {
        return channels.values().stream();
    }

    public Stream<String> getAllUsers() { return channels.keySet().stream(); }
    public void remove(String key) {
        channels.remove(key);
    }

    public int size() {
        return channels.size();
    }
}
