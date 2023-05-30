package com.mdx.util;

import cn.hutool.core.date.DatePattern;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：YangYaNan
 * @date ：Created in 20:12 2022/6/30
 * @description ：Gson序列化工具类
 * @version: 1.0
 */
@Slf4j
public class GsonSerializeUtils {

    public static final Gson gsonToJson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate localDate, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
                }
            })
            .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
                    String dateString = simpleDateFormat.format(date);
                    return new JsonPrimitive(dateString);
                }
            })
            .serializeNulls()
            .create();


    public static final  Gson gsonToBean = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
                }
            })
            .registerTypeAdapter(Date.class, new JsonDeserializer() {
                @SneakyThrows
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
                    return simpleDateFormat.parse(json.getAsJsonPrimitive().getAsString());
                }
            })
            .create();

    public static String toString(Object obj) {
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return gsonToJson.toJson(obj);
        } catch (Exception e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T toBean(String json, Class<T> tClass) {
        if(json == null || json.equals("")){
            return null;
        }
        try {
            return gsonToBean.fromJson(json, tClass);
        } catch (Exception e) {
            log.error("json解析Bean出错：" + json, e);
            return null;
        }
    }

    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return gsonToBean.fromJson(json, new TypeToken<List<E>>(){}.getType());
        } catch (Exception e) {
            log.error("json解析List出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return gsonToBean.fromJson(json, new TypeToken<Map<K, V>>(){}.getType());
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json,  TypeToken typeToken) {
        try {
            return gsonToBean.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

}

