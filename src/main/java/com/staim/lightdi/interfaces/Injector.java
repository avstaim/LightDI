package com.staim.lightdi.interfaces;

/**
 * Implementation Manager Interface
 * Created by alexeyshcherbinin on 28.11.14.
 */
@SuppressWarnings("UnusedDeclaration")
public interface Injector {
    <T> T getInstance(Class<T> interfaceClass);

    <T> T createInstance(Class<T> interfaceClass);
    <T> T createInstance(Class<T> interfaceClass, Object... arguments);

    <T, N extends T> void bind(Class<T> interfaceClass, Class<N> implementationClass);
    void bind(Binder binder);

    void clearSingletons();
}
