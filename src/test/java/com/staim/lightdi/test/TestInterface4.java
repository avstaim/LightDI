package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(name="com.staim.lightdi.test.TestClass4")
public interface TestInterface4 {
    TestInterface3 t3();
}
