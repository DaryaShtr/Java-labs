import java.io.*;
import java.util.*;


public class ProcessorHTML {
    private List<String> htmlText;
    private List<String> htmlTextSansTags;
    private Map<String, Integer> searchedText;
    private Set<Tag> tags;


    public ProcessorHTML(){
        htmlText = new LinkedList<>();
        htmlTextSansTags = new LinkedList<>();
        searchedText = new HashMap<>();
        tags = new TreeSet<>();
    }

    public void readHTMLFile(String source) throws IOException {
        try(Scanner sc = new Scanner(new File(source))){
            while(sc.hasNextLine()){
                htmlText.add(sc.nextLine());
            }
        }
    }

    public void findText(String source) throws IOException {
        read(source);
        extractTags();
        extractText();
        find();
    }

    private void read(String source) throws IOException {
        try (Scanner sc = new Scanner(new File(source)).useDelimiter("[;\n\r]+")) {
            while (sc.hasNext()) {
                pullSearchedText(sc.next());
            }
        }
    }

    private void pullSearchedText(String textFragment){
        searchedText.put(textFragment, -1);
    }

    private void extractTags(){
        htmlText.forEach(str -> tags.addAll(Tags.asList(str.split("(^.*?<)|(>.*?<)|(>.*?$)"))));
        htmlText.forEach(str -> {for(String str1 : str.split("(^.*?<)|(>.*?<)|(>.*?$)")){
            System.out.println(str1 + "!!");
        }});
        System.out.println(tags);
        tags.remove(Tag.parseTag(""));
    }

    private void extractText(){
        htmlText.forEach(this::copyTextSansTags);
    }

    private void copyTextSansTags(String str){
        htmlTextSansTags.add(str.replaceAll("<.*?>", ""));
    }

    private void find(){
        searchedText.keySet().forEach(textFragment -> searchedText.put(textFragment, findFirstEntry(textFragment)));
    }

    private int findFirstEntry(String textFragment){
        return htmlTextSansTags.indexOf(htmlTextSansTags.stream()
                .filter(str -> str.toLowerCase().contains(textFragment.toLowerCase()))
                .findFirst()
                .orElse(null));
    }

    public void writeTags(String out) throws IOException{
        try(PrintStream ps = new PrintStream(new File(out))){
            tags.stream().map(Tag::toString).forEach(item -> ps.println(item));
        }
    }

    public void writePages(String out) throws IOException{
        try(PrintStream ps = new PrintStream(new File(out))){
            searchedText.entrySet().stream().map(this::makeKeyPlusValue).forEach(item -> ps.println(item));
        }
    }

    private String makeKeyPlusValue(Map.Entry e){
        return e.getKey() + " : " + e.getValue();
    }

    public void writeNotFoundText(String out) throws IOException{
        try(PrintStream ps = new PrintStream(new File(out))){
            searchedText.entrySet().stream().filter(this::notFound).map(Map.Entry::getKey).forEach(item -> ps.println(item));
        }
    }

    private boolean notFound(Map.Entry<String, Integer> e){
        return e.getValue().equals(-1);
    }
}
