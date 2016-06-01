package time.domain;

public class Append {
    private String source;
    private String url;
    private String title;
    private String appendToDir;

    public String getAppendToDir() { return appendToDir; }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAppendToDir(String appendToDir) {
        this.appendToDir = appendToDir;
    }

    @Override
    public String toString() {
        return "Append{" +
                "source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
