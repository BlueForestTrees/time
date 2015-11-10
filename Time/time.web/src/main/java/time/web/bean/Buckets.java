package time.web.bean;

import java.util.List;

import time.web.enums.Scale;

public class Buckets {

    private Scale scale;
    // le bucket parent
    private Long parentBucket;
    // les buckets enfants
    private List<Bucket> subbuckets;

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public List<Bucket> getSubbuckets() {
        return subbuckets;
    }

    public void setSubbuckets(List<Bucket> subbuckets) {
        this.subbuckets = subbuckets;
    }

    public void setParentBucket(Long parentBucket) {
        this.parentBucket = parentBucket;
    }

    public Long getParentBucket() {
        return parentBucket;
    }

    @Override
    public String toString() {
        return "Buckets [scale=" + scale + ", parentBucket=" + parentBucket + ", subbuckets=" + subbuckets + "]";
    }

}
