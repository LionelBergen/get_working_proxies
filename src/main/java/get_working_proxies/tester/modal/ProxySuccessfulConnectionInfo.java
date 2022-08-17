package get_working_proxies.tester.modal;

import get_working_proxies.modal.ProxyAddress;
import java.util.Date;

public class ProxySuccessfulConnectionInfo {
  private boolean javascriptWorks;
  private boolean httpsEnabled;
  private long timeToConnect;
  private Date dateTested;
  private ProxyAddress proxyAddress;

  public ProxySuccessfulConnectionInfo() {}

  public ProxySuccessfulConnectionInfo(
      boolean javascriptWorks,
      boolean httpsEnabled,
      long timeToConnect,
      ProxyAddress proxyAddress) {
    this.setJavascriptWorks(javascriptWorks);
    this.setHttpsEnabled(httpsEnabled);
    this.setTimeToConnect(timeToConnect);
    this.setProxyAddress(proxyAddress);
  }

  public boolean isJavascriptWorks() {
    return javascriptWorks;
  }

  public void setJavascriptWorks(boolean javascriptWorks) {
    this.javascriptWorks = javascriptWorks;
  }

  public boolean isHttpsEnabled() {
    return httpsEnabled;
  }

  public void setHttpsEnabled(boolean httpsEnabled) {
    this.httpsEnabled = httpsEnabled;
  }

  public long getTimeToConnect() {
    return timeToConnect;
  }

  public void setTimeToConnect(long timeToConnect) {
    this.timeToConnect = timeToConnect;
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

  @Override
  public String toString() {
    if (this.timeToConnect > 0) {
      return String.format(
          "Does javascript work?: %s %n httpEnabled?: %s %n Time it took: %s %n Date of test: %s",
          this.javascriptWorks, this.isHttpsEnabled(), this.timeToConnect, this.dateTested);
    } else {
      return "Proxy failed: " + this.proxyAddress;
    }
  }
}
