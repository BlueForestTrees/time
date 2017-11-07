package time.api.bean;

import java.util.Comparator;

public class Period{
	public static final  Comparator<Period> byDate = (p1, p2) -> Long.compare(p1.getDate(), p2.getDate());
	public static final int YEAR_OFFSET = 364;
	private long date;
	private int percentMargin;
	private long offset;
	private boolean minMode;
	private boolean maxMode;
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getPercentMargin() {
		return percentMargin;
	}
	public void setPercentMargin(int percentMargin) {
		this.percentMargin = percentMargin;
	}
	public long getFrom(){
		if(maxMode){
			return Long.MIN_VALUE;
		}else if(percentMargin != 0){
			return date - (Math.abs(date*percentMargin/100));
		}else {
			return date;
		}
	}
	public long getTo(){
		if(minMode){
			return Long.MAX_VALUE;
		}else if(percentMargin != 0){
			return date + (Math.abs(date*percentMargin/100));
		}else if(offset != 0){
			return date + offset;
		}else{
			return date;
		}
	}
	public Period(){
		
	}
	public void setPercentMarginWithoutOverride(int percentMargin) {
		if (this.percentMargin == 0) {
			this.percentMargin = percentMargin;
		}
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public boolean isMinMode() {
		return minMode;
	}
	public void setMinMode(boolean minMode) {
		this.minMode = minMode;
	}
	public boolean isMaxMode() {
		return maxMode;
	}
	public void setMaxMode(boolean maxMode) {
		this.maxMode = maxMode;
	}
}
