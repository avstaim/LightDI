package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;

@Inject
public class TestClass4 implements TestInterface4 {
    @Inject
    private TestInterface3 t3;

    @Override
    public TestInterface3 t3() {
        return t3;
    }
}
