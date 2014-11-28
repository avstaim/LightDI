package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(TestClass3.class)
public interface TestInterface3 {
    public TestInterface1 t1();
    public TestInterface2 t2();
}
