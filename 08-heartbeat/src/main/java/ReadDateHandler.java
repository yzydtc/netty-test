import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.net.SocketAddress;

/***
 * 服务端处理类
 */
public class ReadDateHandler extends ChannelInboundHandlerAdapter {

  //创建channelGroup
  private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
   System.out.println("接收到客户端发送的数据:" + msg);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent)evt).state();
      if(state == IdleState.READER_IDLE) {
        ctx.disconnect();
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
