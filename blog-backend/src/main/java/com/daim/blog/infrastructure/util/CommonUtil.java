package com.daim.blog.infrastructure.util;

import java.util.UUID;

public class CommonUtil {

    public static String generateId(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
