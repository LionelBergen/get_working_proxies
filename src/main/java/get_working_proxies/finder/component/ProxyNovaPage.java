package get_working_proxies.finder.component;

import java.util.regex.Pattern;

public abstract class ProxyNovaPage extends ProxyFinderWebPage {
  @Override
  protected Pattern getRegexpPattern() {
    return Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+) ([0-9]+).*", Pattern.DOTALL);
  }

  @Override
  protected String getXPathContainingProxyInfo() {
    return "//*[@id='tbl_proxy_list']/tbody/tr";
  }
}
