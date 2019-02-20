package selects;

import CompaniesDatabase.Range;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class RangedSelectFirstType extends Select {
    public RangedSelectFirstType(String str) throws Exception{
        List<String> tokens = new Scanner(str).tokens().collect(Collectors.toList());
        fields = splitIntoFields(tokens.get(1));
        keyField = fieldToNormalCase(tokens.get(5));
        int lowerBound = Integer.parseInt(tokens.get(7));
        int upperBound = Integer.parseInt(tokens.get(11));
        range = new Range(lowerBound, upperBound);
    }
}