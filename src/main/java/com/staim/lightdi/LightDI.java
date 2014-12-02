package com.staim.lightdi;

import com.staim.lightdi.implementations.BinderImpl;
import com.staim.lightdi.implementations.InjectorImpl;
import com.staim.lightdi.interfaces.Binder;
import com.staim.lightdi.interfaces.Injector;

/**
 * LightDI main factory class
 *
 * Created by alexeyshcherbinin on 28.11.14.
 */
public final class LightDI {
    private static Injector _injector;
    private static Class<?> _injectorClass = InjectorImpl.class;
    private static Class<?> _binderClass = BinderImpl.class;

    private static final String versionHeader = "LightDI v.";

    private LightDI() {}

    public static Injector injector() {
        if (_injector != null) return _injector;
        try {
            _injector = (Injector) _injectorClass.newInstance();
            return _injector;
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Unable to create Implementation Manager Instance");
        }
    }

    public static Binder binder() {
        try {
            return (Binder)_binderClass.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Unable to create Implementation Manager Instance");
        }
    }

    public static String versionString() { return versionHeader + LightDI.class.getPackage().getSpecificationVersion(); }
}
