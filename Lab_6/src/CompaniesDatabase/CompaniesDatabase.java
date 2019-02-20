package CompaniesDatabase;

import selects.Select;
import au.com.bytecode.opencsv.CSVReader;
import writers.JSONWriter;
import writers.XMLWriter;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class CompaniesDatabase {
    private List<Company> base;
    private List<ResultOfSelect> allResults;
    private List<String> errorMessages;

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public CompaniesDatabase() {
        base = new ArrayList<>();
        allResults = new ArrayList<>();
        errorMessages = new ArrayList<>();
    }

    public void fill(String path) throws Exception {
        try (FileReader fr = new FileReader(path)) {
            CSVReader csvr = new CSVReader(fr, ';');
            csvr.readAll().forEach(data -> {
                try {
                    base.add(new Company(data));
                } catch (Exception e) {
                    errorMessages.add(e.toString());
                }
            });
        } catch(FileNotFoundException fne){
            throw new FileNotFoundException("File \"" + path + "\" not found");
        }
    }

    public void doSelects(String path) throws Exception {
        try(Scanner sc = new Scanner(new File(path))){
            while(sc.hasNextLine()){
                doSelect(sc.nextLine());
            }
        }
    }

    public void doSelect(String select){
        try{
            Select s = Select.parse(select);
            doSelectBy(s.getField(), s.getRange(), s.getFields());
        } catch(IndexOutOfBoundsException e){
            errorMessages.add("Incorrect companies name of field");
            ResultOfSelect ros = new ResultOfSelect();
            allResults.add(ros);
        } catch (Exception e){
            errorMessages.add(e.toString());
            ResultOfSelect ros = new ResultOfSelect();
            allResults.add(ros);
        }
    }

    private void doSelectBy(String keyField, Range range, List<String> fields) {
        ResultOfSelect ros = new ResultOfSelect(keyField, range, fields);
        ros.addAll(base.stream().filter(comp -> {
            try {
                return range.includes((Comparable) Company
                        .class
                        .getDeclaredMethod("get" + keyField)
                        .invoke(comp));
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList()));
        allResults.add(ros);
    }

    private LocalDate stringToLocalDate(String str){
        return LocalDate.parse(str, DTF);
    }

    public void writeResultsToXML(String[] paths) {
        for(int i = 0; i < 3; ++i){
            try{
                new XMLWriter(paths[i]).write(allResults.get(i));
            } catch (Exception e){
                errorMessages.add("Unsuccessful writing in " + paths[i]);
            }
        }
    }

    public void writeResultsToJSON(String[] paths) {
        for(int i = 0; i < 3; ++i){
            try{
                clearFile(paths[i]);
                new JSONWriter(paths[i]).write(allResults.get(i));
            } catch (Exception e){
                errorMessages.add("Unsuccessful writing in file : file isn't found");
            }
        }
    }

    private void clearFile(String path) throws IOException{
        try(PrintStream ps = new PrintStream(new File(path))){
            ps.println("");
        }
    }

    public void writeStatistic() throws Exception{
        try(PrintStream ps = new PrintStream(new FileOutputStream("src\\logfile.txt", true))) {
            ps.println("\n" + presentDate());
            allResults.forEach(ps::println);
            errorMessages.forEach(ps::println);
        }
    }

    private String presentDate(){
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss").format(LocalDateTime.now());
    }
}