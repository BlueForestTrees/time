package time.web.bean;

import org.apache.lucene.document.Document;
import time.domain.Metadata;
import time.tool.reference.Fields;

public class LucenePhrase {

    private Document doc;

    private LucenePhrase(Document doc) {
        this.doc = doc;
    }

    public static LucenePhrase with(final Document doc){
        return new LucenePhrase(doc);
    }

    public String author(){
        return doc.get(Fields.AUTHOR);
    }
    public String title(){
        return doc.get(Fields.TITLE);
    }
    public long date(){
        return (long) doc.getField(Fields.DATE).numericValue();
    }
    public String url(){
        return doc.get(Fields.URL);
    }
    public Metadata.Type type(){
        if(typeString() == null){
            return null;
        }else {
            return Metadata.Type.valueOf(typeString());
        }
    }

    private String typeString() {
        return doc.getField(Fields.TYPE).stringValue();
    }

    public String text(){
        return doc.get(Fields.TEXT);
    }
}
