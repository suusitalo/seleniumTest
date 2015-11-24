package com.sami.seleniumTest;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class webTest {

	private static ChromeDriverService service;
	private WebDriver driver;
	
	/*************************************************************************************
	* Starting chromedriver as a service. This saves a lot of time in larger test suites *
	**************************************************************************************/
	@BeforeClass
	public static void createAndStartService() {
		service = new ChromeDriverService.Builder()
	        .usingDriverExecutable(new File("C://Users//Sami//Downloads//chromedriver//chromedriver.exe"))
	        .usingAnyFreePort()
	        .build();
	    try {
			service.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void createAndStopService() {
		service.stop();
	}

	@Before
	public void createDriver() {
		driver = new RemoteWebDriver(service.getUrl(),
	    DesiredCapabilities.chrome());
	}

	@After
	public void quitDriver() {
		driver.quit();
	}
	
	/****************************************************************************************
	* Method for ensuring that page has fully loaded before trying to search items from it  *
	*****************************************************************************************/
	protected WebElement waitForElementVisible(By by) {
	    WebDriverWait wait = new WebDriverWait(driver,6);
	    WebElement element = null;

	    element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	    return element;
	}
	
	
	@Test
	public void startWebDriver(){
			
		driver.get("http://amazon.com");
		//Ensure you are on right web page
		Assert.assertTrue("That is not Amazon.com web page", 
				driver.getTitle().startsWith("Amazon.com: Online Shopping for Electronics"));
		
		//Enter text to be searched
		waitForElementVisible(By.id("twotabsearchtextbox"));//wait for element to become visible
		WebElement textbox = driver.findElement(By.id("twotabsearchtextbox"));
		textbox.sendKeys("Nikon");
		
		//press search button
		WebElement button = driver.findElement(By.className("nav-search-submit"));
		button.click();
		
		//Change sorting to high-to-low
		waitForElementVisible(By.id("sort"));//wait for element to become visible
		Select dropdown = new Select(driver.findElement(By.id("sort")));
		dropdown.selectByVisibleText("Price: High to Low");
		
		//Find second item from the list
		waitForElementVisible(By.id("result_1"));//wait for element to become visible
		WebElement secondItem = driver.findElement(By.id("result_1"));
		secondItem.click();
		
		//Check that product description title contains text "Nikon D3X"
		waitForElementVisible(By.id("productTitle"));//wait for element to become visible
		WebElement itemDescriptionText = driver.findElement(By.id("productTitle"));
		String descriptionString = itemDescriptionText.getText();
		Assert.assertTrue("Item description did not contain 'Nikon D3X'", 
				descriptionString.contains("Nikon D3X"));
		
	}
}
