package org.andrea.jockey.app;


import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
public class SeleniumConfig {

    private static ChromeDriver driver;

    public SeleniumConfig() {
        if(driver ==null) {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();

            ChromeDriverService service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File("/Users/apple/Workspace/intelliJ/jockey/src/main/resources/chromedriver"))
                    .usingAnyFreePort()
                    .build();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.merge(capabilities);
            driver = new ChromeDriver(service, options);

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        }
    }
    public ChromeDriver getDriver(){
        return driver;
    }
}
