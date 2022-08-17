package get_working_proxies.tester.modal;

import java.util.ArrayList;
import java.util.List;

public class ProxyConnectionAttemptHandler {
  private List<ProxySuccessfulConnectionInfo> workingProxiesList;
  private List<ProxyFailedConnectionInfo> failedProxiesList;

  public ProxyConnectionAttemptHandler() {
    this.workingProxiesList = new ArrayList<ProxySuccessfulConnectionInfo>();
    this.failedProxiesList = new ArrayList<ProxyFailedConnectionInfo>();
  }

  public void addSuccessfulProxy(ProxySuccessfulConnectionInfo info) {
    this.workingProxiesList.add(info);
  }

  public void addFailedProxy(ProxyFailedConnectionInfo info) {
    this.failedProxiesList.add(info);
  }

  public List<ProxySuccessfulConnectionInfo> getWorkingProxiesList() {
    return workingProxiesList;
  }

  public List<ProxyFailedConnectionInfo> getFailedProxiesList() {
    return failedProxiesList;
  }
}
