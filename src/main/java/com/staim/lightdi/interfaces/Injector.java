package com.staim.lightdi.interfaces;

import java.util.Map;

/**
 * Implementation Manager Interface
 * Created by alexeyshcherbinin on 28.11.14.
 */
@SuppressWarnings("UnusedDeclaration")
public interface Injector {
    <T> T getInstance(Class<T> interfaceClass);

    <T> T createInstance(Class<T> interfaceClass);
    <T> T createInstance(Class<T> interfaceClass, Object... arguments);

    void inject(Class<?> interfaceClass, String fullName) throws ClassNotFoundException, ClassCastException;
    void inject(Class<?> interfaceClass, String packageName, String implementationName) throws ClassNotFoundException, ClassCastException;
    <T, N extends T> void inject(Class<T> interfaceClass, Class<N> implementationClass);

    void inject(Map<Class<?>, Class<?>> implementationMap);
    void injectNames(Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException;
    void injectNames(Map<Class<?>, String> packageMap, Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException;
    void injectNames(String packageMap, Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException;

    void cleanSingletons();
}
