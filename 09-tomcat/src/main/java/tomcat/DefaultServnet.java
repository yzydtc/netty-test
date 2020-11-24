package tomcat;

import sernet.NettyRequest;
import sernet.NettyResponse;
import sernet.Servnet;

/**
 * Tomcat中对Servnet规范的默认实现
 */
public class DefaultServnet extends Servnet {

  @Override
  public void doGet(NettyRequest request, NettyResponse response) throws Exception {
    String servnetName = request.getUri().split("/")[1];
    String content = "404 - no this Servnet:" + servnetName;
    response.write(content);
  }

  @Override
  public void doPost(NettyRequest request, NettyResponse response) throws Exception {
    doGet(request,response);
  }
}
