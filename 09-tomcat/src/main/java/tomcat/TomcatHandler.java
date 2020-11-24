package tomcat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sernet.NettyRequest;
import sernet.NettyResponse;
import sernet.Servnet;

public class TomcatHandler extends ChannelInboundHandlerAdapter {

  //key为servnet的简单类名，value为对应的实例
  private Map<String, Servnet> nameToServnetMap = new ConcurrentHashMap<>();
  //key为servnet的简单类名，value为对应servnet类的全限定性类名
  private volatile Map<String, String> nameToClassNameMap = new HashMap<>();

  public TomcatHandler(Map<String, Servnet> nameToServnetMap,
      Map<String, String> nameToClassNameMap) {
    this.nameToClassNameMap = nameToClassNameMap;
    this.nameToServnetMap = nameToServnetMap;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof HttpRequest) {
      HttpRequest request = (HttpRequest) msg;
      //从请求中解析出要访问的Servnet名称
      String servnetName = request.uri().split("/")[1];

      Servnet servnet = new DefaultServnet();
      if (nameToServnetMap.containsKey(servnetName)) {
        servnet = nameToServnetMap.get(servnetName);
      } else if (nameToClassNameMap.containsKey(servnetName)) {
        //doule-check 双重检测锁
        if (null == nameToServnetMap.get(servnetName)) {
          synchronized (this) {
            if (null == nameToServnetMap.get(servnetName)) {
              String className = nameToClassNameMap.get(servnetName);
              //反射
              servnet = (Servnet) Class.forName(className).newInstance();
              nameToServnetMap.put(servnetName, servnet);
            }
          }
        }
      }

      //运行至此sevnet不为空
      NettyRequest req = new DefaultNettyRequest(request);
      NettyResponse res = new DefaultNettyResponse(request, ctx);
      //根据不同请求方式处理
      if (request.method().name().equalsIgnoreCase("GET")) {
        servnet.doGet(req, res);
      } else if (request.method().name().equalsIgnoreCase("POST")) {
        servnet.doPost(req, res);
      }
      ctx.close();
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
