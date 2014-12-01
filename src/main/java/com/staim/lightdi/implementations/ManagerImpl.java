package com.staim.lightdi.implementations;

import com.staim.lightdi.annotations.DefaultImplementation;
import com.staim.lightdi.annotations.Inject;
import com.staim.lightdi.annotations.Singleton;
import com.staim.lightdi.interfaces.ImplementationManager;

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

public class ManagerImpl implements ImplementationManager {
    private Map<Class<?>, Class<?>> implementationMap = new HashMap<>();
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
        Class<?> implementationClass = implementationMap.get(interfaceClass);
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
        boolean isSingleton = false;
        if (interfaceClass.isAnnotationPresent(Singleton.class)) {
            isSingleton = true;
            Object singleton = singletons.get(interfaceClass);
            if (singleton != null)
                return cast(singleton);
        }
        T result = createInstance(interfaceClass);
        if (isSingleton) singletons.put(interfaceClass, result);
        return result;
    }

    @Override
    public <T> T createInstance(Class<T> interfaceClass) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            return createInstanceInternal(implementationClass);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public <T> T createInstance(Class<T> interfaceClass, Object... arguments) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            return createInstanceInternal(implementationClass, arguments);
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
    public void inject(Class<?> interfaceClass, String packageName, String implementationName) throws ClassNotFoundException, ClassCastException {
        inject(interfaceClass, packageName + "." + implementationName);
    }

    @Override
    public void inject(Class<?> interfaceClass, String fullName) throws ClassNotFoundException, ClassCastException {
        Class<?> implementationClass = Class.forName(fullName);
        if (implementationClass == null) throw new ClassNotFoundException();
        checkSingletonForInject(interfaceClass);
        if (interfaceClass.isAssignableFrom(implementationClass))
            implementationMap.put(interfaceClass, implementationClass);
        else
            throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
    }

    @Override
    public <T, N extends T> void inject(Class<T> interfaceClass, Class<N> implementationClass) {
        checkSingletonForInject(interfaceClass);
        implementationMap.put(interfaceClass, implementationClass);
    }

    @Override
    public void inject(Map<Class<?>, Class<?>> implementationMap) {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = implementationMap.get(interfaceClass);
            checkSingletonForInject(interfaceClass);
            if (interfaceClass.isAssignableFrom(implementationClass))
                this.implementationMap.put(interfaceClass, implementationClass);
            else
                throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
        }
    }

    @Override
    public void injectNames(Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = Class.forName(implementationMap.get(interfaceClass));
            checkSingletonForInject(interfaceClass);
            if (interfaceClass.isAssignableFrom(implementationClass))
                this.implementationMap.put(interfaceClass, implementationClass);
            else
                throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
        }
    }

    @Override
    public void injectNames(Map<Class<?>, String> packageMap, Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = Class.forName(packageMap.get(interfaceClass) + "." + implementationMap.get(interfaceClass));
            checkSingletonForInject(interfaceClass);
            if (interfaceClass.isAssignableFrom(implementationClass))
                this.implementationMap.put(interfaceClass, implementationClass);
            else
                throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
        }
    }

    @Override
    public void injectNames(String packageName, Map<Class<?>, String> implementationMap) throws ClassNotFoundException, ClassCastException {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = Class.forName(packageName + "." + implementationMap.get(interfaceClass));
            checkSingletonForInject(interfaceClass);
            if (interfaceClass.isAssignableFrom(implementationClass))
                this.implementationMap.put(interfaceClass, implementationClass);
            else
                throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
        }
    }

    @Override
    public void cleanSingletons() {
        singletons.clear();
    }
}
