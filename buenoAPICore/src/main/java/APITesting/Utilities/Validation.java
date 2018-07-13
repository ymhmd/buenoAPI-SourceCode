package APITesting.Utilities;


public class Validation {
	public static boolean isMatched(String str, String regex){
        return str.matches(regex);
	}
}
