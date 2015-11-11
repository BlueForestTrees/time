package time.web.bean;

import java.util.List;

import time.repo.bean.Phrase;

public class Phrases {

    private List<Phrase> phraseList;
    
    private String lastKey;

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
