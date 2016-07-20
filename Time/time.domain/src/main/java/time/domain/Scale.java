package time.domain;

/**
 * Gère les ratios des buckets
 */
public class Scale {
    public static final Long[] SCALES = new Long[]{10000000000L, 10000L, 500L, 10L};
    public static final long BAR_LENGTH = 1000;

    /**
     * Détermine un scale à partir d'un totalDays et d'une BAR_LENGTH
     * @param totalDays l'amplitude à afficher
     * @return un scale de la forme nom de la dimension lucene.
     */
    public static String get(double totalDays) {
        for (int i = SCALES.length - 1; i >= 0; i--) {
            final double daysPerPixels = totalDays / BAR_LENGTH;
            final double daysPerBucket = SCALES[i];
            final double ratio = daysPerPixels / daysPerBucket;
            if (ratio < 1) {
                return String.valueOf(i);
            }
        }
        //si rien trouvé le plus grand. n'arrive pas sauf si totalDays > SCALES[0] * BAR_LENGTH. . .
        return "0";
    }
}
