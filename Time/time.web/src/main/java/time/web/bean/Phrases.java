package time.web.bean;

import java.util.List;

import time.repo.bean.Phrase;

public class Phrases {

    private List<Phrase> phraseList;
    private Integer doc;
    private Float score;
    private Integer lastIndex;

    public Integer getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(Integer lastIndex) {
        this.lastIndex = lastIndex;
    }

    public Integer getDoc() {
        return doc;
    }

    public void setDoc(Integer doc) {
        this.doc = doc;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<Phrase> phrases) {
        this.phraseList = phrases;
    }
    
}
