import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/***
 * 服务端处理器
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

    if(msg instanceof HttpRequest) {
      HttpRequest request = (HttpRequest) msg;
      System.out.println("请求方式:"+request.method().name());
      System.out.println("请求uri:"+request.uri());

      if("/favicon.ico".equals(request.uri())) {
        System.out.println("不处理/favicon.ioc请求");
        return;
      }

      ByteBuf content = Unpooled.copiedBuffer("Hello Netty World", CharsetUtil.UTF_8);
      DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
          HttpResponseStatus.OK,content);
      HttpHeaders headers = response.headers();
      headers.set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
      headers.set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
    cause.printStackTrace();
    ctx.close();
  }
}
