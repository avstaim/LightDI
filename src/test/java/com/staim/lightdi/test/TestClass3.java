package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;

@Inject
public class TestClass3 implements TestInterface3 {
    @Inject private TestInterface1 t1;
    @Inject private TestInterface2 t2;

    @Override
    public TestInterface1 t1() {
        return t1;
    }

    @Override
    public TestInterface2 t2() {
        return t2;
    }
}
