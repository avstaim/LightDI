package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;
import com.staim.lightdi.annotations.UsesInternalInjection;

@UsesInternalInjection
public class TestClass4 implements TestInterface4 {
    @Inject
    private TestInterface3 t3;

    @Override
    public TestInterface3 t3() {
        return t3;
    }
}
