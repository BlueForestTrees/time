package time.web.service;

import java.io.IOException;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import time.web.bean.Buckets;
import time.web.enums.Scale;
import time.web.transformer.BucketTransformer;

@Service
public class BucketService {

    @Autowired
    private int pageSize;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private SortedSetDocValuesReaderState readerState;
    
    @Autowired
    private BucketTransformer bucketTransformer;

    @Autowired
    private FacetsCollector facetsCollector;

    public Buckets getBuckets(final Scale scale, final Long bucketValue, final String term) throws IOException {
        final String bucketName = scale.getField();

        FacetsCollector.search(indexSearcher, getQuery(term, bucketName, bucketValue), 10, facetsCollector);
        Facets facetsCounter = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);

        FacetResult facets = facetsCounter.getTopChildren(10000, bucketName);
        
        return bucketTransformer.toBucketsDTO(facets, scale, bucketValue);
    }

    public Query getQuery(String term, String bucketName, Long bucketValue) {
        boolean noBucket = StringUtils.isEmpty(bucketName) || bucketValue == null;
        boolean noTerm = StringUtils.isEmpty(term);

        if (noBucket && noTerm) {
            return new MatchAllDocsQuery();
        }

        TermQuery textQuery = noTerm ? null : new TermQuery(new Term("text", term));
        Query bucketQuery = noBucket ? null : NumericRangeQuery.newLongRange(bucketName, bucketValue, bucketValue, true, true);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        if (textQuery != null) {
            builder.add(textQuery, Occur.MUST);
        }
        if (bucketQuery != null) {
            builder.add(bucketQuery, Occur.MUST);
        }
        return builder.build();
    }

}
