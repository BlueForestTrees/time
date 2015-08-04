package wiki.tool.chrono;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class Chrono {

	private String name;
	public String getName() {
		return name;
	}

	private DateTime start;
	private DateTime stop;

	public Chrono(String name) {
		super();
		this.name = name;
	}

	public void start() {
		start = DateTime.now();
	}

	public void stop() {
		stop = DateTime.now();
	}

	public String toStringFromDuration(Duration duration) {
		Period period = duration.toPeriod();

		PeriodFormatterBuilder builder = new PeriodFormatterBuilder().printZeroAlways();

		if(period.getYears() > 0){
			builder.appendYears().appendSeparator(" an, ");
		}
		if(period.getMonths() > 0){
			builder.appendMonths().appendSeparator(" mois, ");
		}
		if(period.getDays() > 0){
			builder.appendDays().appendSeparator(" jours, ");
		}
		if (period.getHours() > 0) {
			builder.appendHours().appendSeparator("h:");
		}

		if (period.getMinutes() > 0 || period.getHours() > 0) {
			builder.appendMinutes().appendSeparator("m:");
		}
		
		if (period.getSeconds() > 0) {
			builder.appendSeconds().appendSeparator("s:");
		}

		PeriodFormatter minutesAndSeconds = builder.appendMillis3Digit().appendLiteral("ms").toFormatter();

		return minutesAndSeconds.print(period);
	}

	public Duration getDuration() {
		return Duration.millis(stop.getMillis() - start.getMillis());
	}

	public Duration dividedBy(Long dividand) {
		return getDuration().dividedBy(dividand);
	}

	public String toStringDividedBy(Long dividand) {
		return toStringFromDuration(dividedBy(dividand));
	}

	@Override
	public String toString() {
		Duration duration = getDuration();
		return toStringFromDuration(duration);
	}

	public void withStart(DateTime start) {
		this.start = start;
	}
	public void withStop(DateTime stop) {
		this.stop = stop;
	}
	
	public String getRemaining(long done, long total){
		float progressRatio = (float)total / (float)done;
		stop();
		Duration progressDuration = getDuration();
		Duration totalDuration = new Duration((long)(progressDuration.getMillis() * progressRatio));
		return toStringFromDuration(totalDuration.minus(progressDuration));
	}
	
}
