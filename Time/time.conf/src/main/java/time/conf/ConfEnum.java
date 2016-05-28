package time.conf;

public enum ConfEnum {
    MERGER("${TIME_HOME}/conf/merger.yml"),
    WIKICRAWL("${TIME_HOME}/conf/wiki.crawl.yml"),
    WIKIWEB("${TIME_HOME}/conf/wiki.web.yml"),
    LINKS("${TIME_HOME}/conf/queueLinks.yml"),
    LOCAL_TIKA("${TIME_HOME}/conf/local.tika.yml"),
    APPEND("${TIME_HOME}/conf/append.yml");

    ConfEnum(final String path){
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
