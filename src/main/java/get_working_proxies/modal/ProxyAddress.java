package get_working_proxies.modal;

public class ProxyAddress {
  private String ipAddress;
  private int port;

  public ProxyAddress(String ipAddress, int port) {
    this.ipAddress = ipAddress;
    this.port = port;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
