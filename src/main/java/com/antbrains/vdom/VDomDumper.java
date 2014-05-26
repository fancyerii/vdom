package com.antbrains.vdom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
 
public class VDomDumper {
	protected static Logger logger=Logger.getLogger(VDomDumper.class);
	private PhantomJSDriver driver;
	private String extJs;
	
	public void close(){
		logger.info("I am closed");
		if(driver!=null){
			driver.quit();
		}
	}
	
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
	public VDomDumper(boolean enableJs,boolean loadImages) throws IOException{
		String skipVideoJs=readJsFile("disableVideo.js");
		extJs=readJsFile("extractor.js");
		
		
		DesiredCapabilities dCaps = new DesiredCapabilities();
		dCaps.setJavascriptEnabled(enableJs);

		//dCaps.setCapability("takesScreenshot", true);
		PhantomJSDriverService service = null;
		PhantomJSDriverService.Builder builder=new PhantomJSDriverService.Builder()
				.usingPhantomJSExecutable(
						new File("./phantomjs-1.9.7-linux-x86_64/bin/phantomjs"))
				.usingGhostDriverCommandLineArguments(new String[] {})
				//.usingCommandLineArguments(new
				//String[]{"--load-images=false"})
				//.usingCommandLineArguments(new String[]{"--debug=true"})
				.usingAnyFreePort();
		if(!loadImages){
			builder.usingCommandLineArguments(new String[]{"--load-images=false"});
		}
		service=builder.build();
		// dCaps.setCapability("phantomjs.page.customHeaders.User-Agent", "");
		driver = new PhantomJSDriver(service, dCaps);

		driver.manage().timeouts()
				.implicitlyWait(30000, TimeUnit.MILLISECONDS);
		driver.manage().timeouts()
				.pageLoadTimeout(30000, TimeUnit.MILLISECONDS);
		driver.manage().timeouts()
				.setScriptTimeout(30000, TimeUnit.MILLISECONDS);
		
		driver.executePhantomJS(skipVideoJs);
		
	}
	
	public VDomNode dump(String url){
		try{
			driver.get(url);
			String s=(String)driver.executeScript(extJs);
			return TreeParser.parse(s);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		
	}

}
