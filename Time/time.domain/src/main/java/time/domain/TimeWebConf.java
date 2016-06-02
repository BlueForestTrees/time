package time.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeWebConf {

    private String indexDir;

    public String getIndexDir() {
        return indexDir;
    }
}
