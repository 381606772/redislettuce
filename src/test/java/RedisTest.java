import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;

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
}
