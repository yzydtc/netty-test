package com.xyz.service;

/***
 * 业务接口实现类，即服务真正的提供者
 */
public class SomeServiceImpl implements SomeService {

  @Override
  public String doSome(String depart) {

    return depart + "欢迎你";
  }
}
