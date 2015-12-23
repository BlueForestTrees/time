package time.web.bean;

import java.util.List;

public class BucketGroup {

    private String scale;
    private List<Bucket> buckets;

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    @Override
    public String toString() {
        return "Buckets [scale=" + scale + ", buckets=" + buckets + "]";
    }

}
