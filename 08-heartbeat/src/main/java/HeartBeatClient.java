import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.SneakyThrows;

/***
 * 客户端启动类
 */
public class HeartBeatClient {

  @SneakyThrows
  public static void main(String[] args) throws InterruptedException {
    //定义bootstap
    NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    //bootstrap处理逻辑
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(eventLoopGroup)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();

              pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
              pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
              pipeline.addLast(new HeartBeatHandler(bootstrap));
            }
          });
      bootstrap.connect("localhost",9861).sync();
    } finally {

    }
  }
}
