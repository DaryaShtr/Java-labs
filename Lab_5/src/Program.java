public class Program {

    public static void main(String[] args){
        try{
            ProcessorHTML pHTML = new ProcessorHTML();
            pHTML.readHTMLFile("src\\input1.html");
            pHTML.findText("src\\input2.in");
            pHTML.writeTags("src\\output1.out");
            pHTML.writePages("src\\output2.out");
            pHTML.writeNotFoundText("src\\output3.out");
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
