import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * 服务端启动类
 */
public class SomeServer {

  public static void main(String[] args) throws InterruptedException {
    //定义loopgroup
    EventLoopGroup parentGroup = new NioEventLoopGroup();
    final EventLoopGroup childGroup = new NioEventLoopGroup();
    //主逻辑
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();
              //定义编码，服务器处理器
              pipeline.addLast(new FixedLengthFrameDecoder(11));
              pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
              //pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
              pipeline.addLast(new SomeSocketServerHandler());
            }
          });
      //创建future
      ChannelFuture future = bootstrap.bind(9861).sync();
      System.out.println("服务启动");
      future.channel().closeFuture().sync();
    } finally {
      parentGroup.shutdownGracefully();
      childGroup.shutdownGracefully();
    }
  }
}
