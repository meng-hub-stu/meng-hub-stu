package com.mdx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mdx.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by liqiang on 3/8/15.
 */
@Slf4j
public class CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    public static void putValueWithExpire(RedisTemplate redisTemplate, String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        int DEFAULT_EXPIRE_HOURS = 2;
        redisTemplate.expire(key, DEFAULT_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    public static void putValueWithExpire(RedisTemplate redisTemplate, String key, String value){
        redisTemplate.opsForValue().set(key, value);
        int DEFAULT_EXPIRE_HOURS = 2;
        redisTemplate.expire(key, DEFAULT_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    public static void addExpire(RedisTemplate redisTemplate, String key){
        int DEFAULT_EXPIRE_HOURS = 2;
        redisTemplate.expire(key, DEFAULT_EXPIRE_HOURS, TimeUnit.HOURS);
    }

    public static void addExpire(RedisTemplate redisTemplate, String key, int timeout, TimeUnit timeUnit){
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static Long getExpire(RedisTemplate redisTemplate, String key) {
        return redisTemplate.getExpire(key);
    }

    public static void putValueWithExpire(RedisTemplate redisTemplate, String key, String value, int timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static void putValueWithExpire(RedisTemplate redisTemplate, String key, Object value, int timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public static void putValue(RedisTemplate redisTemplate, String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public static void putValue(RedisTemplate redisTemplate, String key, int value){
        redisTemplate.opsForValue().set(key, value);
    }

    public static void putDoubleValue(RedisTemplate redisTemplate, String key, Double value){
        redisTemplate.opsForValue().set(key, value);
    }

    public static String getValue(RedisTemplate redisTemplate, String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public static Object getValueToObject(RedisTemplate redisTemplate, String key){
        return redisTemplate.opsForValue().get(key);
    }

    public static void putInHash(RedisTemplate redisTemplate, String hashKey, String key, Object value){
        redisTemplate.opsForHash().put(hashKey, key, value);
    }

    public static void deleteKey(StringRedisTemplate redisTemplate, String key) {
        Boolean delete = redisTemplate.delete(key);
        log.info("redis delete key:{},删除结果：{}", key ,delete);
    }

    public static boolean hasKey(RedisTemplate redisTemplate, String key){
        return redisTemplate.hasKey(key);
    }

    public static <T> T getObjectFromCache(RedisTemplate redisTemplate,String key, String hashKey, Class<T> clazz){
        String cachedResult = (String)redisTemplate.opsForHash().get(key, hashKey);
        if (cachedResult != null) {
            Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(clazz);
            try {
                return serializer.deserialize(cachedResult.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("deserialize cached object failed.",e);
            }
        }
        return null;
    }

    public static <T> void putObjectToCache(RedisTemplate redisTemplate,String key, String hashKey, T object){
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>((Class<T>) object.getClass());
        try{
            byte[] bytes= serializer.serialize(object);
            redisTemplate.opsForHash().put(key, hashKey, new String(bytes,"UTF-8"));
        }catch(UnsupportedEncodingException e) {
            logger.error("serialize object failed.",e);
        }
    }

    public static <T> void putObjectToCacheWithExpire(RedisTemplate redisTemplate, String key, String hashKey, T object) {
        putObjectToCache(redisTemplate, key, hashKey, object);
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
    }



    //set的
    public static Long putToSetWithDayExpire(RedisTemplate redisTemplate, String key, String value, int days) {
        return putToSetWithExpire(redisTemplate, key, value, days, TimeUnit.DAYS);
    }

    public static Long putToSetWithExpire(RedisTemplate redisTemplate, String key, String value, int timeout, TimeUnit timeUnit) {
        Long count = redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
        return count;
    }

    public static Long putToSet(RedisTemplate redisTemplate, String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    public static Long getSetSize(RedisTemplate redisTemplate, String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public static Boolean isMemberSet(RedisTemplate redisTemplate, String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public static String doJacksonSerialize(Object pojo, boolean formatDate) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        /*if(formatDate){
            mapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_SHORTDATE_PATTERN));
        }*/

        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            logger.error("Fail to serialize the Object with Jackson: "+pojo.getClass().getName(),e);
        }
        return null;
    }

    public static <T> T doJacksonDeserialize(String objectInJSON, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(objectInJSON, clazz);
        } catch (IOException e) {
            logger.error("Fail to deserialize the JSON to " + clazz.getName()+" : "+objectInJSON,e);
        }
        return null;
    }

    public static String doJacksonSerialize(Object pojo){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            log.error("Fail to serialize the Object with Jackson: "+pojo.getClass().getName(),e);
            return null;
        }
    }

    public static <T> List<T> doListJacksonDeserialize(String objectInJSON, Class<T> pojo) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, pojo);

        try {
            return mapper.readValue(objectInJSON, type);
        } catch (IOException e) {
            logger.error("Fail to deserialize the JSON to " + List.class.getName() +"_"+pojo.getName() + " : " + objectInJSON,e);
            throw new BusException(20002, "Errors when deserializing JSON");
        }
    }
}
