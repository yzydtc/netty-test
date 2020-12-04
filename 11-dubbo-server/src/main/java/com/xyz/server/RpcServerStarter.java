package com.xyz.server;

import com.xyz.registry.RegistryCenter;
import com.xyz.registry.ZKRegistryCenter;

public class RpcServerStarter {

  public static void main(String[] args)
      throws Exception {
    RpcServer server = new RpcServer();
    RegistryCenter registryCenter = new ZKRegistryCenter();
    String serviceAddress = "localhost:8888";
    String providerPackage="com.xyz.service";
    server.publish(registryCenter,serviceAddress,providerPackage);
    server.start();
  }
}
