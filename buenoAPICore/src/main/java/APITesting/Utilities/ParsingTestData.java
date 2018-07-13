package APITesting.Utilities;

import java.util.ArrayList;
import java.util.Map;

import APITesting.methods.commonFunctions;

import com.jayway.restassured.response.Response;

public class ParsingTestData {
	public static String getValue(String str, ArrayList<Response> previousJsonResponse,
			Map<String, String> globalVars, int step, String executionId){
		Object temp = "";
		String ret_value = "";
		if(str.contains(Keywords.EXECUTION_ID)){
			ret_value = executionId;
		}else if(str.contains(Keywords.EMPTY_STRING)){
			ret_value = "";
		}else if(str.contains(Keywords.STEP)){
			temp = (step+1);
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";
		}else if(str.contains(Keywords.TIMESTAMP)){
			temp = commonFunctions.getTimestampMS();
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";	
		}else if(str.contains(Keywords.GLOBAL_VARS)){
			String[] params = (str.split(":"));
			temp = globalVars.get(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";
		}else if(str.contains(Keywords.PREV_COOKIE)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse.get(index-1)).getCookie(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";
		}else if(str.contains(Keywords.PREV_HEADER)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse.get(index-1)).header(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";
		}else if(str.contains(Keywords.PREV_BODY)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse.get(index-1)).path(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp.toString();
			else ret_value = temp.toString();
			if(str.endsWith(" ")) ret_value = ret_value + " ";
		}else ret_value = str.toString();
		return ret_value;
	}
	public static String getAllString(String str, ArrayList<Response> previousJsonResponse,
			Map<String, String> globalVars, int step, String executionId){
		String[] params = (str.split("\\[\\+\\]"));
		String total = "";
		for (int i=0 ; i<params.length ; i++){
			if (i==0) total = getValue(params[i], previousJsonResponse, globalVars, step, executionId);
			else total = total + getValue(params[i], previousJsonResponse, globalVars, step, executionId);
		}
		return total;
	}
}