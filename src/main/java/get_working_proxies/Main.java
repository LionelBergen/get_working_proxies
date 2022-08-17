package get_working_proxies;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
  public static void main(String[] args) {
    System.out.println("Starting program");

    WebDriverManager.firefoxdriver().setup();
    WebDriver driver = new FirefoxDriver();
    driver.get("http://www.google.com");

    System.out.println("Finished program");
  }
}
