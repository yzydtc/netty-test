package com.xyz.dto;

import java.io.Serializable;
import lombok.Data;

/***
 * 客户端发送给服务端的服务调用信息
 */
@Data
public class InvokeMessage implements Serializable {
  //服务名称
  private String className;
  //要调用的服务名
  private String methodName;
  //方法参数列表
  private Class<?>[] paramTypes;
  //方法参数值
  private Object[] paramValues;
  //要调用的接口实现类名前缀
  private String prefix;


}
