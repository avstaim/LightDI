package com.staim.lightdi;

import com.staim.lightdi.implementations.ManagerImpl;
import com.staim.lightdi.interfaces.ImplementationManager;

/**
 * LightDI main factory class
 *
 * Created by alexeyshcherbinin on 28.11.14.
 */
public final class LightDI {
    private static ImplementationManager _instance;
    private static Class<?> _implementationClass = ManagerImpl.class;

    private static final String version = "LightDI v.0.0.3";

    private LightDI() {}

    public static ImplementationManager instance() {
        if (_instance != null) return _instance;
        try {
            _instance = (ImplementationManager)_implementationClass.newInstance();
            return _instance;
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Unable to create Implementation Manager Instance");
        }
    }

    public static String versionString() { return version; }
}
