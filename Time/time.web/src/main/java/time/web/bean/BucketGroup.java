package time.web.bean;

import java.util.List;

import time.web.enums.Scale;

public class BucketGroup {

    private Scale scale;
    private List<Bucket> buckets;

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
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
