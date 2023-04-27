package com.mdx.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Mengdl
 * @date 2023/03/14
 */
public interface ICodeEnum {

    /**
     * code码
     * @return
     */
    Integer getValue();

    /**
     * 值
     * @return
     */
    String getDesc();

    /**
     * code相同
     * @param value
     * @return
     */
    default boolean equals(Integer value) {
        return getValue().equals(value);
    }

    /**
     * desc相同
     * @param desc
     * @return
     */
    default boolean equals(String desc) {
        return getDesc().equals(desc);
    }

    /**
     * 查询数据
     * @param value
     * @param enumClass
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & ICodeEnum> Optional<E> find(Integer value, final Class<E> enumClass) {
        if (value != null) {
            return Arrays.stream(enumClass.getEnumConstants())
                    .filter(v -> v.getValue().equals(value))
                    .findFirst();
        }
        return Optional.empty();
    }

}
