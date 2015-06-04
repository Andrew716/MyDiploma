package excelimport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andrii on 5/28/2015.
 */
public class ParseFile {
    public List<Integer> columns = new ArrayList();
    public static void parseFile(String pathFile){
        try {
            InputStream in = new FileInputStream(pathFile);
            XSSFWorkbook wb = new XSSFWorkbook(in);
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            int [][] array = new int[defineNumberOfRows(pathFile)][defineNumberOfColums(pathFile)];
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    int cellType = cell.getCellType();
                    if (cellType == Cell.CELL_TYPE_NUMERIC){
                        array[cell.getRowIndex()-1][cell.getColumnIndex()-1] = (int) cell.getNumericCellValue();
                    }
                }
            }

            StoreTable.fillArrayForStore(array);
            StoreTable.outputArray(array);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int defineNumberOfRows(String pathFile){
        InputStream in = null;
        try {
            in = new FileInputStream(pathFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);
        int numberOfRows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        return numberOfRows;
    }

    public static int defineNumberOfColums(String pathFile){
        InputStream in = null;
        try {
            in = new FileInputStream(pathFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);
        Row getRow = sheet.getRow(2);
        int numberOfColumns = (int) getRow.getLastCellNum() - getRow.getFirstCellNum() - 1;
        return numberOfColumns;
    }
}
