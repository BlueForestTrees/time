package time.web.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

import time.domain.Scale;
import time.web.bean.Bucket;
import time.web.bean.BucketGroup;
import time.web.bean.TermPeriodFilter;

@Service
public class BucketService {

    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private SortedSetDocValuesReaderState readerState;

    @Autowired
    private QueryService queryService;

    /**
     * Give the bucketGroup matching a query
     *
     * @param term
     * @return
     * @throws IOException
     */
    public BucketGroup getBuckets(final String term) throws IOException {
        final FacetsCollector facetsCollector = new FacetsCollector();
        final TermPeriodFilter termPeriodFilter = TermPeriodFilter.parse(term);
        final Query query = queryService.getQuery(termPeriodFilter);
        FacetsCollector.search(indexSearcher, query, 10, facetsCollector);
        final Facets facetsCounter = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);
        final String scale = determineScale(termPeriodFilter);
        final FacetResult facets = facetsCounter.getTopChildren(10000, scale);
        return toBucketGroup(facets, scale);
    }

    /**
     * Détermine le scale à utiliser pour une requête.
     *
     * @param termPeriodFilter contient éventuellement un from + to
     * @return le scale à utiliser.
     */
    protected String determineScale(TermPeriodFilter termPeriodFilter) {
        if (termPeriodFilter.hasFromTo()) {
            final long totalDays = Math.abs(termPeriodFilter.getTo() - termPeriodFilter.getFrom());
            return Scale.get(totalDays);
        }
        return "0";
    }


    private BucketGroup toBucketGroup(final FacetResult facetResult, final String scale) {
        final BucketGroup bucketsDTO = new BucketGroup();
        bucketsDTO.setBuckets(toBucketsList(facetResult));
        bucketsDTO.setScale(scale);
        return bucketsDTO;
    }

    private List<Bucket> toBucketsList(final FacetResult facetResult) {
        if (facetResult == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(facetResult.labelValues).map(this::toBucket).collect(Collectors.toList());
    }

    private Bucket toBucket(LabelAndValue facet) {
        final Bucket facetDTO = new Bucket();
        facetDTO.setBucket(new Long(facet.label));
        facetDTO.setCount((int) facet.value);
        return facetDTO;
    }

}
