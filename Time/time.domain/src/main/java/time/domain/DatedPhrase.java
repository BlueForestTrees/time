package time.domain;

/**
 * La date est exprimée en nombres de jours par rapport à J.C.
 * @author slimane
 *
 */
public class DatedPhrase {
    private String text;
    private long date;
    private String url;

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
        return "Phrase [url=" + url + " date=" + date + ", text=" + text + ",]";
    }

}
