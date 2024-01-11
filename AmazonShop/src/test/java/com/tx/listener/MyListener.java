package com.tx.listener;


import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.tx.base.TestBase;
import java.io.IOException;

import static com.tx.base.TestBase.CaptureScreenshot;

public class MyListener implements ITestListener {
	public void onTestStart(ITestResult iTestResult) {

	}

	public void onTestSuccess(ITestResult iTestResult) {
		TestBase.test.log(Status.PASS,
				MarkupHelper.createLabel(iTestResult.getName().toUpperCase() + " PASS", ExtentColor.GREEN));

	}

	public void onTestFailure(ITestResult iTestResult) {
		TestBase.test.log(Status.FAIL, iTestResult.getThrowable().getMessage());
		TestBase.test.log(Status.FAIL,
				MarkupHelper.createLabel(iTestResult.getName().toUpperCase() + " FAIL", ExtentColor.RED));

		try {
			TestBase.test.addScreenCaptureFromPath(CaptureScreenshot(iTestResult));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult iTestResult) {
		TestBase.test.log(Status.SKIP,
				MarkupHelper.createLabel(iTestResult.getName().toUpperCase() + " SKIPPED", ExtentColor.PURPLE));

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

	}

	public void onStart(ITestContext iTestContext) {

	}

	public void onFinish(ITestContext iTestContext) {

	}

}
