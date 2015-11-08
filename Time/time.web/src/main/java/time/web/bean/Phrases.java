package time.web.bean;

import java.util.List;

import time.repo.bean.Phrase;
import time.repo.bean.ScoreDocDTO;

public class Phrases {

    private List<Phrase> phraseList;

    private ScoreDocDTO lastScoreDoc;
    
    public ScoreDocDTO getLastScoreDoc() {
        return lastScoreDoc;
    }

    public void setLastScoreDoc(ScoreDocDTO lastScoreDoc) {
        this.lastScoreDoc = lastScoreDoc;
    }

    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<Phrase> phrases) {
        this.phraseList = phrases;
    }
    
}
