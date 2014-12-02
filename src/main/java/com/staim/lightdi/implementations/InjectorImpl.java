package com.staim.lightdi.implementations;

import com.staim.lightdi.annotations.DefaultImplementation;
import com.staim.lightdi.annotations.Inject;
import com.staim.lightdi.annotations.Singleton;
import com.staim.lightdi.interfaces.Binder;
import com.staim.lightdi.interfaces.Injectable;
import com.staim.lightdi.interfaces.Injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.staim.lightdi.util.GenericUtil.cast;

/**
 * Implementation Manager Implementation
 *
 * Created by alexeyshcherbinin on 28.11.14.
 */

public class InjectorImpl implements Injector {
    private Map<Class<?>, Class<?>> _implementationMap = new HashMap<>();
    private Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> T createInstanceInternal(Class<?> implementationClass) {
        try {
            Constructor<?> constructor = implementationClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            insideInjections(object, implementationClass);
            return cast(object);
        } catch (NoSuchMethodException|InvocationTargetException |InstantiationException|IllegalAccessException e) {
            return null;
        }
    }

    public <T> T createInstanceInternal(Class<?> implementationClass, Object... arguments) {
        try {
            Class<?>[] argumentTypes = new Class<?>[arguments.length];
            for (int i = 0; i < arguments.length; i++) argumentTypes[i] = arguments[i].getClass();
            Constructor<?> constructor = null;

            try {
                constructor = implementationClass.getDeclaredConstructor(argumentTypes);
            } catch (NoSuchMethodException e) {
                for (Constructor<?> constructor_ : implementationClass.getDeclaredConstructors()) {
                    if (constructor_.getParameterTypes().length == arguments.length)
                        constructor = constructor_;
                }
            }
            if (constructor == null) return null;

            constructor.setAccessible(true);
            Object object = constructor.newInstance(arguments);
            insideInjections(object, implementationClass);
            return cast(object);
        } catch (InvocationTargetException|InstantiationException|IllegalAccessException e) {
            return null;
        }
    }

    private Class<?> getImplementationClass(Class<?> interfaceClass) throws ClassNotFoundException {
        Class<?> implementationClass = _implementationMap.get(interfaceClass);
        if (implementationClass == null) {
            if (interfaceClass.isAnnotationPresent(DefaultImplementation.class)) {
                DefaultImplementation annotation = interfaceClass.getAnnotation(DefaultImplementation.class);
                implementationClass = annotation.value();
                if (implementationClass.equals(Object.class)) {
                    implementationClass = Class.forName(annotation.name());
                }
            } else throw new ClassNotFoundException();
        }
        return implementationClass;
    }

    private static List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        Class<?> cls = type;
        while (cls != null && cls != Object.class) {
            Collections.addAll(result, cls.getDeclaredFields());
            cls = cls.getSuperclass();
        }
        return result;
    }

    private void insideInjections(Object implementation, Class<?> implementationClass) {
        if (implementationClass.isAnnotationPresent(Inject.class)) {
            for (Field field : getInheritedPrivateFields(implementationClass)) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> fieldClass = field.getType();
                    Object fieldImplementation = getInstance(fieldClass);
                    if (fieldImplementation instanceof Injectable)
                        ((Injectable)fieldImplementation).onInject(implementation);
                    insideInjections(fieldImplementation, fieldClass);
                    field.setAccessible(true);
                    try {
                        field.set(implementation, fieldImplementation);
                    } catch (IllegalAccessException ignored) {}
                }
            }
        }
    }

    @Override
    public <T> T getInstance(Class<T> interfaceClass) {
        if (interfaceClass.isAnnotationPresent(Singleton.class)) {
            Object singleton = singletons.get(interfaceClass);
            if (singleton != null)
                return cast(singleton);
        }
        return createInstance(interfaceClass);
    }

    @Override
    public <T> T createInstance(Class<T> interfaceClass) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            T result = createInstanceInternal(implementationClass);
            if (interfaceClass.isAnnotationPresent(Singleton.class))
                singletons.put(interfaceClass, result);
            if (result instanceof Injectable)
                ((Injectable)result).onCreate();
            return result;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public <T> T createInstance(Class<T> interfaceClass, Object... arguments) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            T result = createInstanceInternal(implementationClass, arguments);
            if (interfaceClass.isAnnotationPresent(Singleton.class))
                singletons.put(interfaceClass, result);
            if (result instanceof Injectable)
                ((Injectable)result).onCreate();
            return result;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void checkSingletonForInject(Class<?> interfaceClass) {
        if (interfaceClass.isAnnotationPresent(Singleton.class)) {
            singletons.remove(interfaceClass);
        }
    }

    @Override
    public <T, N extends T> void bind(Class<T> interfaceClass, Class<N> implementationClass) {
        checkSingletonForInject(interfaceClass);
        _implementationMap.put(interfaceClass, implementationClass);
    }

    @Override
    public void bind(Binder binder) {
        Map<Class<?>, Class<?>> implementationMap = binder.getImplementationMap();
        for (Class<?> interfaceClass : implementationMap.keySet())
            checkSingletonForInject(interfaceClass);
        _implementationMap.putAll(implementationMap);
    }

    @Override
    public void clearSingletons() {
        singletons.clear();
    }
}
