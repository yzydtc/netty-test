package com.xyz.discovery;

import com.xyz.constant.ZKConstant;
import com.xyz.loadbalance.RandomLoadBalance;
import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZKServiceDiscovery implements ServiceDiscovery{
  private CuratorFramework client;
  private List<String> servers;

  public ZKServiceDiscovery() {
    client = CuratorFrameworkFactory.builder()
        .connectString(ZKConstant.ZK_CLUSTER)
        .connectionTimeoutMs(10000)
        .sessionTimeoutMs(4000)
        .retryPolicy(new ExponentialBackoffRetry(1000,10))
        .build();
    client.start();
  }

  @Override
  public String discover(String serviceName) {
    String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;
    try {
      servers = client.getChildren()
          .usingWatcher((CuratorWatcher)event -> {
            servers = client.getChildren().forPath(servicePath);
          }).forPath(servicePath);

      if(servers.size() == 0) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new RandomLoadBalance().choose(servers);

  }
}
