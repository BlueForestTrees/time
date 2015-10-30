package time.web.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

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

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Autowired
	private BucketTransformer facetTransformer;

	@Autowired
	private QueryHelper queryHelper;

	/**
	 * Création d'une requête de facets
	 * 
	 * @param scale
	 *            le niveau de la recherche voir {@link Scale}
	 * @param bucket
	 * @param filter
	 * @return les facets demandés
	 */
	@Transactional
	public List<Facet> getTimeFacets(final Scale scale, final Long bucket, final String filter) {
		final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Phrase.class).get();
		final FacetingRequest facetingRequest = getFacetingRequest(scale, queryBuilder);
		final Query query = queryHelper.getQuery(scale, bucket, filter, queryBuilder);
		final FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Phrase.class);
		final FacetManager facetManager = fullTextQuery.getFacetManager();
		facetManager.enableFaceting(facetingRequest);

		return facetManager.getFacets("phrases");
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
		final List<Facet> timeFacets = getTimeFacets(scale, parentBucket, filter);
		return facetTransformer.toBucketsDTO(timeFacets, scale, parentBucket);
	}

}
