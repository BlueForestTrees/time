package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.web.bean.Bucket;
import time.web.bean.Buckets;
import time.web.enums.Scale;

@Service
public class BucketService {

    @Autowired
    private int pageSize;

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private SortedSetDocValuesReaderState readerState;
    
    @Autowired
    private QueryService queryService;

    public Buckets getBuckets(final Scale scale, final Long bucketValue, final String term) throws IOException {
        final String bucketName = scale.getField();
        final FacetsCollector facetsCollector = new FacetsCollector();
        final Query query = queryService.getQuery(term, bucketName, bucketValue);
        FacetsCollector.search(indexSearcher, query, 10, facetsCollector);
        final Facets facetsCounter = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);
        final FacetResult facets = facetsCounter.getTopChildren(10000, bucketName);
        
        return toBucketsDTO(facets, scale, bucketValue);
    }
    
    protected Buckets toBucketsDTO(final FacetResult facetResult, final Scale scale, final Long parentBucket) {
        final Buckets bucketsDTO = new Buckets();
        bucketsDTO.setSubbuckets(toBucketsDTO(facetResult));
        bucketsDTO.setParentBucket(parentBucket);
        bucketsDTO.setScale(scale);
        return bucketsDTO;
    }

    protected List<Bucket> toBucketsDTO(final FacetResult facetResult) {
        if(facetResult == null){
            return Arrays.asList();
        }
        return Arrays.stream(facetResult.labelValues).map(elt -> toBucketDTO(elt)).collect(Collectors.toList());
    }

    protected Bucket toBucketDTO(LabelAndValue facet) {
        final Bucket facetDTO = new Bucket();
        facetDTO.setBucket(new Long(facet.label));
        facetDTO.setCount((int) facet.value);
        return facetDTO;
    }

}
