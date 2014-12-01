LightDI is a simple lightweight Java library for Dependency Injection ...

Does not require Java EE, works with Android.

**Using LightDI**

- Provide default implementation of interface:

``` java
@DefaultImplementation(TestClass1_1.class)
public interface TestInterface1 {
    String test1();
}
```

or 

``` java
@DefaultImplementation(name="com.staim.lightdi.test.TestClass1_2")
public interface TestInterface1 {
    String test1();
}
```

or ...

- Inject implementation in runtime:

``` java
LightDI.instance().inject(TestInterface1.class, TestClass1_3.class);
```

- Create instance of your class:

``` java
TestInterface1 t1 = LightDI.instance().createInstance(TestInterface1.class);
```

That is quite simple, isn't it? =) 
Let's review some advanced features.

- (Optional) Make your class use inside injections:

``` java
package com.staim.lightdi.test;

import com.staim.lightdi.annotations.Inject;

@Inject
public class TestClass3 implements TestInterface3 {
    @Inject private TestInterface1 t1;
    @Inject private TestInterface2 t2;

    @Override
    public TestInterface1 t1() {
        return t1;
    }

    @Override
    public TestInterface2 t2() {
        return t2;
    }
}
```

*NOTE:* Do not forget to annotate class @Inject as well to inside injections to work.

- Use your injected type hierarchy:

``` java
TestInterface3 t3 = LightDI.instance().createInstance(TestInterface3.class);
t3.t1().test1());
t3.t2().test2("TEST-C", 2));
```
