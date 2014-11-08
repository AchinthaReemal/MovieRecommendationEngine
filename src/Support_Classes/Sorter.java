

package Support_Classes;

/**
 *
 * @author Isuru
 */
public class Sorter {
    
    
    private double array[];
    private int length;
 
    public void sort(double[] inputArr) {
         
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }
 
    private void quickSort(int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        double pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        while (i <= j) {
 
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                i++;
                j--;
            }
        }
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
 
    private void exchangeNumbers(int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public void printAr(){
        for(double i:array){
            System.out.print(i);
            System.out.print(" ");
        }

    }
    
    public double[] getRes(){
        return array;
    }

    
}
