package get_working_proxies.finder.component;

import java.util.regex.Pattern;

public class FreeProxyLists extends ProxyFinderWebPage {
  @Override
  protected Pattern getRegexpPattern() {
    // 66.222.130.164 3128 CA Canada anonymous no yes 5 secs ago
    return Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+) ([0-9]+).*", Pattern.DOTALL);
  }

  @Override
  protected String getURL() {
    return "https://www.freeproxylists.net";
  }

  @Override
  protected String getXPathContainingProxyInfo() {
    return "//*[contains(@class, 'DataGrid')]/tbody/tr";
  }
}
