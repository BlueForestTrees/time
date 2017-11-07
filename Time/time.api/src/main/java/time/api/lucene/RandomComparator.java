package time.api.lucene;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.LeafFieldComparator;
import org.apache.lucene.search.Scorer;

import java.io.IOException;
import java.util.Random;

/**
 * Pour obtenir les résultats mélangés.
 */
public class RandomComparator extends FieldComparator<Integer> implements LeafFieldComparator{

    private final Random random = new Random();

    @Override
    public int compare(int slot1, int slot2) {
        return random.nextInt();
    }

    @Override
    public Integer value(int slot) {
        return random.nextInt();
    }

    @Override
    public void setTopValue(Integer value) {
        //rien à faire. . .
    }

    @Override
    public LeafFieldComparator getLeafComparator(LeafReaderContext context) throws IOException {
        return this;
    }

    @Override
    public void setBottom(int slot) {
        //rien normal
    }

    @Override
    public int compareBottom(int doc) throws IOException {
        return random.nextInt();
    }

    @Override
    public int compareTop(int doc) throws IOException {
        return random.nextInt();
    }

    @Override
    public void copy(int slot, int doc) throws IOException {

    }

    @Override
    public void setScorer(Scorer scorer) {

    }
}