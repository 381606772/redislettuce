import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.Test;

import java.util.List;

public class RedisListTest {

    /**
     * Insert all the specified values at the head of the list stored at key.
     * If key does not exist, it is created as empty before performing the push operations.
     * when the key holds a value that is not list, an error is retured.
     *
     * it is possible to push multiple elements using a single command call just specifying
     * multiple arguments at the end of the command. Elements are inserted on after the other
     * to the head of the list, from the leftmost element to the rightmost element. So for instance
     * the command lpush mylist a b c will result into a list containing c as first element,
     * b as second element and c as last element.
     */
    @Test
    public void lpush() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        commands.lpush("mylist", "!!");
        commands.lpush("mylist", "world");
        commands.lpush("mylist", "hello");

        connection.close();
        client.shutdown();
    }

    @Test
    public void rpush() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        commands.rpush("mylist", "hello");
        commands.rpush("mylist", "world");
        commands.rpush("mylist", "!!!");

        connection.close();
        client.shutdown();
    }

    /**
     * Returns the specified elements of the list sorted at key. the offsets start and stop are
     * zero-based indexs, with 0 being the first element of the list(the head of the list), 1
     * being the next element and so on.
     *
     * These offsets can also be negative numbers indicating offsets starting at the end of the list.
     * For example, -1 is the last element of the list, -2 the penultimate, and so on.
     *
     *
     * out-of-range indexs
     * Out of range indexs will not produce an error. If start is larger than the end of the list,
     * an empty list is returned. If stop is larger than the actual end of the end list, Redis will
     * treat like the last element of the list.
     */
    @Test
    public void lrange() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<List<String>> mylistFuture = commands.lrange("mylist", 0, -1);
        mylistFuture.thenAccept(mylist -> {
           mylist.forEach(System.out::println);
        });

        connection.close();
        client.shutdown();
    }

    /**
     * Removes and returns the first elements of the list stored at key.
     *
     * By default, the command pops a single element from the beginning of the list.
     * When provided with the optional count argument, the reply will consist of up
     * to count elements, depending on the list's length.
     *
     */
    @Test
    public void lpop() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<List<String>> wordsFutrue = commands.lpop("words", 2);
        wordsFutrue.thenAccept(words -> {
           words.forEach(System.out::println);
        });

        connection.close();
        client.shutdown();
    }
}
