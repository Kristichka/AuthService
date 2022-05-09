package net.absoft;

import org.testng.annotations.Factory;

public class DummyTestFactory {

    @Factory
    public Object[] factoryMethod() {
        return new Object[]{
                new AuthenticationServiceTest("one"),
                new AuthenticationServiceTest("two")
        };
    }
}