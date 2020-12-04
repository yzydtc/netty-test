package com.xyz.service;

public class WechatSomeService implements SomeService{

  @Override
  public String doSome(String depart) {
    return depart + "欢迎你 wetchat";
  }
}
