package APITesting.Utilities;
import java.io.File;
import jxl.Sheet;
import jxl.Workbook;


public class ExcelUtilities {
	public static Object[][] get_data(String file, String sheetName, int cols) throws Exception{
		Workbook workbook = Workbook.getWorkbook(new File(file));
		Sheet sheet = workbook.getSheet(sheetName);
		int records = sheet.getRows()-1;
		int currentPosition = 1;
		Object[][] values = new Object[records][cols];
		for(int i = 0 ; i < records ; i++, currentPosition++){
			for(int j = 0 ; j < cols ; j++) values[i][j] = sheet.getCell(j, currentPosition).getContents();
		}
		workbook.close();
		return values;
	}
}
