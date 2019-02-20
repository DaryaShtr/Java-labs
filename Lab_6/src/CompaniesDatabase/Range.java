package CompaniesDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Range<T extends Comparable<T>> {
    private T from;
    private T to;

    private String fromStr;
    private String toStr;

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public Range(T from, T to) throws Exception{
        this.from = from;
        this.to = to;
        checkBounds();
        makeStringReflection();
    }

    private void checkBounds() throws Exception{
        if(from.compareTo(to) > 0){
            throw new Exception("Incorrect bounds of range");
        }
    }

    private void makeStringReflection(){
        if(from instanceof LocalDate){
            fromStr = DTF.format((LocalDate)from);
            toStr = DTF.format((LocalDate)to);
        } else {
            fromStr = from.toString();
            toStr = to.toString();
        }
    }

    public Range(T point) throws Exception{
        this(point, point);
    }

    public boolean includes(T point) {
        if (from instanceof String) {
            return ((String) from).equalsIgnoreCase((String)point);
        } else {
            return (from.compareTo(point) <= 0 && to.compareTo(point) >= 0);
        }
    }

    @Override
    public String toString(){
        if(from.equals(to)){
            return fromStr;
        } else {
            return "From " + fromStr + " to " + toStr;
        }
    }
}
