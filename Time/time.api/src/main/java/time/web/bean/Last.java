package time.web.bean;

import org.apache.lucene.search.FieldDoc;

/**
 * Contient les datas requises pour faire du 'serachAfter', qui requiert: 1-
 * Qu'on lui repasse le FieldDoc 2- On a besoin du lastIndex pour savoir si on
 * est au bout de la pagination.
 * 
 * @author slim
 *
 */
public class Last {
    private final FieldDoc doc;
    private final int lastIndex;

    public Last(FieldDoc doc, int lastIndex) {
        super();
        this.doc = doc;
        this.lastIndex = lastIndex;
    }

    public FieldDoc getDoc() {
        return doc;
    }

    public int getLastIndex() {
        return lastIndex;
    }
}
