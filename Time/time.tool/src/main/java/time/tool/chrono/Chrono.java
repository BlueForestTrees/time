package time.tool.chrono;

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
		start();
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
			builder.appendYears().appendLiteral(" an").appendSeparator(", ");
		}
		if(period.getMonths() > 0){
			builder.appendMonths().appendLiteral(" mois").appendSeparator(", ");
		}
		if(period.getDays() > 0){
			builder.appendDays().appendLiteral(" jours").appendSeparator(", ");
		}
		if (period.getHours() > 0) {
			builder.appendHours().appendLiteral("h").appendSeparator(":");
		}

		if (period.getMinutes() > 0 || period.getHours() > 0) {
			builder.appendMinutes().appendLiteral("m").appendSeparator(":");
		}
		
		if (period.getSeconds() > 0) {
			builder.appendSeconds().appendLiteral("s").appendSeparator(":");
		}
		
		if(period.getMillis() > 0){
			builder.appendMillis3Digit().appendLiteral("ms");
		}

		PeriodFormatter minutesAndSeconds = builder.toFormatter();

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
