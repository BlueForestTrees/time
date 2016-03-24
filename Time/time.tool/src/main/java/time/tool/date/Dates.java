package time.tool.date;

import java.time.LocalDate;

public class Dates {
    public static final long seventiesInDays = 719528L;
    public static final int JCToNowYears = 2000;
    public static long toDays(final LocalDate localDate){
    	return localDate.toEpochDay() + seventiesInDays;
    }
    public static long milliardsToDays(double milliards){
    	return (long) milliards * 364250000000L;
    }
    public static long millionsToDays(double millions){
    	return (long) millions * 364250000L;
    }
}
