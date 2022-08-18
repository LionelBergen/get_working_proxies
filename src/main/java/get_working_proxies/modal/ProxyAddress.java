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

  @Override
  public String toString() {
    return this.ipAddress + ":" + this.port;
  }

  @Override
  public boolean equals(Object proxyAddress) {
    if (proxyAddress == null || proxyAddress.getClass() != this.getClass()) {
      return false;
    }

    return this.ipAddress.equals(((ProxyAddress) proxyAddress).getIpAddress());
  }

  @Override
  public int hashCode() {
    return ipAddress.hashCode();
  }
}
