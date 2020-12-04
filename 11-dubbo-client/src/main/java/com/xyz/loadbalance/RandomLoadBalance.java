package com.xyz.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance{

  @Override
  public String choose(List<String> servers) {
    int index = new Random().nextInt(servers.size());
    return servers.get(index);
  }
}
