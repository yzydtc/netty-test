import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * 客户端处理类
 */
public class ChatSocketClientHandler extends SimpleChannelInboundHandler<String> {

  protected void channelRead0(ChannelHandlerContext ctx, String msg)
      throws Exception {
    System.out.println(msg);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
