package selects;

import CompaniesDatabase.Range;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class PointedSelect extends Select {
    public PointedSelect(String str) throws Exception{
        List<String> tokens = new Scanner(str).tokens().collect(Collectors.toList());
        fields = splitIntoFields(tokens.get(1));
        keyField = fieldToNormalCase(tokens.get(5));
        Matcher matcher = Pattern.compile("'.*?'").matcher(str);
        matcher.find();
        String point = withoutQuotationMarks(matcher.group());
        range = new Range(point);
    }


}