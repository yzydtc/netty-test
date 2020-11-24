import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/***
 * 服务端处理类
 */
public class ChatSocketServerHandler extends ChannelInboundHandlerAdapter {

  //创建channelGroup
  private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Channel channel = ctx.channel();
    group.forEach(
        ch -> {
          if (channel != ch) {
            ch.writeAndFlush(channel.remoteAddress() + ":" + msg + "\n");
          } else {
            ch.writeAndFlush("me:" + msg + "\n");
          }
        }
    );
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    SocketAddress address = channel.remoteAddress();
    System.out.println(address + "---上线");

    group.writeAndFlush(address + "上线");
    group.add(channel);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    Channel channel = ctx.channel();
    SocketAddress address = channel.remoteAddress();
    System.out.println(address + "---下线");
    group.writeAndFlush(address + "下线,在线人数: " + group.size() + "\n");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
