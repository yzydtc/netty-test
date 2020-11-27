package com.xyz.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 服务器类
 */
public class RpcServer {

  private Map<String, Object> registryMap = new HashMap<>();
  private List<String> classCache = new ArrayList<>();

  public void publish(String providerPackage) throws ClassNotFoundException {
    getProviderClass(providerPackage);
    doRegister();
  }

  //获取提供者类名
  private void getProviderClass(String providerPackage) {
    URL resource = this.getClass().getClassLoader()
        .getResource(providerPackage.replaceAll("\\.", "/"));
    File dir = new File(resource.getFile());
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        getProviderClass(providerPackage + "." + file.getName());
      } else if (file.getName().endsWith(".class")) {
        String fileName = file.getName().replace(".class", "").trim();
        classCache.add(providerPackage + "." + fileName);
      }
    }
    classCache.forEach(System.out::println);
  }

  private void doRegister() throws ClassNotFoundException {
    if (classCache.size() == 0) return;

    for (String className : classCache) {
      Class<?> clazz = Class.forName(className);
      Class<?> [] interfaces = clazz.getInterfaces();
      if (interfaces.length == 1) {
        String interfaceName = interfaces[0].getName();

      }
    }
  }

  //定义start
  public void start() throws InterruptedException {
    EventLoopGroup parentGroup = new NioEventLoopGroup();
    EventLoopGroup childGroup = new NioEventLoopGroup();
    
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(parentGroup,childGroup)
          .option(ChannelOption.SO_BACKLOG,1024)
          .childOption(ChannelOption.SO_KEEPALIVE,true)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new ObjectEncoder());
              pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
              pipeline.addLast(new RpcServerHandler(registryMap));
              //pipeline.addLast(null);
            }
          });
      ChannelFuture future = bootstrap.bind(9861).sync();
      System.out.println("微服务已注册启动，端口号9861");
      future.channel().closeFuture().sync();
    } finally {
      parentGroup.shutdownGracefully();
      childGroup.shutdownGracefully();
    }
   
  }
}
