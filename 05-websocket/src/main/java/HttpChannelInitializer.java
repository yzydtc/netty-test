import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

/***
 * 管道初始化器
 */
public class HttpChannelInitializer extends ChannelInitializer {

  @Override
  protected void initChannel(Channel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast("httpServerCodec",new HttpServerCodec());
    pipeline.addLast("httpServerHandler",new HttpServerHandler());
  }
}
