package com.staim.lightdi.implementations;

import com.staim.lightdi.LightDI;
import com.staim.lightdi.interfaces.Binder;
import com.staim.lightdi.interfaces.Injector;

import java.util.HashMap;
import java.util.Map;

/**
 * Binder Implementation
 *
 * Created by alexeyshcherbinin on 02.12.14.
 */
public class BinderImpl implements Binder {
    private String _packageName = null;
    private Map<Class<?>, Class<?>> _implementationMap = new HashMap<>();

    @Override
    public Binder setPackage(String packageName) {
        _packageName = packageName;
        return this;
    }

    @Override
    public <T, N extends T> Binder bind(Class<T> interfaceClass, Class<N> implementationClass) {
        _implementationMap.put(interfaceClass, implementationClass);
        return this;
    }

    private String getName(String name) {
        if (_packageName == null || _packageName.isEmpty()) return name;
        return _packageName + "." + name;
    }

    @Override
    public Binder bind(String interfaceName, String implementationName) throws ClassNotFoundException, ClassCastException {
        Class<?> interfaceClass = Class.forName(getName(interfaceName));
        Class<?> implementationClass = Class.forName(getName(implementationName));
        addClass(interfaceClass, implementationClass);
        return this;
    }

    @Override
    public Binder bind(Class<?> interfaceClass, String implementationName) throws ClassNotFoundException, ClassCastException {
        Class<?> implementationClass = Class.forName(getName(implementationName));
        addClass(interfaceClass, implementationClass);
        return this;
    }

    @Override
    public Binder bind(String interfaceName, Class<?> implementationClass) throws ClassNotFoundException, ClassCastException {
        Class<?> interfaceClass = Class.forName(getName(interfaceName));
        addClass(interfaceClass, implementationClass);
        return this;
    }

    @Override
    public Binder bind(Map<String, String> implementationMap) throws ClassNotFoundException, ClassCastException {
        for (String interfaceName : implementationMap.keySet())
            bind(interfaceName, implementationMap.get(interfaceName));
        return this;
    }

    @Override
    public Binder apply(Map<Class<?>, Class<?>> implementationMap) throws ClassCastException {
        for (Class<?> interfaceClass : implementationMap.keySet()) {
            Class<?> implementationClass = implementationMap.get(interfaceClass);
            addClass(interfaceClass, implementationClass);
        }
        return this;
    }

    private void addClass(Class<?> interfaceClass, Class<?> implementationClass) throws ClassCastException  {
        if (interfaceClass.isAssignableFrom(implementationClass))
            _implementationMap.put(interfaceClass, implementationClass);
        else
            throw new ClassCastException("Class '" + implementationClass.getSimpleName() + "' cannot be cast to interface '" + interfaceClass.getSimpleName() + "'");
    }

    @Override
    public Injector finish() {
        Injector injector = LightDI.injector();
        injector.bind(this);
        return injector;
    }

    @Override
    public Map<Class<?>, Class<?>> getImplementationMap() { return _implementationMap; }
}
