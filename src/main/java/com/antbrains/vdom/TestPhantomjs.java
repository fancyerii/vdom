package com.antbrains.vdom;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class TestPhantomjs {

	public static void main(String[] args) {
		DesiredCapabilities dCaps = new DesiredCapabilities();
		dCaps.setJavascriptEnabled(true);

		dCaps.setCapability("takesScreenshot", true);
		PhantomJSDriverService service = new PhantomJSDriverService.Builder()
				.usingPhantomJSExecutable(
						new File("./phantomjs-1.9.7-linux-x86_64/bin/phantomjs"))
				.usingGhostDriverCommandLineArguments(new String[] {})
				// .usingCommandLineArguments(new
				//String[]{"--load-images=false"})
				//.usingCommandLineArguments(new String[]{"--debug=true"})
				.usingAnyFreePort().build();
		// dCaps.setCapability("phantomjs.page.customHeaders.User-Agent", "");
		PhantomJSDriver driver = new PhantomJSDriver(service, dCaps);
		try {
			driver.manage().timeouts()
					.implicitlyWait(5000, TimeUnit.MILLISECONDS);
			driver.manage().timeouts()
					.pageLoadTimeout(5000, TimeUnit.MILLISECONDS);
			driver.manage().timeouts()
					.setScriptTimeout(5000, TimeUnit.MILLISECONDS);
//			driver.setLogLevel(Level.FINE);
			// String js="var page = this;" +
			// "page.onResourceRequested = function(requestData, request) {"+
			// "if (requestData['Content-Type'] == 'image/gif') {"+
			// "console.log('The url of the request is matching. Aborting: ' + requestData['url']);"+
			// "request.abort();"+
			// "}";
			// Object result = driver.executePhantomJS(js);
			// System.out.println(result);
//			String js = "var page=this;var fn=arguments[0];\n"
//					+ "var fs = require('fs');\n"
//					+ "var f=fs.open(fn, 'w');\n"
//					+ "console.log(f);\n"
//					+ "console.log('hi');\n"
//					+ "page.onResourceReceived = function (response) {"
//						+ "console.log('status: '+response.status);"
//						+ "console.log('url: '+response.url);"
//						+ "f.write(response.status);"
//						+ "f.write(response.url);"
//						+ "f.flush();"
//						+ "var row=[];"
////						+ "document.myRsp.push(row);"
//						+ "row.push(response.id);"
//						+ "row.push(response.url);"
//						+ "row.push(response.time);"
//						+ "row.push(response.headers);"
//						+ "row.push(response.contentType);"
//						+ "row.push(response.status);"
//						+ "console.log(row);"
//
//					+ "};"
//					;
//			driver.executePhantomJS(js,"/home/lili/phantomjs2.txt");
			driver.get("http://www.sogou.com");
//			String js2 = "return document.numbers[0];";

//			Object rsp = driver.executeScript(js2);
//			System.out.println(rsp);
			
			WebElement body=driver.findElement(By.xpath("//BODY"));
			recurPrint(body, 0);
			WebElement div = driver.findElement(By.xpath("//P[@id='cp"));
			
			List<WebElement> children=div.findElements(By.xpath("./*"));
			for(WebElement child:children){
				System.out.println(child.getTagName());
				System.out.println(child.getText());
			}
			
			System.out.println("x: " + div.getLocation().x);
			System.out.println("y: " + div.getLocation().y);
			System.out.println("width: " + div.getSize().width);
			System.out.println("height: " + div.getSize().height);
			// File scrFile =
			// ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			// Now you can do whatever you need to do with it, for example copy
			// somewhere
			// FileUtils.copyFile(scrFile, new File("/home/lili/screen1.png"));
			WebElement input = driver.findElement(By
					.xpath("//INPUT[@id='kw1']"));

			Object o = driver
					.executeScript(
							"var el=arguments[0];"
									+ "var arr = [];"
									+ "for (var i=0, attrs=el.attributes, l=attrs.length; i<l; i++){"
									+ "arr.push(attrs.item(i).nodeName);"
									+ "arr.push(attrs.item(i).nodeValue);"
									+ "}" + "return arr;", input);

			List<String> attrs = (List<String>) o;
			int i = 0;
			String key = null;
			for (String attr : attrs) {
				if (i % 2 == 0) {
					key = attr;
				} else {
					System.out.println(key + ": " + attr);
				}
				i++;
			}

			System.out.println("x: " + input.getLocation().x);
			System.out.println("y: " + input.getLocation().y);
			System.out.println("width: " + input.getSize().width);
			System.out.println("height: " + input.getSize().height);

		} finally {
			driver.quit();
		}
	}
	
	private static void recurPrint(WebElement elem,int tab){
		String tagName=elem.getTagName();
		for(int i=0;i<tab;i++){
			System.out.print("\t");
		}
		System.out.println("<"+tagName+">");
		
		List<WebElement> children=elem.findElements(By.xpath("./*"));
		for(WebElement child:children){
			recurPrint(child, tab+1);
		}
 
		for(int i=0;i<tab;i++){
			System.out.print("\t");
		}
		System.out.println("</"+tagName+">");
	}

}
