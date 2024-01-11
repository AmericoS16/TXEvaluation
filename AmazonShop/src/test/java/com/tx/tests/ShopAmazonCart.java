package com.tx.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.tx.base.TestBase;
import com.tx.pages.AmazonHomePage;

public class ShopAmazonCart extends TestBase {

	private AmazonHomePage homePage;

	// Log4j configuration
	private static final Logger log = LogManager.getLogger(ShopAmazonCart.class);

	@BeforeTest
	void setupTest() {

		homePage = new AmazonHomePage(driver);

	}

	@Test(enabled = true)
	void Verify_quantity_on_cart() {
		String methodName = new Exception().getStackTrace()[0].getMethodName();
		test = extent.createTest(methodName,
				"Verify that the quantity of items on the cart icon is being correctly updated");
		test.log(Status.INFO, "Starting");
		test.assignCategory("Shop car");
		
		log.info("Looking for a product");
		homePage.searchProduct("Boardgame");
		homePage.selectFirstElement();
		int quantity = homePage.setQuantity();
		log.info("Quantity was setup as: "+quantity);
		homePage.addProductToCar();
		Assert.assertEquals(homePage.getCartCount(), String.valueOf(quantity));

	}

}
