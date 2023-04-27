package com.mdx.util;

import com.mdx.service.IUserService;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
public class ClientUtil {
    private static final IUserService userService;

    static{
        userService = SpringUtil.getApplicationContext().getBean(IUserService.class);
    }

    public static String testStr() {
        return getData(R.ok());
    }

    public static<T> T getData(R<T> result) {
        return null != result && result.isOK() ? result.getData() : null;
    }

}
