import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ReverseText {
    private List<StringBuilder> text;

    public ReverseText(){
        text = new LinkedList<>();
    }

    public void input(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input text");
        do{
            text.add(new StringBuilder(sc.nextLine()));
        }
        while(text.get(text.size() - 1).length() != 0);
    }

    public void reverse(){
        text.forEach(str -> str.reverse());
    }

    public void print(){
        text.stream().forEach(str -> System.out.println(str));
    }
}