package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(TestClass1_1.class)
public interface TestInterface1 {
    String test1();
}
