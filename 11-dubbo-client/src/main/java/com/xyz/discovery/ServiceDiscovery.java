package com.xyz.discovery;

/***
 * 服务发现规范
 */
public interface ServiceDiscovery {

  /**
   * 根据服务名称返回提供者的IP+port
   *
   * @param serviceName 服务名称,一般为业务接口名
   * @return 返回经过负载均衡后的主机信息，格式为:IP+PORT
   */
  String discover(String serviceName);
}
