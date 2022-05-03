import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisStringTest {

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
    public void operateSetString01() {
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
    public void operateSetString02() {
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

    /**
     * set key value ex: Set the specified expire time, in seconds.
     */
    @Test
    public void operateSetString03() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        SetArgs setArgs = SetArgs.Builder.ex(Duration.ofSeconds(15));
        RedisFuture<String> future = commands.set("phone", "18658457038", setArgs);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * setrange: overwrites part of the string stored at key, starting at the specified offset, for the entire length
     * of the value. If the offset is larger than the current length of the string at key, the string is padded with
     * zero-bytes to make offset fix it. non-exsiting keys are considered as empty strings, so this command will make
     * sure it holds a string large enough to be able to set value at offset.
     */
    @Test
    public void operateSetString04() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();

        RedisFuture<Long> future = commands.setrange("setrange", 6, "Redis");
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * Returns the length of the string value stored at key. An error is returned when key holds non-string value.
     */
    @Test
    public void operateLengthString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<Long> future = commands.strlen("append");
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * corresponding redis command -- SUBSTR key start end
     *
     * Returns the substring of the string value stored at key, determined by the offsets start and end(both are inclusive)
     * Negative offsets can be used in order to provide an offset starting from the end of the string. So -1 means the last
     * character, -2 the penultimate and so forth.
     *
     *
     * penultimate:   [penˈʌltɪmət] 倒数第二的(second from the last.)
     */
    @Test
    public void operateSubString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<String> future = commands.getrange("substr", 0, 3);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    @Test
    public void operateMgetString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<List<KeyValue<String, String>>> future =
                commands.mget("key1", "key2", "nonexistkey");
        future.thenAccept(values -> {
            values.forEach(keyValue -> {
                System.out.println(keyValue.getKey() + " = " + keyValue.getValue());
            });
        });

        connect.close();
        client.shutdown();
    }

    /**
     * sets the given keys to their respective values. MSET replaces existing values with new values,
     * just as regular set. See MSETNX if you don't want to overwrite existing values.
     *
     * MSET is atomic, so all given keys are set at once. It is not possible for clients to
     * see some of the keys were updated while others are unchanged.
     */
    @Test
    public void operateMsetString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("key1", "Hello_u");
        keyValueMap.put("key2", "World_u");
        RedisFuture<String> future = commands.mset(keyValueMap);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * Increments the number stored at key by one. If the key does not exist, it is set to
     * 0 before performing the operation. An error is returned if the key contains a value of
     * wrong type or contains a string that can not be represented as Integer. This operation
     * is limited to 64 bits signed integers.
     *
     * Note: this is a string operation because Redis does not have a dedicated integer type.
     * The string stored at the key is interpreted as a base-10 64 bits signed integer to
     * execute the operation.
     */
    @Test
    public void operateINCR01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<Long> future = commands.incr("number");
        // RedisFuture<Long> future = commands.incrby("number", 2);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     *
     */
    @Test
    public void operateIncrByFloat() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
        RedisFuture<Double> future = commands.incrbyfloat("float", 1.1);
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }

    /**
     * LCS: longest common subsequence
     *
     * The LCS command implements the longest common subsequence algorithm.
     * Note that this is different than the longest common string algorithm, since
     * matching characters in the string does not need to be contiguous.
     *
     * For instance the LCS between "foo" and "fao" is "fo", since scanning the two strings
     * from  the left to right, the longest common set of characters is composed of
     * the first "f" and then the "o".
     *
     * LCS is very useful in order to evaluate how similar two strings are. Strings can represent
     * many things. For instance if two strings are DNA sequences, the LCS will provide a measure
     * of similarity between the two DNA sequences
     *
     * If strings represent some text edited by some user, the LCS could represent how different
     * the new text is compared to the old one, and so forth.
     *
     *
     * Available since: 7.0.0
     *
     */
    @Test
    public void operateLCS() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();
    }

    /**
     * Append a value to a key:
     * If key already exists and is a string, this command appends the value at the end of the string.
     * If key does not exist it is created and set as an empty string, so APPEND will be similar to SET in this special case.
     */
    @Test
    public void operateAppendString01() {
        RedisURI uri = RedisURI.builder()
                .withHost("192.168.1.236")
                .withPort(7001)
                .build();
        RedisClient client = RedisClient.create(uri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisAsyncCommands<String, String> commands = connect.async();

        RedisFuture<Long> future = commands.append("append", "test string append!");
        future.thenAccept(System.out::println);

        connect.close();
        client.shutdown();
    }
}
