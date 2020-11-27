package tomcat;

public class TomcatStarter{

  public static void main(String[] args) throws Exception {
    TomcatServer tomcatServer = new TomcatServer("webapp");
    tomcatServer.start();
  }
}
