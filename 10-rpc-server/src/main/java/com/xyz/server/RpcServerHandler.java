package com.xyz.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

  private Map<String,Object> registMap;

  public RpcServerHandler(Map<String, Object> registryMap) {
    this.registMap = registryMap;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof InvokeMessage) {
      InvokeMessage message = (InvokeMessage)msg;
      Object result = "提供者没有该办法";
      if (registMap.containsKey(message.getClassName())) {
        Object provider = registMap.get(message.getClassName());
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
