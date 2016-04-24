package time.web.bean;

import java.util.List;

import time.repo.bean.DatedPhrase;

public class Phrases {

    private List<DatedPhrase> phraseList;
    
    private String lastKey;
    
    private long total;
    
    private String[] alternatives;

    public String[] getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(String[] alternatives) {
        this.alternatives = alternatives;
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

    public List<DatedPhrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<DatedPhrase> phrases) {
        this.phraseList = phrases;
    }
    
}
