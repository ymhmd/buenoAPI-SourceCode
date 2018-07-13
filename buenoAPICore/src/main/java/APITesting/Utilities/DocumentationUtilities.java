package APITesting.Utilities;

public class DocumentationUtilities {
	
	public static String getTestDataWithValues(String url, String type, String[] headersKeys
			,String[] headersValues, String body, int statusCode
			,String[] JsonPaths, String[] JsonValues, String[] DataTypes){
		
		String output = variablesProvider.dashLine + "URL: \n <" + url + ">" + variablesProvider.dashLine;
		output = output + "Request type: \n <" + type + ">" + variablesProvider.dashLine;
		String headerOutput = "N/A";
		String pathOutput = "N/A";
		
		for(int i=0 ; i<headersKeys.length; i++){
			if(i==0){
				headerOutput = " <" + headersKeys[i] + "> : <" + headersValues[i] + ">";
			}else{
				headerOutput = headerOutput + "\n <" + headersKeys[i] + "> : <" + headersValues[i] + ">";
			}
			
		}
		for(int i=0 ; i<JsonPaths.length; i++){
			if (JsonPaths[i].equals("")) break;
			if(i==0){
				pathOutput = " <" + JsonPaths[i] + "> : <" + DataTypes[i] + "> : <" + JsonValues[i] + ">";
			}else{
				pathOutput = pathOutput + "\n <" + JsonPaths[i] + "> : <" + DataTypes[i] + "> : <" + JsonValues[i] + ">";
			}
			
		}
		output = output + "Headers: \n" + headerOutput + variablesProvider.dashLine;
		output = output + "Request body: \n <" + body + ">" + variablesProvider.dashLine;
		output = output + "Expected status code: \n" + Integer.toString(statusCode) + variablesProvider.dashLine;
		output = output + "Json paths to check: \n" + pathOutput + variablesProvider.dashLine;
		return output;
		
	}
	
	public static String getTestDataWithoutValues(String url, String type, String[] headersKeys
			,String[] headersValues, String body, int statusCode
			,String[] JsonPaths, String[] DataTypes){
		String dashLine = "\n************************************************************************************\n";
		String output = dashLine + "URL: \n <" + url + ">" + dashLine;
		output = output + "Request type: \n <" + type + ">" + dashLine;
		
		String headerOutput = "N/A";
		String pathOutput = "N/A";
		
		for(int i=0 ; i<headersKeys.length; i++){
			if(i==0){
				headerOutput = " <" + headersKeys[i] + "> : <" + headersValues[i] + ">";
			}else{
				headerOutput = headerOutput + "\n <" + headersKeys[i] + "> : <" + headersValues[i] + ">";
			}
			
		}
		for(int i=0 ; i<JsonPaths.length; i++){
			if (JsonPaths[i].equals("")) break;
			if(i==0){
				pathOutput = " <" + JsonPaths[i] + "> : <" + DataTypes[i] + ">";
			}else{
				pathOutput = pathOutput + "\n <" + JsonPaths[i] + "> : <" + DataTypes[i] + ">";
			}
			
		}
		output = output + "Headers: \n" + headerOutput + dashLine;
		output = output + "Request body: \n <" + body + ">" + dashLine;
		output = output + "Expected status code: \n" + Integer.toString(statusCode) + dashLine;
		output = output + "Json paths to check: \n" + pathOutput + dashLine;
		return output;
	}
	
}

