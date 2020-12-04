package com.xyz.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * 客户端处理类
 */
public class RpcClientHandler extends SimpleChannelInboundHandler {

  private Object result;

  public Object getResult() {
    return this.result;
  }
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg)
      throws Exception {
    this.result=msg;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    cause.printStackTrace();
    ctx.close();
  }
}
