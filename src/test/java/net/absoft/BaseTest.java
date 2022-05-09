package net.absoft;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    @BeforeMethod
    public void baseSetUp() {
        System.out.println("Base setup");
    }

    @AfterMethod
    public void baseTearDown() {
        System.out.println("Base tear down");
    }
}