package com.xyz.loadbalance;

import java.util.List;

public interface LoadBalance {
  String choose(List<String>servers);
}
