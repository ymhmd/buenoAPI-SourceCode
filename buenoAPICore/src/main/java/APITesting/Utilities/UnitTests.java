package APITesting.Utilities;

import org.testng.Assert;
import org.testng.annotations.Test;


public class UnitTests {
	
	public static String getValue(String str){
		String temp = "";
		String ret_value = "";
		if(str.contains("#prev_body_res:")){
			temp = "==XX==";
			if(str.startsWith(" ")) ret_value = " " + temp;
			else ret_value = temp;
			if(str.endsWith(" ")) ret_value += " ";
		}else ret_value = str;
		return ret_value;
	}
	
	public static String getAllString(String str){
		String[] params = (str.split("\\[\\+\\]"));
		String total = "";
		for (int i=0 ; i<params.length ; i++){
			System.out.println("**" + params[i] + "**");
			if (i==0) total = getValue(params[i]);
			else total = total + getValue(params[i]);
		}
		return total;
	}
	
		
	@Test
	public void tc_001(){
		String s = "{'username':'02885808K@nuevomivf.es','[+]#prev_body_res:--[+]':'Prueba2468'}";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "{'username':'02885808K@nuevomivf.es','==XX==':'Prueba2468'}");
	}

	@Test
	public void tc_002(){
		String s = "Bearer [+]#prev_body_res:jws";//
		String actual = getAllString(s);
		Assert.assertEquals(actual, "Bearer ==XX==");
	}
	
	@Test
	public void tc_003(){
		String s = "Bearer Bearer";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "Bearer Bearer");
	}
	
	
	@Test
	public void tc_004(){
		String s = "#prev_body_res:-- [+]#prev_body_res:--";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "==XX== ==XX==");
	}
	
	public void tc_005(){
		String s = "Bearer [+]#prev_body_res:-- [+]Bearer";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "Bearer ==XX== Bearer");
	}
	
	@Test
	public void tc_006(){
		String s = "Bearer +] Bearer";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "Bearer +] Bearer");
	}
	
	@Test
	public void tc_007(){
		String s = "Bearer[+Bearer";
		String actual = getAllString(s);
		Assert.assertEquals(actual, "Bearer[+Bearer");
	}
}
