import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * 服务端处理类
 */
public class SomeSocketServerHandler extends SimpleChannelInboundHandler<String> {

  private int counter;

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg)
      throws Exception {
    System.out.println("接收到第" + ++counter + "个包:" + msg);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
