package time.api.bean;

/**
 * Dans ce [bucket], il y a [count] phrases.
 * 
 * @author slim
 *
 */
public class Bucket {
    private long bucket;
    private int count;

    public long getBucket() {
        return bucket;
    }

    public void setBucket(long bucket) {
        this.bucket = bucket;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Bucket [bucket=" + bucket + ", count=" + count + "]";
    }
    
    
    
}
