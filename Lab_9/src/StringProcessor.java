import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringProcessor {
    //private String source;
    //private String outputPath;

    private List<String> originalText;
    private List<List<String>> results;


    public StringProcessor() throws IOException {
        originalText = new ArrayList<>();
        results = new ArrayList<>();

    }

    public void read(String source) throws IOException{
        try(Scanner sc = new Scanner(new File(source))){
            while(sc.hasNextLine()){
                originalText.add(sc.nextLine());
            }
        }
    }

    private void removeSmth(String regex){
        List<String> result = new ArrayList<>();
        originalText.forEach(str -> result.add(str.replaceAll(regex, "")));
        results.add(result);
    }

    public void removeParenthesis(){
        String regex = "\\(.*\\)";
        removeSmth(regex);
    }

    public void removeExtraNumber(){
        String regex = "(?<=\\d{2})\\d*";
        removeSmth(regex);
    }

    public void removeLeadingZeros(){
        String regex = "(?<=\\D|^)(0+(?=[1-9]+)|0+(?=0))";
        removeSmth(regex);
    }

    public void write(String outputPath){
        try(PrintStream ps = new PrintStream(new File(outputPath))){
            results.forEach(modifiedText -> {
                modifiedText.forEach(ps::println);
                ps.println();
            });
        } catch (IOException e){
            System.out.println("Unsuccessful writing in " + outputPath);
        }
    }
}