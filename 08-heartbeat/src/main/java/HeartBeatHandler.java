import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/***
 * 客户端处理类
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

  private GenericFutureListener listener;
  private ScheduledFuture<?> shcedule;
  private Bootstrap bootstrap;

  public HeartBeatHandler(Bootstrap bootstrap) {
    this.bootstrap = bootstrap;
  }
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //随机发送心跳
    randomSend(ctx.channel());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    //一旦channel关闭，则将监听器去掉，将不再执行递归调用
    shcedule.removeListener(listener);
    System.out.println("Reconnecting...");
    //重连
    bootstrap.connect("localhost",9861).sync();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }


  private void randomSend(Channel channel) {
    //生成一个[1,8)的随机数，作为发送心跳的时间间隔
    int heartBeatInterval = new Random().nextInt(7) + 1;
    System.out.println(heartBeatInterval + "秒后发送下一次心跳");

    //为eventLoop添加异步定时任务-heartBeatInterval秒后发送心跳
    shcedule = channel.eventLoop().schedule( () -> {
      if (channel.isActive()) {
        System.out.println("向服务器发送心跳");
        channel.writeAndFlush("~PING~");
      } else {
        System.out.println("与服务器的连接已经断开");
      }
    },heartBeatInterval, TimeUnit.SECONDS);

    //定义监听器
    listener = (future) -> {
      randomSend(channel);
    } ;
    //为异步定时任务添加监听器
    shcedule.addListener(listener);
  }
}
