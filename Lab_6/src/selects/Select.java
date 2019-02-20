package selects;

import CompaniesDatabase.Company;
import CompaniesDatabase.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public abstract class Select {
    protected String keyField;
    protected Range range;
    protected List<String> fields;


    public static Select parse(String str) throws Exception{
        String regexOfPointedSelect = "(?i)select ([\\w,]+|\\*) from dataBase where \\w+ = '.+'";
        String regexOfRangedSelectFirst = "(?i)select ([\\w,]+|\\*) from dataBase where \\w+ > [0-9]+ and \\w+ < [0-9]+";
        String regexOfRangedSelectSecond = "(?i)select ([\\w,]+|\\*) from dataBase where \\w+ < [0-9]+ and \\w+ > [0-9]+";
        if(str.matches(regexOfPointedSelect)) {
            return new PointedSelect(str);
        } else if(str.matches(regexOfRangedSelectFirst)){
            return new RangedSelectFirstType(str);
        } else if(str.matches(regexOfRangedSelectSecond)){
            return new RangedSelectSecondType(str);
        } else throw new Exception("Incorrect select");
    }

    public String getField(){
        return keyField;
    }

    public Range getRange(){
        return range;
    }

    public List<String> getFields() { return fields; }

    protected String withoutQuotationMarks(String str){
        return str.substring(1, str.length() - 1);
    }

    protected String fieldToNormalCase(String str){
        return Arrays
                .stream(Company.getFieldsNames())
                .filter(field -> field.equalsIgnoreCase(str))
                .collect(Collectors.toList())
                .get(0);
    }

    protected List<String> splitIntoFields(String str){
        if(str.equals("*")){
            return Arrays.asList(Company.getFieldsNames());
        } else{
            return allToSpecialForm(Arrays.asList(str.split(",")));
        }
    }

    protected List<String> allToSpecialForm(List<String> fields) {
        List<String> newFields = new ArrayList<>();
        fields.forEach(field -> newFields.add(fieldToNormalCase(field)));
        return newFields;
    }
}