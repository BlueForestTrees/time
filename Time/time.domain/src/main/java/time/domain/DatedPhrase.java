package time.domain;

import org.apache.lucene.document.Document;

/**
 * La date est exprimée en nombres de jours par rapport à J.C.
 * @author slimane
 *
 */
public class DatedPhrase {
    private String text;
    private long date;
    private String url;
    private Metadata.Type type;
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Metadata.Type getType() {
        return type;
    }

    public void setType(Metadata.Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DatedPhrase{" +
                "text='" + text + '\'' +
                ", date=" + date +
                ", url='" + url + '\'' +
                ", type=" + type +
                '}';
    }
}
