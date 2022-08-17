package get_working_proxies.finder.component;

import get_working_proxies.modal.ProxyAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class ProxyFinderWebPage {
  protected abstract Pattern getRegexpPattern();

  protected abstract String getURL();

  protected abstract String getXPathContainingProxyInfo();

  public List<ProxyAddress> getProxyAddresses(WebDriver driver) {
    List<ProxyAddress> results = new ArrayList<ProxyAddress>();

    driver.get(getURL());

    this.prepareElements();

    List<WebElement> webElements = driver.findElements(By.xpath(getXPathContainingProxyInfo()));

    if (!webElements.isEmpty()) {
      Pattern regExp = getRegexpPattern();

      for (WebElement element : webElements) {
        Matcher matcher = regExp.matcher(element.getText());

        if (matcher.matches()) {
          String ipAddress = matcher.replaceAll("$1");
          int port = Integer.valueOf(matcher.replaceAll("$2"));

          results.add(new ProxyAddress(ipAddress, port));
        }
      }
    }

    return results;
  }

  protected void waitForLoad(WebDriver driver) {
    ExpectedCondition<Boolean> pageLoadCondition =
        new ExpectedCondition<Boolean>() {
          public Boolean apply(WebDriver driver) {
            return ((JavascriptExecutor) driver)
                .executeScript("return document.readyState")
                .equals("complete");
          }
        };
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
    wait.until(pageLoadCondition);
  }

  // Optionally perform extra steps right before we locate elements containing proxy information.
  // For example, click a button to ensure the elements are attached to the current document
  protected void prepareElements() {}
}
