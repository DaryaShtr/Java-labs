import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Program {
    public static void main(String[] args) {
        try{
            LinksValidator lv = new LinksValidator();
            lv.processAll("src\\input.txt");
            lv.printResults("src\\output1.txt", "src\\output2.txt");
        } catch(FileNotFoundException | UnknownHostException e){
            System.out.println("Invalid reference");
            cleanFile("src\\output1.txt");
            cleanFile("src\\output2.txt");
        } catch(Exception e){
            System.out.println(e);
        }
    }

    private static void cleanFile(String path){
        try(PrintStream ps = new PrintStream( new File(path))){
        } catch(FileNotFoundException e){}
    }
}
