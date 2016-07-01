package time.tool.date;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

public class Dates {
    public static final long seventiesInDays = 719528L;
    public static final int JCToNowYears = 2000;
    public static long ilyaToDays(int annees){
    	return toDays(LocalDate.of(annees + Dates.JCToNowYears, Month.JANUARY, 1));
    }
    public static long toDays(int annees){
    	return toDays(LocalDate.of(annees, Month.JANUARY, 1));
    }
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
	public static int toYear(final String romanNumber){
    	int i = 0;
        int arabic = 0;
    	while (i < romanNumber.length()) {
             char letter = romanNumber.charAt(i);
             int number = toNumber(letter);
             i++;
             if (i == romanNumber.length()) {
                 arabic += number;
             } else {
                 int nextNumber = toNumber(romanNumber.charAt(i));
                 if (nextNumber > number) {
                     arabic += (nextNumber - number);
                     i++;
                 } else {
                     arabic += number;
                 }
             }
         }
         final int annee = (arabic - 1) * 100;
         return annee;
    }
    
    public static int toNumber(char romanLetter) {
        switch (romanLetter) {
            case 'I':
            case 'i':
                return 1;
            case 'v':
            case 'V':
                return 5;
            case 'x':
            case 'X':
                return 10;
            default:
                throw new NumberFormatException("Illegal romanLetter \"" + romanLetter);
        }
    }

}
