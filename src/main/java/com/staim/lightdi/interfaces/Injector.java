package com.staim.lightdi.interfaces;

/**
 * Implementation Manager Interface
 * Created by alexeyshcherbinin on 28.11.14.
 */
@SuppressWarnings("UnusedDeclaration")
public interface Injector {
    /**
     * Create Instance
     * @param interfaceClass - Interface Class
     * @param <T> - Interface Class
     * @return new Instance
     */
    <T> T createInstance(Class<T> interfaceClass);

    /**
     * Create Instance with constructor arguments
     * @param interfaceClass - Interface Class
     * @param arguments - Constructor Arguments
     * @param <T> - Interface Class
     * @return new Instance
     */
    <T> T createInstance(Class<T> interfaceClass, Object... arguments);

    /**
     * Get Instance. If instance is marked as @Singleton and it already exists, it is re-used.
     * @param interfaceClass - Interface Class
     * @param <T> - Interface Class
     * @return instance
     */
    <T> T getInstance(Class<T> interfaceClass);


    /**
     * Bind without binder
     * @param interfaceClass - Interface Class
     * @param implementationClass - Implementation Class
     * @param <T> - Interface Class
     * @param <N> - Implementation Class
     */
    <T, N extends T> void bind(Class<T> interfaceClass, Class<N> implementationClass);

    /**
     * Bind with binder
     * @param binder - Binder instance
     */
    void bind(Binder binder);

    /**
     * Try Bind without binder.
     * @param interfaceClass - Interface Class
     * @param implementationClass - Implementation Class
     * @param <T> - Interface Class
     * @param <N> - Implementation Class
     * @return true on success, false when operation is locked by another thread
     */
    <T, N extends T> boolean tryBind(Class<T> interfaceClass, Class<N> implementationClass);

    /**
     * Try Bind with binder
     * @param binder - Binder instance
     * @return true on success, false when operation is locked by another thread
     */
    boolean tryBind(Binder binder);

    /**
     * Clear All Singleton Instances. Singleton instances will be re-created on new requests.
     * Created instances are not destroyed immediately after this call, but if there is no more references to them, they will be destroyed by GC.
     */
    void clearSingletons();
}
