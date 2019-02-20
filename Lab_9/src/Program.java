public class Program {
    public static void main(String[] args) {
        try{
            StringProcessor sp = new StringProcessor();
            sp.read("src\\Input.txt");
            sp.removeParenthesis();
            sp.removeExtraNumber();
            sp.removeLeadingZeros();
            sp.write("src\\Output.txt");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
