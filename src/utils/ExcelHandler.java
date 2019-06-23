package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import models.Property;

public class ExcelHandler {
	private static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().trim();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		}
		return null;
	}

	public static List<Property> readBooksFromExcelFile(String excelFilePath) throws IOException {
		List<Property> listBooks = new ArrayList<Property>();
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Property property = new Property();

			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				
				switch (columnIndex) {
				case 1:
					property.setKey((String) getCellValue(nextCell));
					break;
				case 2:
					property.setIwcProperty((String) getCellValue(nextCell));
					break;
				case 3:
					property.setUdProperty((String) getCellValue(nextCell));
					break;
				}

			}
			if(null != property.getKey() && !property.getKey().equalsIgnoreCase("key ( used in code)")){
				listBooks.add(property);
			}
		}
		workbook.close();
		inputStream.close();
		return listBooks;
	}
}
