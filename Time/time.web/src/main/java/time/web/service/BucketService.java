package time.web.service;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import time.repo.bean.Phrase;
import time.web.bean.BucketsDTO;
import time.web.enums.Scale;
import time.web.tool.QueryHelper;
import time.web.transformer.BucketTransformer;

@Service
public class BucketService {

    @Autowired
    private BucketTransformer facetTransformer;

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

    /**
     * Création d'une requête de facets
     * 
     * @param scale
     *            le niveau de la recherche voir {@link Scale}
     * @param bucket
     * @param filter
     * @return les facets demandés
     * @throws IOException 
     */
    public List<FacetResult> getTimeFacets(final Scale scale, final Long bucketValue, final String term) throws IOException {
        final String bucketName = scale.getField();
        
        FacetsCollector.search(indexSearcher, queryHelper.getQuery(term, bucketName, bucketValue), 10, facetsCollector);
        Facets facets = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);

        return facets.getAllDims(10);
    }

    /**
     * La requete des facets, il s'agit d'un count(*) groupby(scale)
     * 
     * @param scale
     * @param queryBuilder
     * @return
     */
    protected FacetingRequest getFacetingRequest(final Scale scale, final QueryBuilder queryBuilder) {
        return queryBuilder.facet().name("phrases").onField(scale.getField()).discrete().includeZeroCounts(false).createFacetingRequest();
    }

    public BucketsDTO getSubBuckets(final Scale scale, final Long parentBucket, final String filter) {
        final List<FacetResult> timeFacets = getTimeFacets(scale, parentBucket, filter);
        return facetTransformer.toBucketsDTO(timeFacets, scale, parentBucket);
    }

}
