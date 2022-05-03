import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.Value;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisZSetTest {

    /**
     * ZADD key [ NX | XX] [ GT | LT] [CH] [INCR] score member
     *
     * Adds all the specified members with the specified scores to the sorted set stored at key.
     *
     * It is possible to specify multiple score / member pairs. If a specified member is already
     * a member of the sorted set, the score is updated and the element reinserted at the right
     * position to ensure the correct ordering.
     *
     * if key does not exist , a new sorted set with the specified members is created, like if
     * the sorted set was empty. if the key exists but does not hold a sorted set, an error is returned.
     *
     * the score values should be the string representation of a double precision floating point number.
     * +inf and -inf values are valid values as well.
     *
     * ZADD options:
     * ZADD supports a list of options, specified after the name of the key, and before the first score argument.
     * Options are:
     * XX: only update elements that exist. don't add new elements.
     * NX: ...
     * ....
     * ...
     *
     */
    @Test
    public void zadd() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();
        Value<String> scoredValue1 = ScoredValue.fromNullable(1, "score1");
        Value<String> scoredValue2 = ScoredValue.fromNullable(2, "score2");
        Value<String> scoredValue3 = ScoredValue.fromNullable(3, "score3");
        Value<String> scoredValue4 = ScoredValue.fromNullable(4, "score4");
        RedisFuture<Long> future = commands.zadd("myScoredValue", scoredValue1, scoredValue2, scoredValue3, scoredValue4);
        future.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }

    /**
     * ZRANGE key min max  [WITHSCORES]
     * ZREVRANGE, ZRANGEBYSCORE, ZREVRANGEBYSCORE, ZRANGEBYLEX, ZREVRANGEBYLEX
     *
     *
     */
    @Test
    public void zrange() throws InterruptedException {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<List<String>> future = commands.zrange("myScoredValue", 0, 3);
        future.thenAccept(values -> {
            values.forEach(System.out::println);
        });

        // since commands.zrange and commands.zrevrange execute async,
        // so sleep 1 second to ensure output of commands.zrange execution
        // before output of commands.zrevrange execution.
        TimeUnit.SECONDS.sleep(1);

        System.out.println("-------------华丽的分割线----------------");

        RedisFuture<List<String>> revFuture = commands.zrevrange("myScoredValue", 0, 3);
        revFuture.thenAccept(values -> {
            values.forEach(System.out::println);
        });

        connection.close();
        client.shutdown();
    }

    /**
     * Returns the score of member in the scored set at key.
     *
     * if member does not exists in the scored set, or key does not exist, nil is returned.
     */
    @Test
    public void zscore() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<Double> zscoreFuture = commands.zscore("myScoredValue", "score4");
        zscoreFuture.thenAccept(System.out::println);

        connection.close();
        client.shutdown();
    }

    @Test
    public void zRemRangeByRank() {
        RedisClient client = RedisClient.create("redis://192.168.1.236:7001");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        RedisFuture<Long> myScoredValue = commands.zremrangebyrank("myScoredValue", 2, 3);

        connection.close();
        client.shutdown();
    }
}
