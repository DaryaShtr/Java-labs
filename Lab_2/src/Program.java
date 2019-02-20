public class Program {
    public static void main(String[] args){
        try {
            Matrix matrix = new Matrix();
            matrix.inputSize();
            matrix.fillingMatrix();
            matrix.countingResult();
            matrix.print();
            matrix.outputResult();
        }
        catch(NumberFormatException e){
            System.out.println("Incorrect data");
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }
}