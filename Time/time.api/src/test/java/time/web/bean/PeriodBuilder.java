package time.api.bean;

public class PeriodBuilder {
	
	private Period period;
	
	private PeriodBuilder(){
		period = new Period();
	}
	
	public Period get(){
		return period;
	}
	
	public static PeriodBuilder with(){
		return new PeriodBuilder();
	}
	
	public PeriodBuilder date(long date){
		period.setDate(date);
		return this;
	}
	
	public PeriodBuilder offset(int offset){
		period.setOffset(offset);
		return this;
	}
	
	public PeriodBuilder minMode(boolean minMode){
		period.setMinMode(minMode);
		return this;
	}
	
	public PeriodBuilder maxMode(boolean maxMode){
		period.setMaxMode(maxMode);
		return this;
	}
	public PeriodBuilder percentMargin(int percentMargin){
		period.setPercentMargin(percentMargin);
		return this;
	}
}
