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
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import time.web.bean.Bucket;
import time.web.bean.BucketGroup;

@Service
public class BucketService {

	@Autowired
	private IndexSearcher indexSearcher;

	@Autowired
	private SortedSetDocValuesReaderState readerState;

	@Autowired
	private QueryService queryService;

	public BucketGroup getBuckets(final String term) throws IOException {
		final String scale = "2";//mettre un scale fin? le déduire d'un count, d'un min/max?
		final FacetsCollector facetsCollector = new FacetsCollector();
		final Query query = queryService.getQuery(term);
		FacetsCollector.search(indexSearcher, query, 10, facetsCollector);
		final Facets facetsCounter = new SortedSetDocValuesFacetCounts(readerState, facetsCollector);
		final FacetResult facets = facetsCounter.getTopChildren(10000, scale);
		return toBucketsDTO(facets, scale);
	}

	private BucketGroup toBucketsDTO(final FacetResult facetResult, final String scale) {
		final BucketGroup bucketsDTO = new BucketGroup();
		bucketsDTO.setBuckets(toBucketsDTO(facetResult));
		bucketsDTO.setScale(scale);
		return bucketsDTO;
	}

	private List<Bucket> toBucketsDTO(final FacetResult facetResult) {
		if (facetResult == null) {
			return Collections.emptyList();
		}
		return Arrays.stream(facetResult.labelValues).map(this::toBucketDTO).collect(Collectors.toList());
	}

	private Bucket toBucketDTO(LabelAndValue facet) {
		final Bucket facetDTO = new Bucket();
		facetDTO.setBucket(new Long(facet.label));
		facetDTO.setCount((int) facet.value);
		return facetDTO;
	}

}
