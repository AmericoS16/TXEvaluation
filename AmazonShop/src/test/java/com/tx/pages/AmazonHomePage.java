package com.tx.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonHomePage {
	public WebDriver driver;
	private By by_SearcherTextBox = By.id("twotabsearchtextbox");
	private By by_SearchButton = By.id("nav-search-submit-button");
	private By by_FirstElementFromSearch = By.xpath("//div[@data-cel-widget='search_result_1']/div/div/div/div/span");

	// First occurrence
	private By by_QuantitySelect = By.name("quantity");
	private By by_AddToCarButton = By.id("add-to-cart-button");

	// Cart
	private By by_cartCount = By.id("nav-cart-count");

	public AmazonHomePage(WebDriver driver) {
		this.driver = driver;
	}

	public void searchProduct(String product) {
		WebElement searchTextBox = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_SearcherTextBox));
		searchTextBox.clear();
		searchTextBox.sendKeys(product);
		WebElement searchButton = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_SearchButton));
		searchButton.click();

	}

	public void selectFirstElement() {
		WebElement firstelement = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_FirstElementFromSearch));
		firstelement.click();

	}

	public int setQuantity() {
		WebElement quantity = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_QuantitySelect));
		Select select = new Select(quantity);
		int valueForSetUp = (int) Math.floor(Math.random() * 10 + 1);
		select.selectByIndex(valueForSetUp);
		return valueForSetUp;

	}

	public void addProductToCar() {
		WebElement addButton = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_AddToCarButton));
		addButton.click();

	}

	public String getCartCount() {
		WebElement cartCount = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(by_cartCount));
		
		return cartCount.getText();
	}
}
