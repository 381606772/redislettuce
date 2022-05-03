import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RedisHashTest {

    /**
     * Sets field in the hash stored at key to value. If key does not exist, a new key holding
     * a hash is created. If field already exists in the hash, it is overwritten.
     */
    @Test
    public void hset() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        Map<String, String> map = new HashMap<>();
        map.put("hellofield", "hello");
        map.put("worldfield", "world");

        RedisFuture<Long> myhash = commands.hset("myhash", map);

        connection.close();
        client.shutdown();
    }

    @Test
    public void hget() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<String> hget = commands.hget("myhash", "hellofield");
        hget.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }
}
