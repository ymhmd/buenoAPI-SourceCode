package APITesting.Utilities;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import APITesting.RestAssuredModules.HTTPOperations;
import com.jayway.restassured.response.Response;


public class Jira {
	public static void RemoveMessage(String file, String jsonConfig) throws Exception{
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(new InputSource(file));
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList)xpath.evaluate("//*[contains(@message, 'Step')]", doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++){
        	Node value = nodes.item(i).getAttributes().getNamedItem("message");
            String fullMsg = value.getNodeValue();
        	String newMsg = (fullMsg.split("\\[\\+\\]"))[0];
        	String description = (fullMsg.split("\\[\\+\\]"))[1];
        	value.setTextContent(newMsg);
        	description = description.replaceAll(variablesProvider.dashLine, "\\ *");
        	createJiraIssue(jsonConfig, newMsg, description);
        }
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), new StreamResult(new File(file)));
	}
	private static boolean createJiraIssue(String jsonConfig, String summary, String description) throws Exception{
		JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(jsonConfig));
        JSONObject jsonObject = (JSONObject) obj;
        boolean enable = (Boolean) jsonObject.get("jiraIntegrationEnable");
        if(enable){
        	String jiraDescription = "";
        	String [] ps = description.split(variablesProvider.dashLine.replaceAll("\n", ""));
    		for (String p : ps) jiraDescription = jiraDescription + p + "\\\\\\\\ *";
    		jiraDescription = jiraDescription.replaceAll("\n", "");
    		jiraDescription = jiraDescription.substring(0, jiraDescription.length() - 6);
        	System.out.println("-------------------------------------------------------------------");
            System.out.println("> Logging issue on Jira.....");
        	String domain = (String) jsonObject.get("domain");
        	String url = "https://" + domain + ".atlassian.net/rest/api/2/issue/";
        	String email = (String) jsonObject.get("email");
        	String password = (String) jsonObject.get("password");
        	String projectKey = (String) jsonObject.get("projectKey");
        	String issueType = (String) jsonObject.get("issueType");
        	String priority = (String) jsonObject.get("priority");
        	byte[] token = Base64.encodeBase64((email + ":" + password).getBytes());
        	Map<String, String> headersMap = new HashMap<String, String>();
        	headersMap.put("Content-Type", "application/json");
        	headersMap.put("Accept", "application/json");
        	headersMap.put("Authorization", ("Basic " + new String(token)));
        	String body = "{\n" +
                    "    \"fields\": {\n" +
                    "       \"project\":\n" +
                    "       { \n" +
                    "          \"key\": \""+projectKey+"\"\n" +
                    "       },\n" +
                    "       \"summary\": \""+summary+"\",\n" +
                    "       \"description\": \""+jiraDescription+"\",\n" +
                    "       \"issuetype\": {\n" +
                    "       \t\t\"name\": \""+issueType+"\"\n" +
                    "       },\n" +
                    "       \"priority\": {\n" +
                    "       \t\t\"name\": \""+priority+"\"\n" +
                    "       }\n" +
                    "   }\n" +
                    "}";
        	Response res = HTTPOperations.HTTPRequest(url, "POST", headersMap, body);
            if((res.statusCode())==201){
            	System.out.println("Successful Jira Log: " + summary);
            	System.out.println("-------------------------------------------------------------------");
            	return true;
            	
            }else {
            	System.out.println("Failed Jira Log: " + summary);
            	System.out.println("Jira Response: \n" + res.getBody().prettyPrint());
            	System.out.println(body);
            	System.out.println("-------------------------------------------------------------------");
            	return false;
            }
        }else {
        	System.out.println("-------------------------------------------------------------------");
        	System.out.println("Jira Integration is disabled");
        	System.out.println("-------------------------------------------------------------------");
        	return true;
        }
	}
}
