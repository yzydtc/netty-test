package com.xyz.client;

import com.xyz.discovery.ServiceDiscovery;
import com.xyz.discovery.ZKServiceDiscovery;
import com.xyz.dto.InvokeMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/***
 * 代理类
 */
public class RpcProxy {

  //泛型方法
  public static <T> T create(final Class<T> clazz, String prefix) {
    return (T) Proxy.newProxyInstance
        (clazz.getClassLoader(),
            new Class[]{clazz},
            new InvocationHandler() {
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                  //若盗用的是Object的方法，则直接进行本地调用
                  return method.invoke(this, args);
                }
                //完成远程调用
                return rpcInvoke(clazz, method, args, prefix);
              }
            });
  }

  private static <T> Object rpcInvoke(Class<T> clazz, Method method, Object[] args, String prefix)
      throws Exception {
    ServiceDiscovery serviceDiscovery = new ZKServiceDiscovery();
    String serviceAddress = serviceDiscovery.discover(clazz.getName());
    if (serviceAddress == null) {
      return null;
    } ;

    RpcClientHandler handler = new RpcClientHandler();
    NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(eventLoopGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new ObjectEncoder());
              pipeline.addLast(
                  new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
              pipeline.addLast(handler);
            }
          });
      String ip = serviceAddress.split(":")[0];
      String portStr = serviceAddress.split(":")[1];
      Integer port = Integer.valueOf(portStr);
      ChannelFuture future = bootstrap.connect(ip, port).sync();
      //将调用信息传递给Netty服务端
      InvokeMessage message = new InvokeMessage();
      message.setClassName(clazz.getName());
      message.setMethodName(method.getName());
      message.setParamTypes(method.getParameterTypes());
      message.setParamValues(args);
      message.setPrefix(prefix);
      //将远程调用信息发送给Server
      future.channel().writeAndFlush(message);
      future.channel().closeFuture().sync();
    } finally {
      if (eventLoopGroup != null) {
        eventLoopGroup.shutdownGracefully();
      }
    }
    return handler.getResult() + ":" + serviceAddress;
  }
}
