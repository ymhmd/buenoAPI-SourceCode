package APITesting.Runners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.TestNG;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import APITesting.Utilities.Jira;


public class toolExecuter {
	public static void main(String[] args) throws Exception {
		Execute("Runner.xml", "jira.json");
	}
	public static void Execute(String xmlRunner, String jsonConfig) throws Exception{
		System.out.println("XML Runner file: " + xmlRunner);
		System.out.println("JSON config file: " + jsonConfig);
		System.out.println("==================================");
		TestNG runner = new TestNG();
		List<String> suitefiles = new ArrayList<String>();
		suitefiles.add(xmlRunner);
		runner.setTestSuites(suitefiles);
		runner.run();
		//Jira integration section
		File inputFile = new File(xmlRunner);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        String testSuiteName = ((Element) doc.getElementsByTagName("suite").item(0)).getAttribute("name");
        NodeList tests = doc.getElementsByTagName("test");
        String DIR_SEPARATOR = "/";
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("windows")) DIR_SEPARATOR = "\\\\";
        for (int i = 0; i < tests.getLength(); i++) {
            Node test = tests.item(i);
            if (test.getNodeType() == Node.ELEMENT_NODE) {
                String testName = ((Element) test).getAttribute("name");
                Jira.RemoveMessage("test-output" + DIR_SEPARATOR + testSuiteName + DIR_SEPARATOR + testName + ".xml", jsonConfig);
            }
        }
	}
}