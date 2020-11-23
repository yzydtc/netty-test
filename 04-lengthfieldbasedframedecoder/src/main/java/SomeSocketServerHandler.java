import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/***
 * 服务端处理类
 */
public class SomeSocketServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println(ctx.channel().remoteAddress()+","+msg);
    ctx.channel().writeAndFlush("from serveer" + UUID.randomUUID());
    TimeUnit.MILLISECONDS.sleep(500);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
