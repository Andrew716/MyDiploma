package excelimport;

/**
 * Created by Andrii on 5/10/2015.
 */
public class StoreTable {
    public static int[][] fillArrayForStore(int[][] array){
        int[][] tempArray = new int[MainClass.SIZE_OF_ROWS][MainClass.SIZE_OF_COLUMS];
        for (int i = 0; i < MainClass.SIZE_OF_ROWS; i++){
            for (int j = 0; j < MainClass.SIZE_OF_COLUMS; j++){
                tempArray[i][j] = array[i][j];
            }
        }
        return tempArray;
    }

    public static void outputArray(int[][] array){
        for (int i = 0; i < MainClass.SIZE_OF_ROWS; i++){
            for (int j = 0; j < MainClass.SIZE_OF_COLUMS; j++){
                System.out.print(array[i][j] + "  ");
            }
            System.out.println();
        }
    }
}