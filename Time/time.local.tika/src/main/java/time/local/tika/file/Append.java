package time.local.tika.file;

public class Append {
    private String source;

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    private String url;
    private String title;

    @Override
    public String toString() {
        return "Append{" +
                "source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
