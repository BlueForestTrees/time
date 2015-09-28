package time.web.bean;

/**
 * A cette [date], il y a [count] references.
 * @author slim
 *
 */
public class FacetDTO {
	private long date;
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	private long count;
	
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
