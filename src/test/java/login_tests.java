import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class login_tests {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static Properties properties;

    @BeforeClass
    public static void setUp() throws IOException {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("credentials.properties")) {
            properties.load(fis);
        }
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            captureScreenshot(description.getMethodName());
        }
    };

    private void captureScreenshot(String screenshotName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File destFile = new File("./screenshots/" + screenshotName + ".png");
            FileUtils.copyFile(screenshot, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test case 1: Login with wrong username and correct password
    @Test
    public void testLoginWithWrongUsername() {
        driver.get("https://github.com/login?return_to=https%3A%2F%2Fgithub.com%2Ftopics%2Flogin");
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_field")));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login\"]/div[4]/form/div/input[13]"));

        username.clear();
        username.sendKeys(properties.getProperty("username.incorrect"));
        password.clear();
        password.sendKeys(properties.getProperty("password.correct"));
        loginButton.click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flash-error")));
        assertTrue(errorMessage.isDisplayed());
    }

    // Test case 2: Login with correct username and wrong password
    @Test
    public void testLoginWithWrongPassword() {
        driver.get("https://github.com/login?return_to=https%3A%2F%2Fgithub.com%2Ftopics%2Flogin");
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_field")));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login\"]/div[4]/form/div/input[13]"));

        username.clear();
        username.sendKeys(properties.getProperty("username.correct"));
        password.clear();
        password.sendKeys(properties.getProperty("password.incorrect"));
        loginButton.click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flash-error")));
        assertTrue(errorMessage.isDisplayed());
    }

    // Test case 3: Login with correct username and correct password
    @Test
    public void testLoginWithCorrectCredentials() {
        driver.get("https://github.com/login?return_to=https%3A%2F%2Fgithub.com%2Ftopics%2Flogin");
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login_field")));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login\"]/div[4]/form/div/input[13]"));

        username.clear();
        username.sendKeys(properties.getProperty("username.correct"));
        password.clear();
        password.sendKeys(properties.getProperty("password.correct"));
        loginButton.click();

        assertTrue(driver.getTitle().contains("GitHub"));
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}