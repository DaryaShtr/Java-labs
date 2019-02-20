import CompaniesDatabase.CompaniesDatabase;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


public class Program {
    public static void main(String[] args) throws Exception{
        try{
            CompaniesDatabase cd = new CompaniesDatabase();
            cd.fill("src\\input.csv");
            cd.doSelects("src\\requests\\requests.txt");
            cd.writeResultsToXML(new String[]{"src\\requests\\requests1.xml", "src\\requests\\requests2.xml", "src\\requests\\requests3.xml"});
            cd.writeResultsToJSON(new String[]{"src\\requests\\requests1.json", "src\\requests\\requests2.json", "src\\requests\\requests3.json"});
            cd.writeStatistic();
        }catch (Exception e){
            addRecordsToLogFile(e.toString());
        }
    }

    private static void addRecordsToLogFile(String record){
        try(PrintStream ps = new PrintStream(new FileOutputStream("src\\logfile.txt", true))) {
            ps.println(record);
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}