package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;

@Inject
public class TestClass5 extends TestClass3 implements TestInterface5 {
    private int s = 5;

    @Override
    public int test5() {
        return s;
    }

    @Override
    public void test5s(int s) {
        this.s = s;
    }
}
