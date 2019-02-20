import java.io.*;

public class Program {
    public static void main(String[] args){
        try{
            File inputFile = new File("Program.txt");
            File outputFile = new File("Program Without Comments.txt");
            Corrector cr = new Corrector();
            cr.readFile(inputFile);
            cr.clearComments();
            cr.print();
            cr.writeToFile(outputFile);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}