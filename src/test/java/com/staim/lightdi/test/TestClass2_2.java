package com.staim.lightdi.test;

public class TestClass2_2 implements TestInterface2 {
    @Override
    public String test2(String arg1, int arg2) {
        return "TestClass2_2 - " + arg1 + " - " + arg2;
    }
}
