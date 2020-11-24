package tomcat;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.StringUtil;
import sernet.NettyResponse;

/**
 * Tomcat中对Servnet规范的默认实现
 */
public class DefaultNettyResponse implements NettyResponse {
  private HttpRequest request;
  private ChannelHandlerContext context;

  public DefaultNettyResponse(HttpRequest request,ChannelHandlerContext context) {
    this.request = request;
    this.context = context;
  }

  @Override
  public void write(String content) throws Exception {
    //处理context为null
    if (StringUtil.isNullOrEmpty(content)) {
      return;
    }
    //创建响应对象
    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
        HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.getBytes("UTF-8")));
    //获取相应头
    HttpHeaders headers = response.headers();
    //设置响应体类型
    headers.set(HttpHeaderNames.CONTENT_TYPE,"text/json");
    //设置响应体长度
    headers.set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
    //设置缓存过期时间
    headers.set(HttpHeaderNames.EXPIRES,0);
    //若HTTP请求是长连接，则响应也使用长连接
    if (HttpUtil.isKeepAlive(request)) {
      headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    }
    //将响应写入channel
    context.writeAndFlush(response);
  }
}
