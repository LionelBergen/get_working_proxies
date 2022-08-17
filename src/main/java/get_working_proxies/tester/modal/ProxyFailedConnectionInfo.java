package get_working_proxies.tester.modal;

import get_working_proxies.modal.ProxyAddress;
import java.util.Date;

public class ProxyFailedConnectionInfo {
  private ProxyAddress proxyAddress;
  private String errorMessage;
  private Date dateTested;

  public ProxyFailedConnectionInfo() {}

  public ProxyFailedConnectionInfo(ProxyAddress proxyAddress, String errorMessage) {
    this.proxyAddress = proxyAddress;
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ProxyAddress getProxyAddress() {
    return proxyAddress;
  }

  public void setProxyAddress(ProxyAddress proxyAddress) {
    this.proxyAddress = proxyAddress;
  }

  public Date getDateTested() {
    return dateTested;
  }

  public void setDateTested(Date dateTested) {
    this.dateTested = dateTested;
  }
}
