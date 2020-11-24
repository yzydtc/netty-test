import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/***
 * 服务器启动类
 */
public class HttpServer {

  public static void main(String[] args) throws InterruptedException {

    EventLoopGroup parentGroup = new NioEventLoopGroup();
    EventLoopGroup childGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(parentGroup,childGroup).channel(NioServerSocketChannel.class).childHandler(new HttpChannelInitializer());
      ChannelFuture future = bootstrap.bind(9871).sync();
      future.channel().closeFuture().sync();
    }finally {
      parentGroup.shutdownGracefully();
      childGroup.shutdownGracefully();
    }
  }
}
