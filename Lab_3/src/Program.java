public class Program {
    public static void main(String[] args){
        try{
            ReverseText rt = new ReverseText();
            rt.input();
            rt.reverse();
            rt.print();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}