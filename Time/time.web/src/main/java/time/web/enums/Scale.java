package time.web.enums;

/**
 * Détermine la résolution de la requête facets.
 * 
 * @author slim
 *
 */
public enum Scale {
    ONE("date", 1L), TEN("dateByTen", 10L), TEN3("dateByTen3", 10000L), TEN6("dateByTen6", 10000000L), TEN9("dateByTen9", 10000000000L);

    private final String field;
    private final Long multiplier;

    private Scale(String field, Long multiplier) {
        this.field = field;
        this.multiplier = multiplier;
    }

    public String getField() {
        return field;
    }

    public Long getMultiplier() {
        return multiplier;
    }

    /**
     * Fournit l'échelle de niveau supérieur.
     * 
     * @return
     */
    public Scale getParent() {
        switch (this) {
            case ONE:
                return TEN;
            case TEN:
                return TEN3;
            case TEN3:
                return TEN6;
            case TEN6:
                return TEN9;
            default:
                return null;
        }
    }
}
