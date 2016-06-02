package time.domain;

import java.util.List;

public class TextDto {
    private String text;
    private List<DatedPhrase> datedPhrases;
    private Metadata metadata;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DatedPhrase> getDatedPhrases() {
        return datedPhrases;
    }

    public void setDatedPhrases(List<DatedPhrase> datedPhrases) {
        this.datedPhrases = datedPhrases;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
