import org.jsoup.Jsoup;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinksValidator {
    private String protocol;
    private String host;

    private Map<String, String> linksToSites;
    private Map<String, Integer> linksToFiles;
    private List<String> invalidLinks;


    public LinksValidator(){
        linksToSites = new HashMap<>();
        linksToFiles = new HashMap<>();
        invalidLinks = new ArrayList<>();
    }

    public void processAll(String path) throws IOException{
        InputStream is;
        if(isHTMLFile(path)){
            is = new FileInputStream(new File(path));
        } else{
            String source = new Scanner(new File(path)).nextLine();
            URL url = new URL(source);
            protocol = url.getProtocol();
            host = url.getHost();
            is = url.openStream();
        }
        extractLinks(is);
    }

    private boolean isHTMLFile(String path) throws IOException{
        try(Scanner sc = new Scanner(new File(path))){
            if(sc.hasNextLine()){
                return sc.nextLine().matches("<!DOCTYPE .+>");
            } else{
                throw new IOException("File is empty");
            }
        }
    }

    private void extractLinks(InputStream is){
        try(Scanner sc = new Scanner(is)){
            while(sc.hasNextLine()){
                extractLinks(sc.nextLine());
            }
        }
    }

    private void extractLinks(String str){
        try{
            Pattern p = Pattern.compile("(?<=( |^)href ?= ?\\\")[^\\\"]+(?=\\\")");
            Matcher m = p.matcher(str);
            while(m.find()){
                cluster(m.group());
            }
        } catch (NullPointerException e){
            System.out.println(str);
        }
    }

    private void cluster(String link){
        try {
            if (isRelativeReference(link) && protocol != null && host != null) {
                link = protocol + "://" + host + link;
            }
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            if (conn.getContentType().matches("^text/html.*")) {
                linksToSites.put(link, Jsoup.connect(link)
                        .ignoreHttpErrors(true)
                        .get().title());
            } else {
                linksToFiles.put(link, conn.getContentLength());
            }
        } catch (NullPointerException | IOException e) {
            invalidLinks.add(link);
        }
    }

    private boolean isRelativeReference(String str){
        return str.charAt(0) == '/';
    }

    public void printResults(String pathToValid, String pathToInvalid){
        printValidResults(pathToValid);
        printInvalidResults(pathToInvalid);
    }

    public void printValidResults(String path){
        try(PrintStream ps = new PrintStream(new File(path))){
            ps.println("Sites :");
            linksToSites.forEach((link, title) -> ps.println("   " + link + " - " + title));
            ps.println("Files :");
            linksToFiles.forEach((link, size) -> ps.println("   " + link + " - " + size + " bytes"));
        } catch (FileNotFoundException e){
            System.out.println("Unsuccessful printing to " + path);
        }
    }

    public void printInvalidResults(String path){
        try(PrintStream ps = new PrintStream(new File(path))){
            invalidLinks.forEach(ps::println);
        } catch (FileNotFoundException e){
            System.out.println("Unsuccessful printing to " + path);
        }
    }
}
