package seleiniumprojects;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;

public class SuiteCRM {
	WebDriver driver;
	WebDriverWait wait;
	String uniqueString;
	
	
  @Test(description="Verify the website title", enabled=true)
  public void verifyTitle() {
	  Assert.assertEquals("SuiteCRM", driver.getTitle());	  
  }
  
  @Test(description="Print the url of the header image to the console", enabled=true)
  public void getHeaderUrl() {
	  String url=driver.findElement(By.xpath("//a[@title='SuiteCRM']")).getAttribute("href");
	  System.out.println("The URL of the header image: "+url);
  }
  
  @Test(description="Print the first copyright text in the footer to the console", enabled=true)
  public void getCopyrightText() {
	  String text=driver.findElement(By.id("admin_options")).getText();
	  System.out.println("The first copyright text in the footer: "+text);
  }
  
  @Test(description="Open the site and login with the credentials provided", enabled=true)
  @Parameters({"username","password"})
  public void login(String username, String password) {
	  driver.findElement(By.id("user_name")).sendKeys(username);
	  driver.findElement(By.id("username_password")).sendKeys(password);
	  driver.findElement(By.id("bigbutton")).click();
	  Assert.assertTrue(driver.findElement(By.xpath("//a[@id='tab0']")).isDisplayed());
  }
  
  @Test(description="Get the color of the navigation menu", dependsOnMethods= {"login"}, enabled=true)
  public void getColors() {
	 // String color=driver.findElement(By.xpath("//div[@class='sidebar']")).getCssValue("display");
	  String color=driver.findElement(By.xpath("//div[@id='toolbar']")).getCssValue("background-color");
	  System.out.println("The color of the Menu bar: "+Color.fromString(color).asHex());
	  System.out.println("The color of the Side bar: "+driver.findElement(By.xpath("//div[@class='sidebar']")).getCssValue("display"));
  }
  
  @Test(description="Make sure that the Activities menu item exists and is clickable", dependsOnMethods= {"login"}, enabled=true)
  public void checkMenu() { 
	  List<WebElement> menus=driver.findElements(By.xpath("//a[@class='dropdown-toggle grouptab']"));
	  for (WebElement webElement : menus) {
		  if(webElement.getText().equals("Activities")){
			  Assert.assertTrue(webElement.isEnabled());
			  break;
		  }		
	  }	  
  }
  
  @Test(description="Reading a popup that contains additional information about the account/lead", dependsOnMethods= {"login"}, enabled=true)
  public void readAdditionalInfo() {
	  Actions action = new Actions(driver);
	  action.moveToElement(driver.findElements(By.xpath("//a[@class='dropdown-toggle grouptab']")).get(0)).moveToElement(driver.findElements(By.xpath("//a[@id='moduleTab_9_Leads']")).get(0)).click().build().perform();	  
	    
	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-action-name='Create']"))).click();
	  driver.findElement(By.xpath("//a[@data-action-name='Create']")).click();
	  
	  wait.until(ExpectedConditions.elementToBeClickable(By.id("first_name"))).click();
	  driver.findElement(By.id("first_name")).sendKeys(generateUniqueString(5));
	  driver.findElement(By.id("last_name")).sendKeys(generateUniqueString(5));
	  driver.findElement(By.id("phone_work")).sendKeys("9876543201");
	  driver.findElement(By.id("phone_mobile")).sendKeys("8989898998");
	  driver.findElement(By.id("Leads0emailAddress0")).sendKeys(generateUniqueString(5)+"@gmail.com");
	  driver.findElement(By.id("SAVE")).click();
	  
	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-action-name='List']"))).click();
	  driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	  driver.findElement(By.xpath("//a[@data-action-name='List']")).click();

	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h2[@class='module-title-text']"))).click();
	  
	  driver.findElements(By.xpath("//span[@title='Additional Details']")).get(0).click();
	  
	  
	  driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	  
	  List<WebElement> eles=driver.findElements(By.xpath("//div[contains(text(),'Additional Details')]"));
	  for(int i=0;i<eles.size();i++) {
		  if(eles.get(i).isDisplayed()) {
			  action.moveToElement(eles.get(i)).click();
			  break;
		  }		  
	  }  
	  
	  System.out.println("The phone number on the popup is: "+driver.findElement(By.xpath("//span[@class='phone']")).getText());
  }
  
  @Test(description="Open the accounts page and print the contents of the table", dependsOnMethods= {"login"}, enabled=true)
  public void traversingTables() {
	  Actions action = new Actions(driver);
	  action.moveToElement(driver.findElements(By.xpath("//a[@class='dropdown-toggle grouptab']")).get(0)).moveToElement(driver.findElements(By.xpath("//a[@id='moduleTab_9_Accounts']")).get(0)).click().build().perform();	  
	 
	  driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	  
	  /*List<WebElement> eles=driver.findElements(By.xpath("//a[starts-with(@href,'?action=ajaxui#ajaxUILoc=index.php%3Fmodule%3DAccounts%26offset%')]"));
	 
	  System.out.println("The names of the first 5 odd-numbered rows of the table:"+eles.size());
	  
	  wait.until(ExpectedConditions.elementToBeClickable(eles.get(0))).click();
	  
	  for(int i=1,j=1;i<eles.size();i=i+2) {		  
		  if(j<=5) {
			  System.out.println(eles.get(i).getText());
			  j++;
		  }	
		  else {
			  break;
		  }
	  }  */
	  
	  WebElement baseTable = driver.findElement(By.tagName("table"));
	  
	  wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr/td[3]/b/a"))));
	  for (int i=1;i<=10;i+=2) {
		  WebElement tableRow = baseTable.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]"));
      
		  System.out.println("The Name: "+tableRow.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]/td[3]/b/a")).getText());
	  }
	  
  }
  
  @Test(description="Open the leads page and print the usernames and full names from the table", dependsOnMethods= {"login"}, enabled=true)
  public void traversingTables2() {
	  Actions action = new Actions(driver);
	  action.moveToElement(driver.findElements(By.xpath("//a[@class='dropdown-toggle grouptab']")).get(0)).moveToElement(driver.findElements(By.xpath("//a[@id='moduleTab_9_Leads']")).get(0)).click().build().perform();	  
	  
	  driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	  
	  WebElement baseTable = driver.findElement(By.tagName("table"));
	  driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
	  
	  wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr/td[3]/b/a"))));
	  for (int i=1;i<=10;i++) {
		  WebElement tableRow = baseTable.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]"));
		  wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]"))));
		  System.out.println("The Name: "+tableRow.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]/td[3]/b/a")).getText()+" AND the User: "+tableRow.findElement(By.xpath("//table[@class='list view table-responsive']/tbody/tr["+i+"]/td[8]/a")).getText());
	  }
	  
  }
  
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
  }
  
  
  public String generateUniqueString(int n) {
	// chose a Character random from this String 
      String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                  + "0123456789"
                                  + "abcdefghijklmnopqrstuvxyz"; 

      // create StringBuffer size of AlphaNumericString 
      StringBuilder sb = new StringBuilder(n); 

      for (int i = 0; i < n; i++) { 

          // generate a random number between 
          // 0 to AlphaNumericString variable length 
          int index 
              = (int)(AlphaNumericString.length() 
                      * Math.random()); 

          // add Character one by one in end of sb 
          sb.append(AlphaNumericString 
                        .charAt(index)); 
      } 
      
      String generatedString=sb.toString().substring(0, 1).toUpperCase() + sb.toString().substring(1);
      
      StringBuffer uniqueString=new StringBuffer(generatedString);
      
      for(int i = 1; i < generatedString.length(); i++) {   
      	
      	if(Character.isUpperCase(generatedString.charAt(i))) {    
              //Convert it into upper case using toLowerCase() function    
          	uniqueString.setCharAt(i, Character.toLowerCase(generatedString.charAt(i)));    
          }
          
       /*   //Checks for lower case character    
          if(Character.isLowerCase(generatedString.charAt(i))) { 
				//Convert it into upper case using toUpperCase() function    
              uniqueString.setCharAt(i, Character.toUpperCase(generatedString.charAt(i)));    
          }    
          //Checks for upper case character    
          else if(Character.isUpperCase(generatedString.charAt(i))) {    
              //Convert it into upper case using toLowerCase() function    
          	uniqueString.setCharAt(i, Character.toLowerCase(generatedString.charAt(i)));    
          }    */
      }    
      

      return uniqueString.toString();
  }
  
  
  @BeforeTest
  public void beforeTest() {
	  System.setProperty("webdriver.gecko.driver", "C:\\Users\\PrasanthiChippidi\\Documents\\Personel\\SDET\\geckodriver-v0.27.0-win64\\geckodriver.exe");
	  driver=new FirefoxDriver();
	  wait = new WebDriverWait(driver, 20);
	  driver.get("https://alchemy.hguy.co/crm");
  }

  @AfterTest
  public void afterTest() {
	  driver.close();
  }

}
