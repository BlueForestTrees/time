package time.transformer.tool.parser;

public class RomanParser implements IParser {

    private static final int[] numbers = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
    private static final String[] letters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

    @Override
    public String to(final long nbJours) {
        long nbSiecles = 1 + nbJours / 36425;
        String result = "";
        for (int i = 0; i < numbers.length; i++) {
            while (nbSiecles >= numbers[i]) {
                result += letters[i];
                nbSiecles -= numbers[i];
            }
        }
        return "au " + result + "e siècle";

    }

    @Override
    public Long from(final String roman) {
        final String romanUp = roman.toUpperCase();

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

    private int letterToNumber(char letter) {
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
