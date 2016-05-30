package time.append;

public class AppendDone {
    private Append append;
    private String indexDir;
    private long phraseCount;
    private boolean overwriteOccurs;

    public AppendDone(Append append) {
        this.append = append;
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
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
        return "AppendDone{" +
                "append=" + append +
                ", indexDir='" + indexDir + '\'' +
                ", phraseCount=" + phraseCount +
                ", overwriteOccurs=" + overwriteOccurs +
                '}';
    }
}
