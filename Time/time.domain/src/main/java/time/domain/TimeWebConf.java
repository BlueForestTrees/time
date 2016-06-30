package time.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeWebConf {

    private String indexDir;
    private int port;
    private int searchPhrasePageSize;

    public int getSearchPhrasePageSize() {
        return searchPhrasePageSize;
    }

    public int getPort() {
        return port;
    }

    public String getIndexDir() {
        return indexDir;
    }
}
