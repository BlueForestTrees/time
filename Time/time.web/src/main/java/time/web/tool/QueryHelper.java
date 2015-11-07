package time.web.tool;

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
public class QueryHelper {

    public Query getQuery(String term, String bucketName, Long bucketValue){
        boolean noBucket = StringUtils.isEmpty(bucketName) || bucketValue == null;
        boolean noTerm = StringUtils.isEmpty(term);
        
        if(noBucket && noTerm){
            return new MatchAllDocsQuery();
        }
        
        TermQuery textQuery = noTerm ? null : new TermQuery(new Term("text", term));
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(bucketName, bucketValue, bucketValue, true, true);
        
        
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if(textQuery != null){
            builder.add(textQuery, Occur.MUST);
        }
        if(bucketQuery != null){
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }
    
}
