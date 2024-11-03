package com.kenny.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisOperator {
    @Autowired
    private StringRedisTemplate redisTemplate;

// Key (key), simple key-value operations

    /**
     * Implement command: TTL key, in seconds, returns the remaining time to live for the given key.
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * Implement command: expire set expiration time, in seconds
     *
     * @param key
     * @return
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * Implement command: INCR key, increment key by one
     *
     * @param key
     * @return
     */
    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Implement command: KEYS pattern, find all keys matching the given pattern
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * Implement command: DEL key, delete a key
     *
     * @param key
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

// String

    /**
     * Implement command: SET key value, set a key-value pair (associate a string value with a key)
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Implement command: SET key value EX seconds, set key-value and expiration time (in seconds)
     *
     * @param key
     * @param value
     * @param timeout (in seconds)
     */
    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * Implement command: GET key, return the string value associated with the key
     *
     * @param key
     * @return value
     */
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * Batch query, equivalent to mget
     *
     * @param keys
     * @return
     */
    public List<String> mget(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * Batch query, pipeline
     *
     * @param keys
     * @return
     */
    public List<Object> batchGet(List<String> keys) {
        // nginx -> keepalived (same concept)
        // redis -> pipeline
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection src = (StringRedisConnection) connection;

                for (String k : keys) {
                    src.get(k);
                }
                return null;
            }
        });

        return result;
    }


// Hash

    /**
     * Implement command: HSET key field value, set the value of field in hash key to value
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * Implement command: HGET key field, return the value of the field in hash key
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    /**
     * Implement command: HDEL key field [field ...], delete one or more specified fields in hash key, ignore non-existent fields.
     *
     * @param key
     * @param fields
     */
    public void hdel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * Implement command: HGETALL key, return all fields and values in hash key.
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

// List

    /**
     * Implement command: LPUSH key value, insert a value at the head of list key
     *
     * @param key
     * @param value
     * @return the length of the list after executing LPUSH command.
     */
    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * Implement command: LPOP key, remove and return the head element of list key.
     *
     * @param key
     * @return the head element of list key.
     */
    public String lpop(String key) {
        return (String) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * Implement command: RPUSH key value, insert a value at the tail (right end) of list key.
     *
     * @param key
     * @param value
     * @return the length of the list after executing RPUSH command.
     */
    public long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }
}
