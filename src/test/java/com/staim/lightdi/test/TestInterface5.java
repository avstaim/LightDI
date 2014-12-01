package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(TestClass5.class)
public interface TestInterface5 extends TestInterface3 {
    int test5();
}
