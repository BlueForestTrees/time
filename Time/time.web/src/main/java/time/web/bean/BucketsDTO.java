package time.web.bean;

import java.util.List;

import time.web.enums.Scale;

public class BucketsDTO {
	
	private Scale scale;
	public Scale getScale() {
		return scale;
	}
	public void setScale(Scale scale) {
		this.scale = scale;
	}

	//les buckets enfants
	private List<BucketDTO> subbuckets;
	public List<BucketDTO> getSubbuckets() {
		return subbuckets;
	}
	public void setSubbuckets(List<BucketDTO> subbuckets) {
		this.subbuckets = subbuckets;
	}
	
	
	//le bucket parent
	private Long parentBucket;
	public void setParentBucket(Long parentBucket) {
		this.parentBucket = parentBucket;
	}
	public Long getParentBucket() {
		return parentBucket;
	}

}
