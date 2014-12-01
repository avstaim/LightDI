package com.staim.lightdi.implementations;

import com.staim.lightdi.annotations.DefaultImplementation;
import com.staim.lightdi.annotations.Inject;
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

    public <T> T createInstanceInternal(Class<?> implementationClass) {
        try {
            Constructor<?> constructor = implementationClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            insideInjections(object, implementationClass);
            return cast(object);
        } catch (NoSuchMethodException|InvocationTargetException |InstantiationException|IllegalAccessException e) {
            //Log.e(TAG, "Error Instantiating " + implementationName + ": ", e);
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
            //Log.e(TAG, "Error Instantiating " + implementationName + ": ", e);
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
                    Object fieldImplementation = createInstance(fieldClass);
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
    public <T> T createInstance(Class<?> interfaceClass) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            return createInstanceInternal(implementationClass);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public <T> T createInstance(Class<?> interfaceClass, Object... arguments) {
        try {
            Class<?> implementationClass = getImplementationClass(interfaceClass);
            return createInstanceInternal(implementationClass, arguments);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void inject(Class<?> interfaceClass, String packageName, String implementationName) throws ClassNotFoundException {
        Class<?> implementationClass = Class.forName(packageName + "." + implementationName);
        if (implementationClass == null) throw new ClassNotFoundException();
        inject(interfaceClass, implementationClass);
    }

    @Override
    public void inject(Class<?> interfaceClass, Class<?> implementationClass) {
        implementationMap.put(interfaceClass, implementationClass);
    }

    @Override
    public void inject(Map<Class<?>, Class<?>> implementationMap) {
        this.implementationMap.putAll(implementationMap);
    }

    @Override
    public void inject(Map<Class<?>, String> packageMap, Map<Class<?>, String> implementationMap) throws ClassNotFoundException {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = Class.forName(packageMap.get(interfaceClass) + "." + implementationMap.get(interfaceClass));
            this.implementationMap.put(interfaceClass, implementationClass);
        }
    }
}
