package time.domain;

public class Meta {
    private String metaPath;

    public Meta() {
    }

    public String getMetaPath() {
        return metaPath;
    }

    public Meta(String metaPath) {
        this.metaPath = metaPath;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "metaPath='" + metaPath + '\'' +
                '}';
    }
}
