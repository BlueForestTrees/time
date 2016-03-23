package time.web.bean;

import java.util.Comparator;

public class Period{
	public static final  Comparator<Period> byDate = (p1, p2) -> Long.compare(p1.getDate(), p2.getDate());
	private Long date;
	private Short offset;
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public Short getOffset() {
		return offset;
	}
	public void setOffset(Short offset) {
		this.offset = offset;
	}
	public Long getFirst(){
		return date + (offset*date/100);
	}
	public Long getLast(){
		return date - (offset*date/100);
	}
	public Period(){
		
	}
	protected Period(Long date, Short offset) {
		super();
		this.date = date;
		this.offset = offset;
	}
}
