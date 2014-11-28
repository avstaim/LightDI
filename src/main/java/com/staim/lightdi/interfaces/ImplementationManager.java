package com.staim.lightdi.interfaces;

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
}
