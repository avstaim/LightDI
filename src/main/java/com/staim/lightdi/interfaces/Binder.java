package com.staim.lightdi.interfaces;

import java.util.Map;

/**
 * Binder allows to bind interfaces with light and elegance
 * Created by alexeyshcherbinin on 02.12.14.
 */
public interface Binder {
    Binder setPackage(String packageName);

    <T, N extends T> Binder bind(Class<T> interfaceClass, Class<N> implementationClass);

    Binder bind(String interfaceName, String implementationName) throws ClassNotFoundException, ClassCastException;
    Binder bind(Class<?> interfaceClass, String implementationName) throws ClassNotFoundException, ClassCastException;
    Binder bind(String interfaceName, Class<?> implementationClass) throws ClassNotFoundException, ClassCastException;

    Binder bind(Map<String, String> implementationMap) throws ClassNotFoundException, ClassCastException;
    Binder apply(Map<Class<?>, Class<?>> implementationMap) throws ClassCastException;

    Injector finish();

    Map<Class<?>, Class<?>> getImplementationMap();
}
