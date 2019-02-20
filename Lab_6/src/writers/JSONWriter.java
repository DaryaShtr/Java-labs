package writers;

import CompaniesDatabase.ResultOfSelect;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class JSONWriter {
    private String path;


    public JSONWriter(String path){
        this.path = path;
    }

    public void write(ResultOfSelect ros) throws Exception{
        try (FileWriter fw = new FileWriter(new File(path))) {
            JSONObject rootJSON = new JSONObject();
            JSONArray allResultsJSON = new JSONArray();
            allResultsJSON.put(ros.toJSONObject());
            rootJSON.put("All Results", allResultsJSON);
            fw.write(rootJSON.toString(3));
        }
    }
}
