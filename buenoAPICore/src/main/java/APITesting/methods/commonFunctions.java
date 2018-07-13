package APITesting.methods;

import java.sql.Timestamp;

public class commonFunctions {
	
	public static Object getTimestampMS(){
		return (new Timestamp(System.currentTimeMillis()).getTime());
	}
	
	public static String getExecutionId(){
		return "execution@" + Long.toString(new Timestamp(System.currentTimeMillis()).getTime());
	}
}
