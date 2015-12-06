package time.transformer.tool.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

public class RomanParser implements IParser {

    @Override
    public long from(final Matcher matcher) {
        final String romanUp = matcher.group("g").toUpperCase();

        int i = 0;
        int arabic = 0;

        while (i < romanUp.length()) {
            char letter = romanUp.charAt(i);
            int number = letterToNumber(letter);
            i++;
            if (i == romanUp.length()) {
                arabic += number;
            } else {
                int nextNumber = letterToNumber(romanUp.charAt(i));
                if (nextNumber > number) {
                    arabic += (nextNumber - number);
                    i++;
                } else {
                    arabic += number;
                }
            }
        }
        final int annee = (arabic - 1) * 100;
        return LocalDate.of(annee, Month.JANUARY, 1).toEpochDay() + seventiesInDays;
    }

    private static int letterToNumber(char letter) {
        switch (letter) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            default:
                throw new NumberFormatException("Illegal character \"" + letter + "\" in Roman numeral");
        }
    }

}
