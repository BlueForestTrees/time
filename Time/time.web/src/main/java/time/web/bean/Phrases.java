package time.web.bean;

import java.util.List;

import time.repo.bean.Phrase;

public class Phrases {

    private List<Phrase> phraseList;
    
    private String lastKey;
    
    private long total;
    
    private String alternative;

    public String getAlternative() {
        return alternative;
    }

    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<Phrase> phrases) {
        this.phraseList = phrases;
    }
    
}
