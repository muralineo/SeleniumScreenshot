package neo;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class ScreenshotTest {

    public static WebDriver driver;

    /**
     * It forms the URL. Page name is enough.
     * No need to give full url.
     */
    public static String urlBuilder(String webpage)  {
        return  "https://" + webpage + ".com";
    }

    /**
     * It invokes different browser.
     * @param name is browser name of our choice.
     * @param pageName is name of webpage.
     */
    public static void invokeBrowser(BrowserName name, String pageName) {

        if (name == BrowserName.CHROME) {
            driver = new ChromeDriver();
        } else if (name == BrowserName.FIREFOX){
            driver = new FirefoxDriver();
        } else if (name == BrowserName.IE) {
            driver = new InternetExplorerDriver();
        } else if (name == BrowserName.EDGE){
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(urlBuilder(pageName));
    }

    /**
     * It will get usernames and passwords add them into a Map
     * @return Map
     */
    public static Map<String, String> userCredentials(){

        Scanner scan = new Scanner(System.in);
        String continued = "c";

        List<String> usernames = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        while (continued.equalsIgnoreCase("c")  ){
            System.out.println("Enter username: ");
            String id = scan.nextLine();
            System.out.println("Enter password: ");
            String pass = scan.nextLine();
            System.out.println("Press 'c' to add username and password or Press any key to quit: ");
            continued = scan.nextLine();
            usernames.add(id);
            passwords.add(pass);
        }

        Map<String, String> credentials = new HashMap<>();
        for (int i = 0; i < usernames.size(); i++) {
            credentials.put("user_" + i , usernames.get(i));
            credentials.put("password_" + i, passwords.get(i));
        }
        return credentials;
    }

    /**
     * It's an utility method to take screenshot
     * @param driver
     * @throws IOException
     */
    public static void takeScreenshot(WebDriver driver) throws IOException {
        Date date = new Date();
        //FileName will be the time stamp. So that the screenshot will not get overridden
        String fileName = date.toString().replace(":", "_")
                .replace(" ", "_") + ".jpg";
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File source = screenshot.getScreenshotAs(OutputType.FILE);
        File destination = new File(".//Screenshots//" + fileName);
        FileHandler.copy(source, destination);
    }

    /**
     * It checks the login page by passing username and password.
     * If it fails to log in, a screenshot will be captured with timestamp.
     * @throws IOException
     */
    public static void loginTestNetflix(Map<String, String> credentials) throws IOException {
        for (int i = 0; i < credentials.size()/2 ; i++) {

            invokeBrowser(BrowserName.CHROME, "netflix");
            driver.findElement(By.xpath("//a[@class='authLinks redButton']")).click();

            WebElement userId = driver.findElement(By.xpath("//input[contains(@id,'id_userLoginId')]"));
            userId.sendKeys(credentials.get("user_" + i));
            WebElement pass = driver.findElement(By.xpath("//input[@name='password']"));
            pass.sendKeys(credentials.get("password_" + i));
            takeScreenshot(driver);
            driver.findElement(By.xpath("//button[text()='Sign In']")).click();
            try {
                driver.findElement(By.xpath("//ul/li[4]/div/a/div/div"));
                System.out.println("Login was successful");
            } catch (Exception e) {
                System.out.println("Wrong credentials");
            }
            finally {
                takeScreenshot(driver);
                driver.close();
            }
        }
    }

    public static void loginTestImdb(Map<String, String> credentials) throws IOException {

        for (int i = 0; i < credentials.size()/2; i++) {

            invokeBrowser(BrowserName.CHROME, "imdb");
            driver.findElement(By.xpath("//*[text()='Sign In']")).click();
            driver.findElement(By.xpath("//span[contains(text(), 'Sign in with IMDb')]")).click();


            WebElement userId = driver.findElement(By.xpath("//input[@type='email']"));
            userId.sendKeys(credentials.get("user_" + i));
            WebElement pass = driver.findElement(By.xpath("//input[@name='password']"));
            pass.sendKeys(credentials.get("password_" + i));

            takeScreenshot(driver);
            driver.findElement(By.xpath("//input[@type='submit']")).click();

            try {
                driver.findElement(By.xpath("(//div[text()='Watchlist'])[1]"));
                System.out.println("Login was successful");
            } catch (Exception e) {
                System.out.println("Wrong credentials");
            }
            finally {
                takeScreenshot(driver);
                driver.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        loginTestNetflix(userCredentials());
        loginTestImdb(userCredentials());

    }

}
