package com.staim.lightdi.test;

import com.staim.lightdi.LightDI;
import org.junit.Test;


/**
 * Primary Testing
 *
 * Created by a_shcherbinin on 18.06.14.
 */
public class DITest {
    @Test
    public void testDI() {
        TestInterface1 t1 = LightDI.instance().createInstance(TestInterface1.class);
        t1.test1();
        LightDI.instance().inject(TestInterface1.class, TestClass1_2.class);
        t1 = LightDI.instance().createInstance(TestInterface1.class);
        t1.test1();
        LightDI.instance().inject(TestInterface1.class, TestClass1_3.class);
        t1 = LightDI.instance().createInstance(TestInterface1.class);
        t1.test1();

        TestInterface2 t2 = LightDI.instance().createInstance(TestInterface2.class);
        t2.test2("TEST-A", 0);
        LightDI.instance().inject(TestInterface2.class, TestClass2_2.class);
        t2 = LightDI.instance().createInstance(TestInterface2.class);
        t2.test2("TEST-B", 1);
        
        TestInterface3 t3 = LightDI.instance().createInstance(TestInterface3.class);
        t3.t1().test1();
        t3.t2().test2("TEST-C", 2);
        
        LightDI.instance().inject(TestInterface1.class, TestClass1_1.class);
        
        TestInterface4 t4 = LightDI.instance().createInstance(TestInterface4.class);
        t4.t3().t1().test1();
        t4.t3().t2().test2("TEST-D", 3);
    }
}
