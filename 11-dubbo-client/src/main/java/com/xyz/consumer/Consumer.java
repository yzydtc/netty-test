package com.xyz.consumer;

import com.xyz.client.RpcProxy;
import com.xyz.service.SomeService;

public class Consumer {

  public static void main(String[] args) {
    SomeService service = RpcProxy.create(SomeService.class,"Alipay");
    if (service != null) {
      System.out.println(service.doSome("hello world"));
    }
  }
}
