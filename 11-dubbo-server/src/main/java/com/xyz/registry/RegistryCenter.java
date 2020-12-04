package com.xyz.registry;

/***
 * 定义注册中心规范
 */
public interface RegistryCenter {

  /**
   *
   * @param serviceName 服务名称
   * @param serviceAddress IP:port
   */
  void register(String serviceName, String serviceAddress) throws Exception;
}
