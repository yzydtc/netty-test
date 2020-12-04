package com.xyz.service;

public class AlipaySomeService implements SomeService{

  @Override
  public String doSome(String depart) {
    return depart +"欢迎 alipay";
  }
}
