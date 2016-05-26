package time.conf;

public enum Confs {
    WIKICRAWL("${TIME_HOME}/conf/wiki.yml"),
    MERGER("${TIME_HOME}/conf/merger.yml"),
    WIKIWEB("${TIME_HOME}/conf/wiki.web.yml"),
    LINKS("${TIME_HOME}/conf/queueLinks.yml");

    Confs(final String path){
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
