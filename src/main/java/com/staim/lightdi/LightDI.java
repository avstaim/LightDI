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
    private static Class<?> _injectorClass = InjectorImpl.class;
    private static Class<?> _binderClass = BinderImpl.class;

    private static final String versionHeader = "LightDI v.";

    private LightDI() {}

    private static class InjectorHolder {
        static final Injector INSTANCE = getInstance();

        private static Injector getInstance() {
            try {
                return  (Injector)_injectorClass.newInstance();
            } catch (InstantiationException|IllegalAccessException e) {
                throw new RuntimeException("Unable to create Implementation Manager Instance");
            }
        }
    }

    /**
     * Get Injector Instance
     *
     * @return Injector Instance
     */
    public static Injector injector() {
        return InjectorHolder.INSTANCE;
    }

    /**
     * Get Binder Instance
     *
     * @return Binder Instance
     */
    public static Binder binder() {
        try {
            return (Binder)_binderClass.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Unable to create Implementation Manager Instance");
        }
    }

    /**
     * Get LightDI Version
     * @return version string
     */
    public static String versionString() { return versionHeader + LightDI.class.getPackage().getSpecificationVersion(); }
}
