package CompaniesDatabase;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class Company {

    private class Title {
        String name;
        String shortTitle;


        Title(String name, String shortTitle) {
            this.name = name;
            this.shortTitle = shortTitle;
        }

        @Override
        public String toString(){
            return "Name : " + name + "\nShort Title : " + shortTitle;
        }
    }

    private class SignificantDates {
        LocalDate dateUpdate;
        LocalDate dateFoundation;


        SignificantDates(LocalDate dateUpdate, LocalDate dateFoundation) {
            this.dateUpdate = dateUpdate;
            this.dateFoundation = dateFoundation;
        }

        @Override
        public String toString(){
            return "Date Update : " + DTF.format(dateUpdate) + "\nDate Foundation : " + DTF.format(dateFoundation);
        }
    }

    private class Contacts {
        String address;
        String phone;
        EMail email;
        String internetAddressOrLink;


        Contacts(String address, String phone, EMail email, String internetAddressOrLink) {
            this.address = address;
            this.phone = phone;
            this.email = email;
            this.internetAddressOrLink = internetAddressOrLink;
        }

        @Override
        public String toString(){
            return "Address : " + address + "\nPhone : " + phone + "\nEMail : " + email + "\nInternet Address : " + internetAddressOrLink;
        }
    }

    private class BusinessField {
        String branch;
        String activity;


        BusinessField(String branch, String activity) {
            this.branch = branch;
            this.activity = activity;
        }

        @Override
        public String toString(){
            return "Branch : " + branch + "\nActivity : " + activity;
        }
    }

    private class OtherInformation {
        int countEmployees;
        long auditor;


        OtherInformation(int countEmployees, long auditor) {
            this.countEmployees = countEmployees;
            this.auditor = auditor;
        }

        @Override
        public String toString(){
            return "Count Employees : " + countEmployees + "\nAuditor: " + auditor;
        }
    }


    private Title title;
    private SignificantDates dates;
    private Contacts contacts;
    private BusinessField businessField;
    private OtherInformation otherInfo;

    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public Company(String[] data) throws Exception {
        checkNecessaryFields(data);
        title = new Title(data[0], data[1]);
        dates = new SignificantDates(stringToLocalDate(data[2]), stringToLocalDate(data[4]));
        contacts = new Contacts(data[3], data[7], new EMail(data[8]), data[11]);
        businessField = new BusinessField(data[9], data[10]);
        otherInfo = new OtherInformation(Integer.parseInt(data[5]), Long.parseLong(data[6]));
    }

    private void checkNecessaryFields(String[] data) throws Exception {
        if (Arrays.asList(new String[]{data[1], data[4], data[9], data[5]}).contains("")) {
            throw new Exception("Necessary field isn't filled");
        }
    }

    private LocalDate stringToLocalDate(String str){
        if(isValidDate(str)){
            return LocalDate.parse(str, DTF);
        } else{ return null; }
    }

    private boolean isValidDate(String str){
        try{
            DTF.parse(str);
            return true;
        } catch (Exception e){ return false; }
    }

    public String getName() {
        return title.name;
    }

    public String getShortTitle() {
        return title.shortTitle;
    }

    public LocalDate getDateUpdate() {
        return dates.dateUpdate;
    }

    public String getAddress() {
        return contacts.address;
    }

    public LocalDate getDateFoundation() {
        return dates.dateFoundation;
    }

    public int getCountEmployees() {
        return otherInfo.countEmployees;
    }

    public long getAuditor() {
        return otherInfo.auditor;
    }

    public String getPhone() {
        return contacts.phone;
    }

    public EMail getEmail() {
        return contacts.email;
    }

    public String getBranch() {
        return businessField.branch;
    }

    public String getActivity() {
        return businessField.activity;
    }

    public String getInternetAddressOrLink() {
        return contacts.internetAddressOrLink;
    }

    public static String[] getFieldsNames(){
        return new String[]{"Name", "ShortTitle", "DateUpdate", "Address", "DateFoundation", "CountEmployees",
        "Auditor", "Phone", "Email", "Branch", "Activity", "InternetAddressOrLink"};
    }

    @Override
    public String toString() {
        return title + "\n" + dates + "\n" + contacts + "\n" + businessField + "\n" + otherInfo + "\n";
    }

    public Element toXMLElement() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element companyXML = document.createElement("Company");
            Arrays
                    .asList(Company.class.getDeclaredFields())
                    .forEach(innerClass -> companyXML.appendChild(getXMLInnerClass(document, innerClass)));
            return companyXML;
        } catch (Exception e) { return null; }
    }

    private Element getXMLInnerClass(Document document, Field innerClass) {
        String innerClassName = firstLetterToUpper(innerClass.getName());
        Element innerClassXML = document.createElement(innerClassName);
        Arrays
                .asList(innerClass.getType().getDeclaredFields())
                .forEach(field -> {
                        try{
                            innerClassXML.appendChild(getXMLField(document, field));
                        } catch (Exception e){}
                });
        return innerClassXML;
    }

    private Element getXMLField(Document document, Field field) {
        try {
            Element fieldXML = document.createElement(field.getName());
            fieldXML.setTextContent(getStringFromField(field));
            return fieldXML;
        } catch (Exception e) { return null; }
    }

    private String getStringFromField(Field field) throws Exception{
        String fieldName = firstLetterToUpper(field.getName());
        Object obj = Company.class.getMethod("get" + fieldName).invoke(this);
        if(obj instanceof LocalDate){
            return DTF.format((LocalDate)obj);
        } else {
            return obj.toString();
        }
    }

    public JSONObject toJSONObject(){
        JSONObject companyJSON = new JSONObject();
        Arrays
                .asList(Company.class.getDeclaredFields())
                .forEach(innerClass -> {
                    try{
                        companyJSON.put(innerClass.getName(), getJSONInnerClass(innerClass));
                    } catch (Exception e){}
                });
        return companyJSON;
    }

    private JSONObject getJSONInnerClass(Field innerClass) {
        JSONObject innerClassJSON = new JSONObject();
        Arrays
                .asList(innerClass.getType().getDeclaredFields())
                .forEach(field -> {
                    try{
                        innerClassJSON.put(field.getName(), getStringFromField(field));
                    } catch (Exception e){}
                });
        return innerClassJSON;
    }

    public JSONObject fieldsToJSON(List<String> fields){
        JSONObject companyJSON = new JSONObject();
        fields.forEach(field -> {
            try {
                companyJSON.put(field, Company
                        .class
                        .getDeclaredMethod("get" + field)
                        .invoke(this));
            } catch (Exception e) {}
        });
        return companyJSON;
    }

    public Element fieldsToXML(List<String> fields){
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element companyXML = document.createElement("Company");
            fields.forEach(field -> {
                try {
                    companyXML.appendChild(fieldToXML(document, field));
                } catch (Exception e) {}
            });
            return companyXML;
        } catch (Exception e) { return null; }
    }

    private Element fieldToXML(Document document, String field){
        try {
            Element fieldXML = document.createElement(field);
            fieldXML.setTextContent(Company
                    .class
                    .getDeclaredMethod("get" + field)
                    .invoke(this).toString());
            return fieldXML;
        } catch (Exception e) { return null; }
    }

    private String firstLetterToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}