package com.staim.lightdi.test;

import com.staim.lightdi.LightDI;
import junit.framework.Assert;
import org.junit.Test;


/**
 * Primary Testing
 *
 * Created by a_shcherbinin on 18.06.14.
 */
public class DITest {
    @Test
    public void testDI() {
        Assert.assertEquals("LightDI v.null", LightDI.versionString());

        TestInterface1 t1 = LightDI.injector().getInstance(TestInterface1.class);
        Assert.assertNotNull(t1);
        Assert.assertEquals("TestClass1_1", t1.test1());

        LightDI.injector().bind(TestInterface1.class, TestClass1_2.class);
        t1 = LightDI.injector().getInstance(TestInterface1.class);
        Assert.assertNotNull(t1);
        Assert.assertEquals("TestClass1_2", t1.test1());

        LightDI.injector().bind(TestInterface1.class, TestClass1_3.class);
        t1 = LightDI.injector().getInstance(TestInterface1.class);
        Assert.assertNotNull(t1);
        Assert.assertEquals("TestClass1_3", t1.test1());

        TestInterface2 t2 = LightDI.injector().getInstance(TestInterface2.class);
        Assert.assertNotNull(t2);
        Assert.assertEquals("TestClass2_1 - TEST-A - 0", t2.test2("TEST-A", 0));

        LightDI.injector().bind(TestInterface2.class, TestClass2_2.class);
        t2 = LightDI.injector().getInstance(TestInterface2.class);
        Assert.assertNotNull(t2);
        Assert.assertEquals("TestClass2_2 - TEST-B - 1", t2.test2("TEST-B", 1));
        
        TestInterface3 t3 = LightDI.injector().getInstance(TestInterface3.class);
        Assert.assertNotNull(t3);
        Assert.assertNotNull(t3.t1());
        Assert.assertNotNull(t3.t2());
        Assert.assertEquals("TestClass1_3", t3.t1().test1());
        Assert.assertEquals("TestClass2_2 - TEST-C - 2", t3.t2().test2("TEST-C", 2));

        TestInterface4 t4 = LightDI.binder()
                .bind(TestInterface1.class, TestClass1_1.class)
                .bind(TestInterface2.class, TestClass2_1.class)
                .finish()
                .getInstance(TestInterface4.class);

        Assert.assertNotNull(t4);
        Assert.assertNotNull(t4.t3());
        Assert.assertNotNull(t4.t3().t1());
        Assert.assertNotNull(t4.t3().t2());
        Assert.assertEquals("TestClass1_1", t4.t3().t1().test1());
        Assert.assertEquals("TestClass2_1 - TEST-D - 3", t4.t3().t2().test2("TEST-D", 3));

        TestInterface5 t5 =  LightDI.injector().getInstance(TestInterface5.class);
        Assert.assertNotNull(t5);
        Assert.assertNotNull(t5.t1());
        Assert.assertNotNull(t5.t2());
        Assert.assertEquals("TestClass1_1", t5.t1().test1());
        Assert.assertEquals("TestClass2_1 - TEST-E - 4", t5.t2().test2("TEST-E", 4));
        Assert.assertEquals(5, t5.test5());
        t5.test5s(6);
        Assert.assertEquals(6, t5.test5());

        TestInterface5 t5a =  LightDI.injector().getInstance(TestInterface5.class);
        Assert.assertEquals(6, t5a.test5());
        t5.test5s(7);
        Assert.assertEquals(7, t5a.test5());
        t5a.test5s(8);
        Assert.assertEquals(8, t5.test5());
    }
}
