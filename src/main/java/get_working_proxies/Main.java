package get_working_proxies;

import get_working_proxies.finder.component.FreeProxyLists;
import get_working_proxies.finder.component.ProxyFinderWebPage;
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

    List<ProxySuccessfulConnectionInfo> workingProxiesSorted =
        results.getSortedWorkingProxiesBySpeed();

    System.out.println(
        "Fasted proxy: "
            + workingProxiesSorted.get(0).getProxyAddress()
            + " with a speed of: "
            + workingProxiesSorted.get(0).getTimeToConnect());
    System.out.println(
        "Slowest proxy: "
            + workingProxiesSorted.get(workingProxiesSorted.size() - 1).getProxyAddress()
            + " with a speed of: "
            + workingProxiesSorted.get(workingProxiesSorted.size() - 1).getTimeToConnect());

    for (ProxySuccessfulConnectionInfo workingProxy : workingProxiesSorted) {
      System.out.println(workingProxy.getProxyAddress().toString());
    }
  }

  public static List<ProxyAddress> getListOfProxies(WebDriver driver) throws Exception {
    List<ProxyFinderWebPage> listOfProxyPages = new ArrayList<ProxyFinderWebPage>();
    // use a hashset to ensure no duplicates
    HashSet<ProxyAddress> uniqueProxiesFound = new HashSet<ProxyAddress>();

    List<Thread> threads = new ArrayList<Thread>();

    // listOfProxyPages.add(new ProxyNovaPage80());
    // listOfProxyPages.add(new ProxyNovaPage8080());
    // listOfProxyPages.add(new ProxyNovaPage3128());
    // listOfProxyPages.add(new FreeProxyList());
    listOfProxyPages.add(new FreeProxyLists());

    synchronized (uniqueProxiesFound) {
      for (ProxyFinderWebPage page : listOfProxyPages) {
        threads.add(
            new Thread() {
              public void run() {
                List<ProxyAddress> results = page.getProxyAddresses(driver);
                uniqueProxiesFound.addAll(results);
              }
            });
      }
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
