package APITesting.TCs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import APITesting.RestAssuredModules.HTTPOperations;
import APITesting.Utilities.DocumentationUtilities;
import APITesting.Utilities.ExcelUtilities;
import APITesting.Utilities.Keywords;
import APITesting.Utilities.ParsingTestData;
import APITesting.Utilities.Validation;
import APITesting.Utilities.variablesProvider;
import APITesting.methods.commonFunctions;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;


public class CorrelationTCs_ValuesValidation {
	private ArrayList<Response> previousJsonResponse = new ArrayList<Response>();
	private int Step = -1;	
	private Map<String, String> globalVarsData;
	private String uniqueIdPerExecution = "execution@123";
	
	@BeforeSuite
	public void setUniqueIdPerExecution(){
		uniqueIdPerExecution = commonFunctions.getExecutionId();
	}
	
	@BeforeClass
	public void importGlobalVars(ITestContext context) throws Exception{
		String ExcelFile = context.getCurrentXmlTest().getAllParameters().get("GlobalVarsExcelFile");
		if(ExcelFile == null) return;
		Object[][] data = ExcelUtilities.get_data(ExcelFile,
				variablesProvider.globalVarsSheet,
				variablesProvider.globalVarsCols);
		globalVarsData = new HashMap<String, String>(data.length);
		for (Object[] mapping : data){
			globalVarsData.put(mapping[0].toString(), mapping[1].toString());
		}
	}
	
	@DataProvider()
	public Object[][] ProvideData(ITestContext context) throws Exception{
		String FileType = context.getCurrentXmlTest().getAllParameters().get("FileFormat");
		if(FileType != null && FileType.equalsIgnoreCase("XML")){
			return null;
		}else{
			String ExcelFile = context.getCurrentXmlTest().getAllParameters().get("ExcelFile");
			String ExcelSheet = context.getCurrentXmlTest().getAllParameters().get("ExcelSheet");
			Object[][] testObjArray = ExcelUtilities.get_data(ExcelFile, ExcelSheet, variablesProvider.dataProviderWithValuesCols);
			return (testObjArray);
		}
	}	
	
	@Test(dataProvider="ProvideData")
	public void test_steps(String description, String URL, String reguestMethod, String headerKeys,
			String headerValues, String postBody, String StatusCode,
			String JsonKeys, String values, String DataTypes, String waitPeriod) throws Exception{
		if(waitPeriod != null && !waitPeriod.equals("")){
			Thread.sleep(Long.parseLong(waitPeriod));
		}
		if(reguestMethod.equals("SOAP")){
			RestAssured.defaultParser = Parser.XML;
		}else{
			RestAssured.defaultParser = Parser.JSON;
		}
		Step++;
		//for header data
		String [] headerKeysList = headerKeys.split("\\r?\\n");
		String [] headerValuesList = headerValues.split("\\r?\\n");
		Assert.assertEquals(headerKeysList.length, headerValuesList.length);
		Map <String, String> headersMap = new HashMap<String, String>();
		for(int i=0 ; i<headerKeysList.length ; i++){
			if(headerKeysList[i].equals("") &&  headerValuesList[i].equals("")) break;
			headerKeysList[i] = ParsingTestData.getAllString(headerKeysList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
			headerValuesList[i] = ParsingTestData.getAllString(headerValuesList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
			//for nested global variables
			headerValuesList[i] = ParsingTestData.getAllString(headerValuesList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
			headersMap.put(headerKeysList[i], headerValuesList[i]);
		}
		URL = ParsingTestData.getAllString(URL, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
		reguestMethod = ParsingTestData.getAllString(reguestMethod, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
		postBody = ParsingTestData.getAllString(postBody, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
		//for nested global variables: nested with 3 levels
		postBody = ParsingTestData.getAllString(postBody, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
		postBody = ParsingTestData.getAllString(postBody, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
		
		Response res = HTTPOperations.HTTPRequest(URL, reguestMethod, headersMap, postBody);
		if (res == null){
			Assert.assertEquals(reguestMethod.trim(), variablesProvider.availableRequestTypes,"[Step " + Integer.toString(Step+1) + "]: Invalid Request type check test data please");
		}
		previousJsonResponse.add(Step, res);
		
		//Prepare expected cols
		int StCode = Integer.parseInt(ParsingTestData.getAllString(StatusCode, previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution));
		String [] JsonKeysList = JsonKeys.split("\\r?\\n");
		String [] JsonValuesList = values.split("\\r?\\n");
		String [] DataTypesList = DataTypes.split("\\r?\\n");
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")){
				break;
			}else{
				JsonKeysList[i] = ParsingTestData.getAllString(JsonKeysList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
				JsonValuesList[i] = ParsingTestData.getAllString(JsonValuesList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
				DataTypesList[i] = ParsingTestData.getAllString(DataTypesList[i], previousJsonResponse, globalVarsData, Step, uniqueIdPerExecution);
			}
		}
		String Params = DocumentationUtilities.getTestDataWithValues(URL, reguestMethod, headerKeysList,
				headerValuesList, postBody, StCode, JsonKeysList, JsonValuesList, DataTypesList);
		Params = "[Step " + Integer.toString(Step+1) + "]: "+ description +" [+] \n"+ Params + 
				"The returned response:\n\n"+
				"Headers:\n" + res.headers().toString()+ "\n\n" + 
				"Cookies:\n" + res.cookies().toString()+ "\n\n" + 
				"Body:\n" + res.getBody().prettyPrint() + "\n";
		
		Assert.assertEquals((int)res.getStatusCode(),
				StCode, Params);
		/* Validation section:
		 * this section to check:
		 * 1) Key Data type
		 * 2) Key is exists
		 * 3) Key with specific value
		*/
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")) break;
			
			if(JsonValuesList[i].equals(Keywords.NO_VALIDATION)){
				if(DataTypesList[i].equals(Keywords.NO_VALIDATION)){
					//check it is null only
					if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
						String[] ps = (JsonKeysList[i].split(":"));
						Assert.assertNull(res.getHeader(ps[1]), 
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");
					}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
						String[] ps = (JsonKeysList[i].split(":"));
						Assert.assertNull(res.getCookie(ps[1]),
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");
					}else{
						Assert.assertNull(res.path(JsonKeysList[i]), 
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");	
					}
				}else{
					if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
						String[] ps = (JsonKeysList[i].split(":"));
						//check it is not null
						Assert.assertNotNull(res.getHeader(ps[1]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
						String[] ps = (JsonKeysList[i].split(":"));
						//check it is not null
						Assert.assertNotNull(res.getCookie(ps[1]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					}else{
						//check it is not null
						Assert.assertNotNull(res.path(JsonKeysList[i]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
						//check data type
						String s = res.path(JsonKeysList[i]).getClass().getName().toLowerCase();
						String actualDataType = s.split("\\.")[2];
						Assert.assertEquals(actualDataType, DataTypesList[i].toLowerCase(),
								Params + "It is: " + JsonKeysList[i] + "\n");	
					}
				}
			}else{
				if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
					String[] ps = (JsonKeysList[i].split(":"));
					//check it is not null
					Assert.assertNotNull(res.getHeader(ps[1]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//compare values
					Assert.assertEquals(res.getHeader(ps[1]).toString(),
							JsonValuesList[i],
							Params + "It's: " + JsonKeysList[i] + "\n");
				}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
					String[] ps = (JsonKeysList[i].split(":"));
					//check it is not null
					Assert.assertNotNull(res.getCookie(ps[1]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//compare values
					Assert.assertEquals(res.getCookie(ps[1]).toString(),
							JsonValuesList[i],
							Params + "It's: " + JsonKeysList[i] + "\n");
				}else{
					//check it is not null
					Assert.assertNotNull(res.path(JsonKeysList[i]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//check data type
					String s = (res.path(JsonKeysList[i])).getClass().getName().toLowerCase();
					String actualDataType = s.split("\\.")[2];
					Assert.assertEquals(actualDataType, DataTypesList[i].toLowerCase(),
							Params + "It is: " + JsonKeysList[i] + "\n");
					//compare values
					String actualValue = res.path(JsonKeysList[i]).toString();
					String expectedRegex = JsonValuesList[i];
					boolean matched = Validation.isMatched(actualValue, expectedRegex);
					Assert.assertEquals(true, matched, Params 
							+ "Expected [" + expectedRegex + "] is NOT match Actual [" + actualValue + "]");					
//					Assert.assertEquals(res.path(JsonKeysList[i]).toString(),
//							JsonValuesList[i],
//							Params + "It's: " + JsonKeysList[i] + "\n");
				}
			}
		}	
	}
}
