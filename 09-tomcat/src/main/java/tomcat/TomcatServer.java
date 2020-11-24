package tomcat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sernet.Servnet;

public class TomcatServer {

  //key为servnet的简单类名，value为对应的实例
  private Map<String, Servnet> nameToServnetMap = new ConcurrentHashMap<>();
  //key为servnet的简单类名，value为对应servnet类的全限定性类名
  private Map<String, String> nameToClassNameMap = new HashMap<>();

  private String basePackage;

  public TomcatServer(String basePackage) {
    this.basePackage = basePackage;
  }

  //启动tomcat
  public void start() {
    cacheClassName(basePackage);
    runServer();
  }


  private void cacheClassName(String basePackage) {
    //获取指定包中的资源
    URL resource = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
    //若目录中没有任何资源，则直接结束
    if (resource == null) {
      return;
    }

    //将URL资源转换为File资源
    File dir = new File(resource.getFile());
    //遍历指定包及其子孙包中的所有文件，查找所有的.class文件
    for (File file : dir.listFiles()) {
      //为目录，调用递归
      if (file.isDirectory()) {
        cacheClassName(basePackage + "." + file.getName());
      } else {
        String simpleClassName = file.getName().replace("class", "").trim();
        nameToClassNameMap.put(simpleClassName.toLowerCase(), basePackage + "." + simpleClassName);
      }
    }
    System.out.println(nameToClassNameMap);
  }


  //Netty启动server的套路
  private void runServer() {
    EventLoopGroup parent = new NioEventLoopGroup();
    EventLoopGroup child = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(parent, child)
          //指定存放请求的队列的长度
          .option(ChannelOption.SO_BACKLOG, 1024)
          //指定是否用心跳机制来检测长连接的存活性，即客户端的存活性
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();
              pipeline.addLast(new HttpServerCodec());
              pipeline.addLast(new TomcatHandler(nameToServnetMap, nameToClassNameMap));
            }
          });


    } finally {
      parent.shutdownGracefully();
      child.shutdownGracefully();
    }
  }
}
