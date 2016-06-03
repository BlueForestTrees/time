package time.domain;

public class IndexCreation {
    private String sourceIndexDir;
    private String destIndexDir;
    private long phraseCount;
    private boolean overwriteOccurs;

    public void setDestIndexDir(String destIndexDir) { this.destIndexDir = destIndexDir;}

    public String getDestIndexDir() { return destIndexDir; }

    public String getSourceIndexDir() {
        return sourceIndexDir;
    }

    public void setSourceIndexDir(String sourceIndexDir) {
        this.sourceIndexDir = sourceIndexDir;
    }

    public long getPhraseCount() {
        return phraseCount;
    }

    public void setPhraseCount(long phraseCount) {
        this.phraseCount = phraseCount;
    }

    public boolean isOverwriteOccurs() {
        return overwriteOccurs;
    }

    public void setOverwriteOccurs(boolean overwriteOccurs) {
        this.overwriteOccurs = overwriteOccurs;
    }

    @Override
    public String toString() {
        return "IndexCreation{" +
                "sourceIndexDir='" + sourceIndexDir + '\'' +
                ", phraseCount=" + phraseCount +
                ", overwriteOccurs=" + overwriteOccurs +
                ", destIndexDir='" + destIndexDir + '\'' +
                '}';
    }
}
