package com.staim.lightdi.util;

/**
 * Generic Java Utilities
 * Created by alexeyshcherbinin on 30.09.14.
 */
public final class GenericUtil {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
