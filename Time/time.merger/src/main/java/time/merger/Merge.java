package time.merger;

public class Merge {
    private String mergeableIndexesDir;
    private String mergedIndexDir;

    public String getMergedIndexDir() {
        return mergedIndexDir;
    }

    public void setMergedIndexDir(String mergedIndexDir) {
        this.mergedIndexDir = mergedIndexDir;
    }

    public String getMergeableIndexesDir() {
        return mergeableIndexesDir;
    }

    public void setMergeableIndexesDir(String mergeableIndexesDir) {
        this.mergeableIndexesDir = mergeableIndexesDir;
    }

    @Override
    public String toString() {
        return "Merge{" +
                "mergeableIndexesDir='" + mergeableIndexesDir + '\'' +
                ", mergedIndexDir='" + mergedIndexDir + '\'' +
                '}';
    }
}
