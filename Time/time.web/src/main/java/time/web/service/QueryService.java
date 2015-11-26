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
    public Query getQuery(String term, String bucketName, Long bucketValueFrom, Long bucketValueTo) {
        boolean noBucket = StringUtils.isEmpty(bucketName) || (bucketValueFrom == null && bucketValueTo == null);
        boolean noTerm = StringUtils.isEmpty(term);

        if (noBucket && noTerm) {
            return new MatchAllDocsQuery();
        }

        Query textQuery = noTerm ? null : getTermQuery(term);
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(bucketName, bucketValueFrom, bucketValueTo, true, true);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if (textQuery != null) {
            builder.add(textQuery, Occur.MUST);
        }
        if (bucketQuery != null) {
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

    protected Query getTermQuery(String term) {
        boolean multiTerm = term.contains(" ");
        if(!multiTerm){
            return new TermQuery(new Term("text", term));            
        }else{
            final BooleanQuery.Builder builder = new BooleanQuery.Builder();
            Arrays.stream(term.split(" ")).forEach(t -> builder.add(new TermQuery(new Term("text", t)), Occur.SHOULD));
            return builder.build();
        }
    }
}
