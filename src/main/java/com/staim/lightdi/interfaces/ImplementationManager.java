package com.staim.lightdi.interfaces;

import java.util.Map;

/**
 * Implementation Manager Interface
 * Created by alexeyshcherbinin on 28.11.14.
 */
@SuppressWarnings("UnusedDeclaration")
public interface ImplementationManager {
    <T> T createInstance(Class<?> interfaceClass);
    <T> T createInstance(Class<?> interfaceClass, Object... arguments);

    void inject(Class<?> interfaceClass, String packageName, String implementationName) throws ClassNotFoundException;
    void inject(Class<?> interfaceClass, Class<?> implementationClass);

    void inject(Map<Class<?>, Class<?>> implementationMap);
    void injectNames(Map<Class<?>, String> implementationMap) throws ClassNotFoundException;
    void injectNames(Map<Class<?>, String> packageMap, Map<Class<?>, String> implementationMap) throws ClassNotFoundException;
    void injectNames(String packageName, Map<Class<?>, String> implementationMap) throws ClassNotFoundException;
}
