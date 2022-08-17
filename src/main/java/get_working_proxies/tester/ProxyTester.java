package get_working_proxies.tester;

import com.google.common.collect.Iterables;
import get_working_proxies.modal.ProxyAddress;
import get_working_proxies.tester.modal.ProxyConnectionAttemptHandler;
import get_working_proxies.tester.modal.ProxyFailedConnectionInfo;
import get_working_proxies.tester.modal.ProxySuccessfulConnectionInfo;
import get_working_proxies.tester.modal.ProxyTestingPage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class ProxyTester {
  private static final String ID_FOR_JAVASCRIPT_DISABLED = "noscriptmsg";

  private static final String PROXY_GENERAL_CONNECTION_ERROR = "Could not connect to proxy";
  private static final String PROXY_ONLY_ALLOWS_SPECIFIC_URLS =
      "Proxy will not work for our purposes";
  private static final String PROXY_DID_NOT_HIDE_IP_ERROR =
      "Proxy was a low level proxy (Does not hide real ip)";

  @SuppressWarnings("unused")
  private static final String ELEMENT_ID_OF_BROWSER_INFO = "browserInfo";

  public static ProxyConnectionAttemptHandler testAListOfProxies(
      List<ProxyAddress> proxyAddresses, int numberOfThreadsToRun, String actualIpAddress)
      throws Exception {
    ProxyConnectionAttemptHandler handler = new ProxyConnectionAttemptHandler();
    int sizeOfEachThread = proxyAddresses.size();
    List<Thread> threads = new ArrayList<Thread>();

    if (numberOfThreadsToRun != 0) {
      sizeOfEachThread = (int) Math.floor(proxyAddresses.size() / numberOfThreadsToRun);

      // If we're trying to get more threads then we have addresses
      if (sizeOfEachThread == 0) {
        sizeOfEachThread = proxyAddresses.size();
      }
    }

    for (List<ProxyAddress> proxyAddressList :
        Iterables.partition(proxyAddresses, sizeOfEachThread)) {
      threads.add(
          new Thread() {
            @Override
            public void run() {
              try {
                testAListOfProxies(proxyAddressList, handler, actualIpAddress);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          });
    }

    for (Thread t : threads) {
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }

    return handler;
  }

  public static void testAListOfProxies(
      List<ProxyAddress> proxyAddresses,
      ProxyConnectionAttemptHandler handler,
      String actualIpAddress)
      throws IOException {
    for (ProxyAddress address : proxyAddresses) {
      testAProxy(address, handler, actualIpAddress);
    }
  }

  public static void testAProxy(
      ProxyAddress proxy, ProxyConnectionAttemptHandler handler, String actualIpAddress)
      throws IOException {
    boolean closeBrowserAfterWork = true;
    Long startTime = System.currentTimeMillis();
    Long endTime = (Long) null;
    boolean javaSriptEnabled = false;
    closeBrowserAfterWork = false;
    String errorMessage = null;

    FirefoxOptions options = new FirefoxOptions();
    Proxy ffProxy = new Proxy();
    ffProxy.setHttpProxy(proxy.getIpAddress() + ":" + proxy.getPort());
    options.setProxy(ffProxy);

    WebDriver driver = new FirefoxDriver(options);

    try {
      System.out.println(
          "Attempting to get address: " + ProxyTestingPage.URL_CONTAINING_CLIENT_INFO);
      driver.get(ProxyTestingPage.URL_CONTAINING_CLIENT_INFO);
    } catch (WebDriverException e) {
      errorMessage = PROXY_GENERAL_CONNECTION_ERROR;
    }

    if (errorMessage != null) {
      // The URL may be different if we're on a proxy which doesn't allow directing anywhere.
      if (!driver.getCurrentUrl().contains(ProxyTestingPage.URL_CONTAINING_CLIENT_INFO)) {
        errorMessage = PROXY_ONLY_ALLOWS_SPECIFIC_URLS;
      } else if (driver.findElements(By.id(ProxyTestingPage.ELEMENT_ID_OF_IP_ADDRESS)).isEmpty()) {
        errorMessage = PROXY_GENERAL_CONNECTION_ERROR;
      } else {
        WebElement webElementContainingIpAddress =
            driver.findElement(By.id(ProxyTestingPage.ELEMENT_ID_OF_IP_ADDRESS));

        // If we can't connect to the proxy server.
        if (webElementContainingIpAddress == null) {
          errorMessage = PROXY_GENERAL_CONNECTION_ERROR;
        }
        // If the proxy is not hiding our real ip address
        else if (actualIpAddress != null
            && webElementContainingIpAddress.getText().contains(actualIpAddress)) {
          errorMessage = PROXY_DID_NOT_HIDE_IP_ERROR;
        } else {
          javaSriptEnabled =
              driver.findElements(By.id(ID_FOR_JAVASCRIPT_DISABLED)).size() == 0
                  || !driver.findElement(By.id(ID_FOR_JAVASCRIPT_DISABLED)).isDisplayed();
        }
      }
    }

    if (errorMessage == null) {
      endTime = System.currentTimeMillis() - startTime;

      handler.addSuccessfulProxy(
          new ProxySuccessfulConnectionInfo(javaSriptEnabled, true, endTime, proxy));

      System.out.println("Proxy works!: " + proxy);
      System.out.println("took: " + endTime);
    } else {
      System.out.println("Proxy does NOT work: " + proxy);

      handler.addFailedProxy(new ProxyFailedConnectionInfo(proxy, errorMessage));
    }

    if (closeBrowserAfterWork) {
      driver.close();
    }
  }
}
