import java.util.*;


public class Matrix {
    private int[][] matrix;
    private Map<Integer, Integer> map;
    private int sizeX;
    private int sizeY;
    private int result;
    private boolean isResult;


    public Matrix(){
        map = new TreeMap<>(Comparator.reverseOrder());
    }

    public void inputSize() throws Exception{
        inputSizeX();
        inputSizeY();
        checkSizes();
        createMatrix();
    }

    private void inputSizeX() {
        System.out.println("Input number of columns");
        sizeX = new Scanner(System.in).nextInt();
    }

    private void inputSizeY() {
        System.out.println("Input number of lines");
        sizeY = new Scanner(System.in).nextInt();
    }

    private void checkSizes() throws Exception {
        if (sizeX <= 0 || sizeY <= 0) {
            throw new Exception("Incorrect size of matrix");
        }
    }

    private void createMatrix() {
        matrix = new int[sizeY][sizeX];
    }

    public void fillingMatrix() {
        for (int i = 0; i < sizeY; ++i) {
            for (int j = 0; j < sizeX; ++j) {
                putOnContainers(i, j);
            }
        }
    }

    private void putOnContainers(int i, int j) {
        matrix[i][j] = new Random().nextInt(19) - 9;
        if (map.containsKey(matrix[i][j])) {
            map.put(matrix[i][j], map.get(matrix[i][j]) + 1);
        } else {
            map.put(matrix[i][j], 1);
        }
    }

    public void countingResult(){
        for (Integer key : map.keySet()) {
            if (map.get(key) == 2) {
                result = key;
                isResult = true;
                break;
            }
        }
    }

    public void print(){
        for (int[] line : matrix) {
            for(int element : line){
                System.out.printf("%3d", element);
            }
            System.out.println();
        }
    }

    public void outputResult(){
        if(isResult){
            System.out.println("The maximum element that occurs twice : " + result);
        } else{
            System.out.println("There is no element that occurs twice");
        }
    }
}