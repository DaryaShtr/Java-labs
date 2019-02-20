import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tags {
    public static List<Tag> asList(String[] array){
        List<Tag> list = new LinkedList<>();
        Arrays.stream(array).forEach(str -> list.add(Tag.parseTag(str)));
        return list;
    }
}
