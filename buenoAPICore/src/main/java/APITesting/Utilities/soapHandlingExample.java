package APITesting.Utilities;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;

public class soapHandlingExample {
	public static void main(String[] args) {
		RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
				.appendDefaultContentCharsetToContentTypeIfUndefined(false));
		//GetHolidaysAvailableResult/HolidayCode[1]/Code  : NEW-YEARS-DAY-ACTUAL
		//GetHolidaysAvailableResult/HolidayCode[1]/Description  : New Year's Day
		String bdy = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hs=\"http://www.holidaywebservice.com/HolidayService_v2/\">" +
					 "<soapenv:Body>" +
					 "<hs:GetHolidaysAvailable>" +
					 "<hs:countryCode>UnitedStates</hs:countryCode>" +
					 "</hs:GetHolidaysAvailable>" +
					 "</soapenv:Body>" +
					 "</soapenv:Envelope>";
		//String req_1 = "Envelope.Body.GetHolidaysAvailableResponse.GetHolidaysAvailableResult.HolidayCode[1].Code";
		//String req_2 = "Envelope.Body.GetHolidaysAvailableResponse.GetHolidaysAvailableResult.HolidayCode[2].Description";
		RestAssured.defaultParser = Parser.XML;
		Response soapRes = null;
		soapRes = RestAssured.given().config(RestAssured.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
			    .header("Content-Type", "text/xml")
			    .when()
			    .body(bdy)
			    .post("http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx?wsdl");
		//String p = "Envelope.Body.GetHolidaysAvailableResponse.GetHolidaysAvailableResult.HolidayCode";
		String p = "Envelope.Body.GetHolidaysAvailableResponse.GetHolidaysAvailableResult.HolidayCode[1].Code";
		Object i = (soapRes.path(p));
		System.out.println((i));
		
	}

}
