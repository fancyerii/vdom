package com.antbrains.vdom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
 

public class TestPhantomjs2 {
	
	public static String readJsFile(String fileName) throws IOException{
		InputStream is=TestPhantomjs2.class.getClassLoader().getResourceAsStream(fileName);
		BufferedReader br=null;
		StringBuilder sb=new StringBuilder();
		try{
			br=new BufferedReader(new InputStreamReader(is,"UTF8"));
			
			String line;
			while((line=br.readLine())!=null){
				sb.append(line).append("\n");
			}
		}finally{
			if(br!=null){
				br.close();
			}
		}
		String js=sb.toString();
		return js;
	}
	
	public static void main(String[] args) throws IOException {
		String skipVideoJs=readJsFile("disableVideo.js");
		String extJs=readJsFile("extractor.js");
		
		
		DesiredCapabilities dCaps = new DesiredCapabilities();
		dCaps.setJavascriptEnabled(true);

		//dCaps.setCapability("takesScreenshot", true);
		PhantomJSDriverService service = new PhantomJSDriverService.Builder()
				.usingPhantomJSExecutable(
						new File("./phantomjs-1.9.7-linux-x86_64/bin/phantomjs"))
				.usingGhostDriverCommandLineArguments(new String[] {})
				//.usingCommandLineArguments(new
				//String[]{"--load-images=false"})
				//.usingCommandLineArguments(new String[]{"--debug=true"})
				.usingAnyFreePort().build();
		// dCaps.setCapability("phantomjs.page.customHeaders.User-Agent", "");
		PhantomJSDriver driver = new PhantomJSDriver(service, dCaps);
		//driver.setLogLevel(Level.FINE);
		try {
			driver.manage().timeouts()
					.implicitlyWait(30000, TimeUnit.MILLISECONDS);
			driver.manage().timeouts()
					.pageLoadTimeout(30000, TimeUnit.MILLISECONDS);
			driver.manage().timeouts()
					.setScriptTimeout(30000, TimeUnit.MILLISECONDS);
			
			driver.executePhantomJS(skipVideoJs);
			driver.get("http://www.newsmth.net/nForum/#!article/AutoWorld/1937709257");
			//driver.get("http://www.baidu.com");
			String o=(String)driver.executeScript(extJs);
			System.out.println(o);
		} finally {
			driver.quit();
		}
	}
	

}
