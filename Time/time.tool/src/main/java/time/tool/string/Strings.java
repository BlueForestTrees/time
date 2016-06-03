package time.tool.string;

import java.util.Arrays;

public class Strings {
    public static String withSlash(final String begin) {
        return with(begin, "/");
    }

    public static String withPipe(final String begin) {
        return with(begin, "|");
    }

    public static String with(String begin, final String end) {
        if (begin == null) {
            return "";
        } else if (begin.endsWith(end)) {
            return begin;
        } else {
            return begin + end;
        }
    }

    public static String beginWith(final String end, final String... begins) {
        if (Arrays.stream(begins).anyMatch(begin -> end.startsWith(begin))) {
            return end;
        } else {
            return begins[0] + end;
        }
    }

    public static String bold(final String phrase, final String dateExtract) {
        return phrase.replace(dateExtract, "<b>" + dateExtract + "</b>");
    }

    public static String clean(final String phrase) {
        return phrase.replaceAll("\\[.*?\\]", "");
    }

    public static String noReturns(final String s) {
        return s.replaceAll("\r\n", "\\air\\haine");
    }

    public static String forFilename(final String s) {
        return s.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    public static String firstValued(final String... ss) {
        return Arrays.stream(ss).filter((s) -> s != null && !"".equals(s)).findFirst().get();
    }

    public static String without(final String metaPath, final String endToRemove) {
        if (metaPath.endsWith(endToRemove)) {
            return metaPath.substring(0, metaPath.length() - endToRemove.length());
        } else {
            return metaPath;
        }
    }
}
