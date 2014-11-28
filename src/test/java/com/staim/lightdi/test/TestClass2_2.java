package com.staim.lightdi.test;

public class TestClass2_2 implements TestInterface2 {
    @Override
    public void test2(String arg1, int arg2) {
        System.out.println("TestClass2_2 - " + arg1 + " - " + arg2);
    }
}
