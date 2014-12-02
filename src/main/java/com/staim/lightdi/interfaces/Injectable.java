package com.staim.lightdi.interfaces;

/**
 * Implement this interface to mark class as injectable (optional)
 * Created by alexeyshcherbinin on 02.12.14.
 */
public interface Injectable {
    void onCreate();
    void onInject(Object parent);
}
