package time.repo.bean;


public class ScoreDocDTO {
    
    private float score;
    private int doc;
    private int shardIndex;
    
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }
    public int getDoc() {
        return doc;
    }
    public void setDoc(int doc) {
        this.doc = doc;
    }
    public int getShardIndex() {
        return shardIndex;
    }
    public void setShardIndex(int shardIndex) {
        this.shardIndex = shardIndex;
    }
}
