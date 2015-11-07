package time.web.service;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.repo.bean.Phrase;
import time.web.enums.Scale;
import time.web.tool.QueryHelper;
import time.web.transformer.PhraseTransformer;

@Service
public class PhraseService {

    @Autowired
    private int pageSize;
    
    @Autowired
    private IndexSearcher indexSearcher;  
    
    @Autowired
    private SortedSetDocValuesReaderState readerState;

    @Autowired
    private QueryHelper queryHelper;
    
    @Autowired
    private FacetsCollector facetsCollector;
    
    public List<Phrase> find(final Scale scale, final Long bucketValue, final String term) throws IOException {
        final String bucketName = scale.getField();
        
        Query query = queryHelper.getQuery(term, bucketName, bucketValue);        
        TopDocs search = indexSearcher.search(query,10);
        
        
    }

}
