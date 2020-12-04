package com.xyz.server;

import com.xyz.dto.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.StringUtil;
import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

  private Map<String,Object> registMap;
  private String providerPackage;
  public RpcServerHandler(Map<String, Object> registryMap) {
    this.registMap = registryMap;
  }
  public RpcServerHandler(Map<String, Object> registryMap,String providerPackage) {
    this.registMap = registryMap;
    this.providerPackage = providerPackage;
  }
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof InvokeMessage) {
      InvokeMessage message = (InvokeMessage)msg;
      Object result = "提供者没有该办法";
      //新加逻辑
      String interfaceName = message.getClassName();
      String simpleInterfaceName = interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
      String prefix = message.getPrefix();
      String key = providerPackage + "." + prefix + simpleInterfaceName;
      if(StringUtil.isNullOrEmpty(prefix)) {
        for (String rkey : registMap.keySet()) {
          if (rkey.endsWith(simpleInterfaceName)) {
            key = rkey;
            break;
          }
        }
      }

      if (registMap.containsKey(key)) {
        Object provider = registMap.get(key);
        result = provider.getClass()
                         .getMethod(message.getMethodName(),message.getParamTypes())
                         .invoke(provider,message.getParamValues());
      }
      ctx.writeAndFlush(result);
      ctx.close();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
   cause.printStackTrace();
   ctx.close();
  }
}
