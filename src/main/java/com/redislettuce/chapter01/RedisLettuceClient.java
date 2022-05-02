package com.redislettuce.chapter01;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisLettuceClient {
    public static void main(String[] args) {
        RedisClient redisClient = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.set("hello", "Hello, Redis!");

        connection.close();
        redisClient.shutdown();
    }
}
