package time.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeWebConf {

    private String indexDir;
    private int port;

    public int getPort() {
        return port;
    }

    public String getIndexDir() {
        return indexDir;
    }
}
