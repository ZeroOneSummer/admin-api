package com.dms.api.common.validator;

import com.dms.api.common.exception.DMException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new DMException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new DMException(message);
        }
    }
}
