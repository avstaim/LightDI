package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;

@DefaultImplementation(TestClass2_1.class)
public interface TestInterface2 {
    void test2(String arg1, int arg2);
}
