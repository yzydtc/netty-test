package com.xyz.registry;

import org.junit.Test;

public class RegisterTest {

  @Test
  public void test() throws Exception {
    RegistryCenter center = new ZKRegistryCenter();
    center.register("com.abc.service.SomeService2", "localhost:8888");
    System.in.read();
  }
}
