package get_working_proxies;

import get_working_proxies.finder.component.ProxyFinderWebPage;
import get_working_proxies.finder.component.ProxyNovaPage3128;
import get_working_proxies.finder.component.ProxyNovaPage80;
import get_working_proxies.finder.component.ProxyNovaPage8080;
import get_working_proxies.modal.ProxyAddress;
import get_working_proxies.tester.ProxyTester;
import get_working_proxies.tester.modal.ProxyConnectionAttemptHandler;
import get_working_proxies.tester.modal.ProxySuccessfulConnectionInfo;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
  private static final String ipAddrEnvironmentVariable = "MY_IP";

  public static void main(String[] args) throws Exception {
    System.out.println("Starting program");

    String actualIpAddress = System.getenv(ipAddrEnvironmentVariable);
    System.out.println("ip address to hide (optional): " + actualIpAddress);

    WebDriverManager.firefoxdriver().setup();
    WebDriver driver = new FirefoxDriver();

    List<ProxyAddress> listOfProxies = getListOfProxies(driver);

    System.out.println("Results");
    System.out.println("-------");
    System.out.println(String.format("Number of proxies found:   %s", listOfProxies.size()));

    driver.close();

    System.out.println("Going to test proxies next...");

    ProxyConnectionAttemptHandler results =
        ProxyTester.testAListOfProxies(
            listOfProxies.stream().collect(Collectors.toList()), 10, actualIpAddress);

    for (ProxySuccessfulConnectionInfo workingProxy : results.getWorkingProxiesList()) {
      System.out.println(workingProxy.getProxyAddress().toString());
    }
  }

  public static List<ProxyAddress> getListOfProxies(WebDriver driver) throws Exception {
    List<ProxyFinderWebPage> listOfProxyPages = new ArrayList<ProxyFinderWebPage>();
    // use a hashset to ensure no duplicates
    HashSet<ProxyAddress> uniqueProxiesFound = new HashSet<ProxyAddress>();

    List<Thread> threads = new ArrayList<Thread>();

    listOfProxyPages.add(new ProxyNovaPage80());
    listOfProxyPages.add(new ProxyNovaPage8080());
    listOfProxyPages.add(new ProxyNovaPage3128());

    for (ProxyFinderWebPage page : listOfProxyPages) {
      threads.add(
          new Thread() {
            public void run() {
              uniqueProxiesFound.addAll(page.getProxyAddresses(driver));
            }
          });
    }

    for (Thread t : threads) {
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }

    return new ArrayList<ProxyAddress>(uniqueProxiesFound);
  }
}
