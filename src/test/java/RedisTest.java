import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;

import java.util.List;

public class RedisTest {

    @Test
    public void test01() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();
        String value = commands.get("hello");

        System.out.println("value = " + value);

        connection.close();
        client.shutdown();
    }

    @Test
    public void test02() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<String> future = commands.get("hello");
        future.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }

    @Test
    public void test03() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        commands.get("hello").thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    @Test
    public void test04() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();

        RedisFuture<List<String>> future = commands.keys("*");
        future.thenAccept(keys -> {
            keys.forEach(System.out::println);
        });

        connect.close();
        client.shutdown();
    }

    /**
     * set key value XX: Only set the key if it already exist.
     */
    @Test
    public void operateString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        SetArgs setArgs = SetArgs.Builder.xx();
        RedisFuture<String> future = commands.set("phone", "18902835052", setArgs);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * setnx: Set the value of a key, only if the key does not exist.
     * return: 1 if the key was set 0 if the key was not set.
     */
    @Test
    public void operateString02() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<Boolean> setnx = commands.setnx("address", "jinxian");
        setnx.thenAccept(System.out::println);
    }

}
