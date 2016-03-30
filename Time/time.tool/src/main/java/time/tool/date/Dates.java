package time.tool.date;

import java.time.LocalDate;
import java.time.Month;

import org.joda.time.Months;

public class Dates {
    public static final long seventiesInDays = 719528L;
    public static final int JCToNowYears = 2000;
    public static long toDays(final LocalDate localDate){
    	return localDate.toEpochDay() + seventiesInDays;
    }
    public static long milliardsToDays(double milliards){
    	return -(long) (milliards * 364250000000d);
    }
    public static long millionsToDays(double millions){
    	return -(long) (millions * 364250000d);
    }
	public static long yearsToDays(final double doubleValue) {
		//on traite des années
		if(doubleValue < 999999d){
			return toDays(LocalDate.of((int)doubleValue, Month.JANUARY, 1));
		}else{
		//on considère qu'on a déjà un jour
			return (long)doubleValue;				
		}
	}
}
