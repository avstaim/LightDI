package com.staim.lightdi.test;

import com.staim.lightdi.annotations.DefaultImplementation;
import com.staim.lightdi.annotations.Singleton;

@DefaultImplementation(TestClass5.class)
@Singleton
public interface TestInterface5 extends TestInterface3 {
    int test5();
    void test5s(int s);
}
