import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/***
 * 客户端处理类
 */
public class SomeSocketClientHandler extends SimpleChannelInboundHandler<String> {

  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s)
      throws Exception {
    System.out.println(channelHandlerContext.channel().remoteAddress()+","+s);
    channelHandlerContext.channel().writeAndFlush("fromLocal"+ LocalDateTime.now());
    TimeUnit.MILLISECONDS.sleep(500);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.channel().writeAndFlush("from client: begin talkng");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
