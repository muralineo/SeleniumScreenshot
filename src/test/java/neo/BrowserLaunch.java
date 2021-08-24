package neo;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.LinkedList;
import java.util.List;


public class BrowserLaunch {


//    public static WebDriver driver;
    public static WebDriver driver;

    public static void openBrowser(BrowserName name,
                                   boolean isIncognito){

        if (name == BrowserName.CHROME && isIncognito){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("incognito");
            driver = new ChromeDriver(options);
        } else if (name == BrowserName.CHROME) {
            driver = new ChromeDriver();
        } else if (name == BrowserName.FIREFOX){
            driver = new FirefoxDriver();
        } else if (name == BrowserName.IE){
            driver = new InternetExplorerDriver();
        } else if (name == BrowserName.EDGE) {
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
    }

    public static void navigationMethods(List<String> urlList,
                        BrowserName name, boolean isIncognito){
        // Opening browser
        openBrowser(name, isIncognito);
        for (int i = 0; i < urlList.size(); i++) {
            if (i == 0) {
                // First url in the list will be opened using get() method
                driver.get("https://" + urlList.get(i) + ".com");
            } else {
                // All other urls will be opened using navigate().to() method
                driver.navigate().to("https://" + urlList.get(i) + ".com");
            }
            System.out.println("The title of " +
                    driver.getCurrentUrl() + ": \n" +driver.getTitle());
        }
        driver.navigate().back();
        driver.navigate().forward();
        driver.navigate().refresh();
        driver.quit();
    }

    public static void windowMethods(String url, BrowserName name,
                                     int windowWidth, int windowHeight){
        openBrowser(name, false);
        driver.get(url);
        Dimension currentSize = driver.manage().window().getSize();
        System.out.println("The Width of current window: " + currentSize.getWidth());
        System.out.println("The Height of current window: " + currentSize.getHeight());
        int newWidth = currentSize.getWidth() - windowWidth;
        int newHeight = currentSize.getHeight() - windowHeight;
        Dimension newSize = new Dimension(newWidth, newHeight);
        driver.manage().window().setSize(newSize);
        System.out.println("The Width of new window: " + newSize.getWidth());
        System.out.println("The Height of new window: " + newSize.getHeight());
        driver.close();
    }

    public static void main(String[] args)  {

        List<String> urlList = new LinkedList<>();
        urlList.add("google");
        urlList.add("facebook");
        urlList.add("youtube");
        urlList.add("netflix");

//        navigationMethods(urlList, BrowserName.CHROME, true);
//        windowMethods("https://netflix.com", BrowserName.EDGE,  200, 300 );
        driver = new ChromeDriver();
        driver.get("https://facebook.com");

        TakesScreenshot screenshot = driver.findElement(By.name("email"));







    }
}
