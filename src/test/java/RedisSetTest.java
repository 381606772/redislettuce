import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.Test;

import java.util.Set;

public class RedisSetTest {

    /**
     * SADD
     *
     * Add the specified members to the set stored at key. Specified members that are already
     * a member of this set are ignored. If key does not exist, a new set is created before
     * adding the specified members.
     *
     * an errors is returned when the value stored at key is not a set.
     */
    @Test
    public void sadd() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Long> future = commands.sadd("users", "zhangshan", "wangwu", "lisi");
        future.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }


    /**
     * smembers
     *
     * Returns all the members of the set value stored at key.
     *
     * This has the same effect as running SINTER with one argument key.
     *
     */
    @Test
    public void smembers() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Set<String>> future = commands.smembers("users");
        future.thenAccept(users -> {
            users.forEach(user -> {
                System.out.println("user = " + user);
            });
        });

        connection.close();
        client.shutdown();
    }

    /**
     * SCARD
     *
     * Returns the set cardinality(number of the elements) of the set stored at key.
     *
     */
    @Test
    public void scard() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Long> future = commands.scard("users");
        future.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }

    /**
     * SDIFF
     *
     * Returns the members of the set resulting from the difference between the first set and successvie sets.
     */
    @Test
    public void sdiff() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Set<String>> future = commands.sdiff("key1", "key2", "key3");
        future.thenAccept(diffSet -> {
           diffSet.forEach(diff -> {
               System.out.println("diff = " + diff);
           });
        });

        connection.close();
        client.shutdown();
    }

    /**
     * SDIFFSTORE destination key [key ...]
     *
     * This command is equal to SDIFF, but instead of returning the resulting set, it is stored in destination.
     *
     * if the destination already exists, it is overwritten.
     */
    @Test
    public void sdiffStore() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Long> future = commands.sdiffstore("key", "key1", "key2", "key3");
        future.thenAccept(System.out::print);

        connection.close();
        client.shutdown();
    }

    /**
     * 交集
     * Returns the members of the set resulting from the intersection of all the given sets.
     * For example:
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * SINTER key1 key2 key3 = {c}
     *
     * Keys that do not exist are considered to be empty sets. With one of the keys being an
     * empty set, the resulting set is also empty (since set intersection with an empty set
     * always results in an empty set)
     */
    @Test
    public void sinter() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        RedisFuture<Set<String>> sinterFuture = commands.sinter("key1", "key2", "key3");
        sinterFuture.thenAccept(sinter -> {
           sinter.forEach(System.out::println);
        });

        connection.close();
        client.shutdown();
    }
}
