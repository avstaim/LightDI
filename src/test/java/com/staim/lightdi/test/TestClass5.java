package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;

@Inject
public class TestClass5 extends TestClass3 implements TestInterface5 {
    @Override
    public int test5() {
        return 5;
    }
}
