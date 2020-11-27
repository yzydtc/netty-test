package com.xyz.server;

public class RpcServerStarter {

  public static void main(String[] args)
      throws ClassNotFoundException, InterruptedException, IllegalAccessException, InstantiationException {
    RpcServer server = new RpcServer();
    server.publish("com.xyz.service");
    server.start();
  }
}
