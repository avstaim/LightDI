package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(TestClass4.class)
public interface TestInterface4 {
    TestInterface3 t3();
}
