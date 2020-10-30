import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/***
 * 客户端处理类
 */
public class SomeSocketClientHandler extends ChannelInboundHandlerAdapter {


  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    byte[] msgBytes = "hello world".getBytes();
    ByteBuf byteBuf = null;
    for(int i=0;i<100;i++) {
      byteBuf = Unpooled.buffer(msgBytes.length);
      byteBuf.writeBytes(msgBytes);
      ctx.writeAndFlush(byteBuf);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
