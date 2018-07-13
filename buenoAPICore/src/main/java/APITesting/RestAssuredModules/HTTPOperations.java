package APITesting.RestAssuredModules;

import static com.jayway.restassured.RestAssured.given;

import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;

public class HTTPOperations {
	
	public static Response HTTPRequest(String URL, String Type, Map<String, String> headersMap, String body){
		Response res = null;
		if(Type.trim().equalsIgnoreCase("POSTFormData")){
			String [] params = body.split("=");
			if(headersMap.size()>0){
				res = given()
						.multiPart(params[0], params[1])
						.config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.when()
						.post(URL);
			}else{
				res = given()
						.multiPart(params[0], params[1])
						.config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.when()
						.post(URL);
			}
		}
		if(Type.trim().equalsIgnoreCase("SOAP")){
			res = given().config(RestAssured.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
				    .relaxedHTTPSValidation().headers(headersMap)
				    .body(body)
				    .when()
				    .post(URL);	
		}else if(Type.trim().equalsIgnoreCase("GET")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.when()
						.get(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.when()
						.get(URL);
			}			
		}else if(Type.trim().equalsIgnoreCase("DELETE")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.delete(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.delete(URL);
			}			
		}else if(Type.trim().equalsIgnoreCase("POST")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.post(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.post(URL);
			}
		}else if(Type.trim().equalsIgnoreCase("PATCH")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.patch(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.patch(URL);
			}
		}else if(Type.trim().equalsIgnoreCase("PUT")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.put(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.put(URL);
			}
		}
		return res;
	}
}
