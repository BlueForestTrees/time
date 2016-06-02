package time.conf;

public enum ConfEnum {
    INDEX_MANAGE("${TIME_HOME}/conf/index.manage.yml"),
    WIKICRAWL("${TIME_HOME}/conf/wiki.crawl.yml"),
    TIMEWEB("${TIME_HOME}/conf/wiki.web.yml"),
    LINKS("${TIME_HOME}/conf/queueLinks.yml"),
    LOCAL_TIKA("${TIME_HOME}/conf/local.tika.yml"),
    APPEND("${TIME_HOME}/conf/append.yml"),
    ANALYSER("${TIME_HOME}/conf/analyser.yml");

    ConfEnum(final String path){
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
