package CompaniesDatabase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ResultOfSelect {

    private String selectName;
    private Range range;
    private List<String> fields;
    private List<Company> results;


    public ResultOfSelect(){
        this(null, null, new ArrayList<>());
    }

    public ResultOfSelect(String selectName, Range range, List<String> fields){
        this.selectName = selectName;
        this.range = range;
        this.fields = new ArrayList<>(fields);
        results = new ArrayList<>();
    }

    public void addAll(List<Company> companies){
        results.addAll(companies);
    }

    public String getSelectName() {
        return selectName;
    }

    public Range getRange() { return range; }

    public List<Company> getResults(){ return results; }

    @Override
    public String toString(){
        if(selectName == null && range == null){
            return "Unrecognized select";
        } else{
            return selectName + " " + range + " - " + results.size();
        }
    }

    public Element toXMLElement(){
        try{
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rosXML = document.createElement("Companies");
            getResults().forEach(company -> rosXML.appendChild(document.adoptNode(company.fieldsToXML(fields))));
            return rosXML;
        } catch (Exception e){ return null; }
    }

    public JSONObject toJSONObject(){
        try{
            JSONObject rosJSON = new JSONObject();
            JSONArray resultsJSON = new JSONArray();
            getResults().forEach(company -> {
                try{
                    resultsJSON.put(company.fieldsToJSON(fields));
                } catch (Exception e){}
            });
            rosJSON.put("Companies", resultsJSON);
            return rosJSON;
        } catch (Exception e){ return null; }
    }
}