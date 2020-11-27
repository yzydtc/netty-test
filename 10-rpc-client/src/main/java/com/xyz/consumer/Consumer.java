package com.xyz.consumer;

import com.xyz.client.RpcProxy;
public class Consumer {

  public static void main(String[] args) {
    SomeService service = RpcProxy.create(SomeService.class);
    System.out.println(service.doSome("hello world"));
  }

}
