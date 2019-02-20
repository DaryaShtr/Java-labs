public class Tag implements Comparable<Tag>{
    private String tagName;
    private int length;


    public Tag(String tagName){
        this.tagName = tagName;
        this.length = tagName.length();
    }

    public String getTagName() {
        return tagName;
    }

    public static Tag parseTag(String s){
        return new Tag(s);
    }

    @Override
    public int compareTo(Tag o){
        if(length != o.length){
            return length - o.length;
        } else{
            return getTagName().compareToIgnoreCase(o.getTagName());
        }
    }

    @Override
    public String toString(){
        return "<" + tagName + ">";
    }
}
