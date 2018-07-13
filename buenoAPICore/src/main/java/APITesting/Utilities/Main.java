package APITesting.Utilities;

import java.util.HashMap;
import java.util.Map;

import APITesting.RestAssuredModules.HTTPOperations;

import com.jayway.restassured.response.Response;

public class Main {
	public static boolean isMatched(String str, String regex){
        return str.matches(regex);
    }
	public static boolean executeCmdLine(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	public static void main(String[] args) throws Exception {
		Map <String, String> headersMap = new HashMap<String, String>();
		headersMap.put("Authorization", "Token token=\"7zQuK63tYCxsVZb5nbjG\" , email=\"msoliman3890@hotmail.com\"");
		String URL = "https://api.instabug.com/api/web/applications/birdy-demo-app/beta/bugs/8584/comments";
		Response res = HTTPOperations.HTTPRequest(URL, "POSTFormData", headersMap, "comment[comment]=hello_2345");
		/*
		Response res = given()
		.multiPart("comment[comment]", "hello_500")
		.config(RestAssured.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
			    .relaxedHTTPSValidation()
			    .header("Authorization", "Token token=\"7zQuK63tYCxsVZb5nbjG\" , email=\"msoliman3890@hotmail.com\"")
			    .when()
			    .post("");
		*/
		System.out.println(res.getBody().prettyPrint());		
	}
}