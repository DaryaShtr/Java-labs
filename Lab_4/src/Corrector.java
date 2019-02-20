import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Corrector {
    private StringBuilder text;
    private List<String> quotedText;
    private List<String> quotedSymbols;


    public Corrector(){
        text = new StringBuilder();
        quotedText = new LinkedList<>();
        quotedSymbols = new LinkedList<>();
    }

    public void readFile(File source) throws Exception {
        try(FileInputStream fis = new FileInputStream(source)){
            Scanner sc = new Scanner(fis);
            while (sc.hasNext()) {
                text.append(sc.nextLine()).append("\r\n");
            }
        }
    }

    public void clearComments() {
        cutQuotedSymbols();
        cutQuotedText();
        removeComments();
        insertQuotedText();
        insertQuotedSymbols();
    }

    private void cutQuotedSymbols(){
        StringBuilder tmp = new StringBuilder();
        Pattern p = Pattern.compile("'\\\\?.'");
        Matcher m = p.matcher(text.toString());
        for(int i = 0; m.find(); ++i){
            quotedSymbols.add(addBackslashes(m.group()));
            m.appendReplacement(tmp, "'" + i + "'");
        }
        m.appendTail(tmp);
        text = tmp;
    }

    private void cutQuotedText(){
        StringBuilder tmp = new StringBuilder();
        Pattern p = Pattern.compile("\\\".*?([^\\\\](\\\\\\\\)*\\\")");
        Matcher m = p.matcher(text.toString());
        for(int i = 0; m.find(); ++i){
            quotedText.add(addBackslashes(m.group()));
            m.appendReplacement(tmp, "\"" + i + "\"");
        }
        m.appendTail(tmp);
        text = tmp;
    }

    private String addBackslashes(String str){
        StringBuilder tmp = new StringBuilder();
        Pattern p = Pattern.compile("\\\\");
        Matcher m = p.matcher(str);
        while(m.find()){
            m.appendReplacement(tmp, "\\\\\\\\");
        }
        m.appendTail(tmp);
        return tmp.toString();
    }

    private void removeComments(){
        Pattern p = Pattern.compile("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)");
        Matcher m = p.matcher(text.toString());
        text = new StringBuilder(m.replaceAll(""));
    }

    private void insertQuotedText(){
        StringBuilder tmp = new StringBuilder();
        Pattern p = Pattern.compile("\".+?\"");
        Matcher m = p.matcher(text.toString());
        while(m.find()){
            m.appendReplacement(tmp, quotedText.get(m.group().charAt(1) - '0'));
        }
        m.appendTail(tmp);
        text = tmp;
    }

    private void insertQuotedSymbols(){
        StringBuilder tmp = new StringBuilder();
        Pattern p = Pattern.compile("\'.+?\'");
        Matcher m = p.matcher(text.toString());
        while(m.find()){
            m.appendReplacement(tmp, quotedSymbols.get(m.group().charAt(1) - '0'));
        }
        m.appendTail(tmp);
        text = tmp;
    }

    public void print(){
        System.out.println(text);
    }

    public void writeToFile(File f) throws IOException {
        try(FileWriter fw = new FileWriter(f)){
            fw.write(text.toString());
        }
    }
}