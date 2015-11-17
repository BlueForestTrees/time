package time.transformer.tool.parser;

import java.util.regex.Matcher;

public class RomanParser implements IParser {

    @Override
    public Long from(final Matcher matcher) {
        final String romanUp = matcher.group("g").toUpperCase();

        int i = 0;
        Long arabic = 0L;

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

        return (arabic - 1) * 36425;

    }

    private static int letterToNumber(char letter) {
        switch (letter) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new NumberFormatException("Illegal character \"" + letter + "\" in Roman numeral");
        }
    }

}
