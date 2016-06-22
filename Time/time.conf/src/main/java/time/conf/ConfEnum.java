package time.conf;

public enum ConfEnum {
    INDEX_MANAGE("${TIME_CONF_HOME}/index.manage.yml"),
    WIKICRAWL("${TIME_CONF_HOME}/wiki.crawl.yml"),
    TIMEWEB("${TIME_CONF_HOME}/wiki.web.yml"),
    LINKS("${TIME_CONF_HOME}/queueLinks.yml"),
    LOCAL_TIKA("${TIME_CONF_HOME}/local.tika.yml"),
    META_TO_INDEX("${TIME_CONF_HOME}/meta.to.index.yml"),
    ANALYSER("${TIME_CONF_HOME}/analyser.yml"),
    TRANSFORMER("${TIME_CONF_HOME}/transformer.yml"),
    LIVEPARSE("${TIME_CONF_HOME}/liveparse.yml");

    ConfEnum(final String path){
        this.path = path;
    }

    private String path;

    public String getPath() {
        return path;
    }
}
