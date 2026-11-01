package practical6;

import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementInteraction {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.getBoolean("headless")) options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get(Path.of("web", "elements.html").toUri().toString());
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            driver.findElement(By.id("name")).sendKeys("Sonam");
            driver.findElement(By.id("female")).click();
            driver.findElement(By.id("sports")).click();
            Select colors = new Select(driver.findElement(By.id("color")));
            colors.selectByVisibleText("Purple");
            if (!"Sonam".equals(driver.findElement(By.id("name")).getDomProperty("value"))
                    || !driver.findElement(By.id("female")).isSelected()
                    || !driver.findElement(By.id("sports")).isSelected()
                    || !colors.getFirstSelectedOption().getText().equals("Purple")) {
                throw new IllegalStateException("Element values did not match");
            }
            // Keep implicit and explicit waits separate to avoid compounded timeouts.
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.findElement(By.id("alert")).click();
            if (!wait.until(ExpectedConditions.alertIsPresent()).getText().equals("Hello Sonam")) {
                throw new IllegalStateException("Unexpected alert");
            }
            driver.switchTo().alert().accept();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("notesFrame"));
            driver.findElement(By.id("notes")).sendKeys("STQA frame test");
            if (!"STQA frame test".equals(driver.findElement(By.id("notes")).getDomProperty("value"))) {
                throw new IllegalStateException("Frame text did not match");
            }
            driver.switchTo().defaultContent();
            driver.findElement(By.id("load")).click();
            wait.until(ExpectedConditions.textToBe(By.id("result"), "Ready"));
            System.out.println("PASS - textbox, radio, checkbox, dropdown, alert and frame");
            System.out.println("PASS - implicit wait and delayed result with explicit wait");
        } finally {
            driver.quit();
        }
    }
}
