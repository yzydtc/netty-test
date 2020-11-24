package tomcat;

public class TomcatStarter{

  public static void main(String[] args) {
    TomcatServer tomcatServer = new TomcatServer("webapp");
    tomcatServer.start();
  }
}
