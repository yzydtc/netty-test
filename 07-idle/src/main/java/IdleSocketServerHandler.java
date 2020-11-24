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
public class IdleSocketServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    // 若由空闲事件触发
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      String eventDes = null;
      switch (event.state()) {
        case READER_IDLE: eventDes="读空闲超时:";break;
        case WRITER_IDLE: eventDes="写空闲超时:";break;
        case ALL_IDLE: eventDes="读写空闲超时";
      }
      System.out.println(ctx.channel().remoteAddress()+ ":" +eventDes);
      //ctx.channel().close();
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
