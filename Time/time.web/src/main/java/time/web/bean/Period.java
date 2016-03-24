package time.web.bean;

import java.util.Comparator;

public class Period{
	public static final  Comparator<Period> byDate = (p1, p2) -> Long.compare(p1.getDate(), p2.getDate());
	private long date;
	private int offset;
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public long getFrom(){
		return date - (date*offset/100);
	}
	public long getTo(){
		return date + (date*offset/100);
	}
	public Period(){
		
	}
	protected Period(long date, int offset) {
		super();
		this.date = date;
		this.offset = offset;
	}
}
