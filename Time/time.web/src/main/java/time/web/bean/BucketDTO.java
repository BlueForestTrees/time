package time.web.bean;

/**
 * Dans ce [bucket], il y a [count] phrases.
 * @author slim
 *
 */
public class BucketDTO {
	private long bucket;
	private long count;

	public long getBucket() {
		return bucket;
	}
	public void setBucket(long bucket) {
		this.bucket = bucket;
	}
	
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
