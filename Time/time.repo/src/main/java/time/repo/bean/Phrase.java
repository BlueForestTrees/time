package time.repo.bean;

public class Phrase {
    private String text;
    //nb de jours / JC
    private long date;
    private String pageUrl;

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
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
        return "Phrase [pageUrl=" + pageUrl + " date=" + date + ", text=" + text + ",]";
    }

}
