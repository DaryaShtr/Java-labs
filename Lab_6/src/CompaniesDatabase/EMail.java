package CompaniesDatabase;

public class EMail {
    String name;
    String domainName;
    boolean isNA;

    public EMail(String mail) {
        try{
            check(mail);
            name = mail.substring(0, mail.indexOf('@'));
            domainName = mail.substring(mail.indexOf('@') + 1, mail.length());
        } catch (Exception e){
            isNA = true;
        }
    }

    private void check(String mail) throws Exception{
        if(!mail.matches("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}")){
            throw new Exception("Incorrect email address");
        }
    }

    @Override
    public String toString() {
        if(isNA){
            return "";
        } else{
            return name + "@" + domainName;
        }
    }
}
