package time.merger;

public class Merge {
    private String mergeableIndexesDir;
    private String mergedIndexesDir;

    public String getMergedIndexesDir() {
        return mergedIndexesDir;
    }

    public void setMergedIndexesDir(String mergedIndexesDir) {
        this.mergedIndexesDir = mergedIndexesDir;
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
                ", mergedIndexesDir='" + mergedIndexesDir + '\'' +
                '}';
    }
}
