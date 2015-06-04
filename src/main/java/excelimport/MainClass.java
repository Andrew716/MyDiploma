package excelimport;

/**
 * Created by Andrii on 5/11/2015.
 */
public class MainClass {

    public final static String PATH_FILE = "C:\\Users\\Andrii\\Desktop\\1.xlsx";
    public final static int SIZE_OF_ROWS = ParseFile.defineNumberOfRows(PATH_FILE);
    public final  static int SIZE_OF_COLUMS = ParseFile.defineNumberOfColums(PATH_FILE);

    public static void main(String [] args) {
        ParseFile.parseFile(PATH_FILE);

    }
}
