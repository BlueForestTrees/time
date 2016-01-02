package time.web.service;

import java.util.Arrays;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class QueryService {
    public Query getQuery(final String term, final String scale, Long bucketValueFrom, Long bucketValueTo) {
        boolean noBucket = StringUtils.isEmpty(scale) || (bucketValueFrom == null && bucketValueTo == null);
        boolean noTerm = StringUtils.isEmpty(term);

        if (noBucket && noTerm) {
            return new MatchAllDocsQuery();
        }

        Query textQuery = noTerm ? null : getOrTermQuery(term.toLowerCase());
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(scale, bucketValueFrom, bucketValueTo, true, true);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if (textQuery != null) {
            builder.add(textQuery, Occur.MUST);
        }
        if (bucketQuery != null) {
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

    protected Query getOrTermQuery(String term) {
        boolean hasOrs = term.contains(" ");
        if(!hasOrs){
            return getAndTermQuery(term);
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split(" ")).forEach(t -> builder.add(getAndTermQuery(t), Occur.SHOULD));
            return builder.build();
        }
    }

    protected Query getAndTermQuery(String term) {
        boolean hasAnds = term.contains("+");
        if(!hasAnds){
            return new TermQuery(new Term("text", term));
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split("\\+")).forEach(t -> builder.add(new TermQuery(new Term("text", t)), Occur.MUST));
            return builder.build();
        }
    }
}
