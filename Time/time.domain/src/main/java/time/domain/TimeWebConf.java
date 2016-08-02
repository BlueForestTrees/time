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

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSearchPhrasePageSize(int searchPhrasePageSize) {
        this.searchPhrasePageSize = searchPhrasePageSize;
    }

    @Override
    public String toString() {
        return "TimeWebConf{" +
                "indexDir='" + indexDir + '\'' +
                ", port=" + port +
                ", searchPhrasePageSize=" + searchPhrasePageSize +
                '}';
    }
}
